/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xss;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints(
    value = {
      "xss-reflected-6a-hint-1",
      "xss-reflected-6a-hint-2",
      "xss-reflected-6a-hint-3",
      "xss-reflected-6a-hint-4"
    })
public class CrossSiteScriptingLesson6a implements AssignmentEndpoint {
  private final LessonSession userSessionData;

  public CrossSiteScriptingLesson6a(LessonSession userSessionData) {
    this.userSessionData = userSessionData;
  }

  @PostMapping("/CrossSiteScripting/attack6a")
  @ResponseBody
  public AttackResult completed(@RequestParam String DOMTestRoute) {

    if (DOMTestRoute.matches("start\\.mvc#test(\\/|)")) {
      // return )
      return success(this).feedback("xss-reflected-6a-success").build();
    } else {
      return failed(this).feedback("xss-reflected-6a-failure").build();
    }
  }
}
