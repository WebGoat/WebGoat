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

package org.owasp.webgoat.lessons.xss.stored;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import static org.springframework.http.MediaType.ALL_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.xss.Comment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoredXssComments implements AssignmentEndpoint {

  private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");

  private static final Map<String, List<Comment>> userComments = new HashMap<>();
  private static final List<Comment> comments = new ArrayList<>();
  private static final String phoneHomeString = "<script>webgoat.customjs.phoneHome()</script>";

  static {
    comments.add(
        new Comment(
            "secUriTy",
            LocalDateTime.now().format(fmt),
            "<script>console.warn('unit test me')</script>Comment for Unit Testing"));
    comments.add(new Comment("webgoat", LocalDateTime.now().format(fmt), "This comment is safe"));
    comments.add(new Comment("guest", LocalDateTime.now().format(fmt), "This one is safe too."));
    comments.add(
        new Comment(
            "guest",
            LocalDateTime.now().format(fmt),
            "Can you post a comment, calling webgoat.customjs.phoneHome() ?"));
  }

  @GetMapping(
      path = "/CrossSiteScriptingStored/stored-xss",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = ALL_VALUE)
  @ResponseBody
  public Collection<Comment> retrieveComments(@CurrentUsername String username) {
    List<Comment> allComments = Lists.newArrayList();
    Collection<Comment> newComments = userComments.get(username);
    allComments.addAll(comments);
    if (newComments != null) {
      allComments.addAll(newComments);
    }
    Collections.reverse(allComments);
    return allComments;
  }

  @PostMapping("/CrossSiteScriptingStored/stored-xss")
  @ResponseBody
  public AttackResult createNewComment(
      @RequestBody String commentStr, @CurrentUsername String username) {
    Comment comment = parseJson(commentStr);

    List<Comment> comments = userComments.getOrDefault(username, new ArrayList<>());
    comment.setDateTime(LocalDateTime.now().format(fmt));
    comment.setUser(username);

    comments.add(comment);
    userComments.put(username, comments);

    if (comment.getText().contains(phoneHomeString)) {
      return (success(this).feedback("xss-stored-comment-success").build());
    } else {
      return (failed(this).feedback("xss-stored-comment-failure").build());
    }
  }

  private Comment parseJson(String comment) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(comment, Comment.class);
    } catch (IOException e) {
      return new Comment();
    }
  }
}
