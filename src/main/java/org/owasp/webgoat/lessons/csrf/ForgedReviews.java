/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.csrf;

import static org.springframework.http.MediaType.ALL_VALUE;

import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"csrf-review-hint1", "csrf-review-hint2", "csrf-review-hint3"})
public class ForgedReviews extends AssignmentEndpoint {

  private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");

  private static final Map<String, List<Review>> userReviews = new HashMap<>();
  private static final List<Review> REVIEWS = new ArrayList<>();
  private static final String weakAntiCSRF = "2aa14227b9a13d0bede0388a7fba9aa9";

  static {
    REVIEWS.add(
        new Review("secUriTy", LocalDateTime.now().format(fmt), "This is like swiss cheese", 0));
    REVIEWS.add(new Review("webgoat", LocalDateTime.now().format(fmt), "It works, sorta", 2));
    REVIEWS.add(new Review("guest", LocalDateTime.now().format(fmt), "Best, App, Ever", 5));
    REVIEWS.add(
        new Review(
            "guest",
            LocalDateTime.now().format(fmt),
            "This app is so insecure, I didn't even post this review, can you pull that off too?",
            1));
  }

  @GetMapping(
      path = "/csrf/review",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = ALL_VALUE)
  @ResponseBody
  public Collection<Review> retrieveReviews(@CurrentUsername String username) {
    Collection<Review> allReviews = Lists.newArrayList();
    Collection<Review> newReviews = userReviews.get(username);
    if (newReviews != null) {
      allReviews.addAll(newReviews);
    }

    allReviews.addAll(REVIEWS);

    return allReviews;
  }

  @PostMapping("/csrf/review")
  @ResponseBody
  public AttackResult createNewReview(
      String reviewText,
      Integer stars,
      String validateReq,
      HttpServletRequest request,
      @CurrentUsername String username) {
    final String host = (request.getHeader("host") == null) ? "NULL" : request.getHeader("host");
    final String referer =
        (request.getHeader("referer") == null) ? "NULL" : request.getHeader("referer");
    final String[] refererArr = referer.split("/");

    Review review = new Review();
    review.setText(reviewText);
    review.setDateTime(LocalDateTime.now().format(fmt));
    review.setUser(username);
    review.setStars(stars);
    var reviews = userReviews.getOrDefault(username, new ArrayList<>());
    reviews.add(review);
    userReviews.put(username, reviews);
    // short-circuit
    if (validateReq == null || !validateReq.equals(weakAntiCSRF)) {
      return failed(this).feedback("csrf-you-forgot-something").build();
    }
    // we have the spoofed files
    if (referer != "NULL" && refererArr[2].equals(host)) {
      return failed(this).feedback("csrf-same-host").build();
    } else {
      return success(this)
          .feedback("csrf-review.success")
          .build(); // feedback("xss-stored-comment-failure")
    }
  }
}
