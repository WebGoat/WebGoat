/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.chromedevtools;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NetworkDummy implements AssignmentEndpoint {

  private final LessonSession lessonSession;

  public NetworkDummy(LessonSession lessonSession) {
    this.lessonSession = lessonSession;
  }

  @PostMapping("/ChromeDevTools/dummy")
  @ResponseBody
  public AttackResult completed(@RequestParam String successMessage) {
    String answer = (String) lessonSession.getValue("randValue");

    if (successMessage != null && successMessage.equals(answer)) {
      return success(this).feedback("xss-dom-message-success").build();
    } else {
      return failed(this).feedback("xss-dom-message-failure").build();
    }
  }
}
