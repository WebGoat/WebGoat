/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.csrf;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Created by jason on 9/29/17. */
@RestController
@AssignmentHints({"csrf-get.hint1", "csrf-get.hint2", "csrf-get.hint3", "csrf-get.hint4"})
public class CSRFConfirmFlag1 implements AssignmentEndpoint {

  private final LessonSession userSessionData;

  public CSRFConfirmFlag1(LessonSession userSessionData) {
    this.userSessionData = userSessionData;
  }

  @PostMapping(
      path = "/csrf/confirm-flag-1",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult completed(String confirmFlagVal) {
    Object userSessionDataStr = userSessionData.getValue("csrf-get-success");
    if (userSessionDataStr != null && confirmFlagVal.equals(userSessionDataStr.toString())) {
      return success(this)
          .feedback("csrf-get-null-referer.success")
          .output("Correct, the flag was " + userSessionData.getValue("csrf-get-success"))
          .build();
    }

    return failed(this).build();
  }
}
