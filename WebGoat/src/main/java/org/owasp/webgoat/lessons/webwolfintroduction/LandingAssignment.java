/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webwolfintroduction;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LandingAssignment implements AssignmentEndpoint {
  private final String landingPageUrl;

  public LandingAssignment(@Value("${webwolf.landingpage.url}") String landingPageUrl) {
    this.landingPageUrl = landingPageUrl;
  }

  @PostMapping("/WebWolf/landing")
  @ResponseBody
  public AttackResult click(String uniqueCode, @CurrentUsername String username) {
    if (StringUtils.reverse(username).equals(uniqueCode)) {
      return success(this).build();
    }
    return failed(this).feedback("webwolf.landing_wrong").build();
  }

  @GetMapping("/WebWolf/landing/password-reset")
  public ModelAndView openPasswordReset(@CurrentUsername String username) {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject(
        "webwolfLandingPageUrl", landingPageUrl.replace("//landing", "/landing"));
    modelAndView.addObject("uniqueCode", StringUtils.reverse(username));

    modelAndView.setViewName("lessons/webwolfintroduction/templates/webwolfPasswordReset.html");
    return modelAndView;
  }
}
