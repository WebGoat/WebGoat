/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpbasics;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"http-basics.hints.http_basics_lesson.1"})
public class HttpBasicsLesson implements AssignmentEndpoint {

  @PostMapping("/HttpBasics/attack1")
  @ResponseBody
  public AttackResult completed(@RequestParam String person) {
    if (!person.isBlank()) {
      return success(this)
          .feedback("http-basics.reversed")
          .feedbackArgs(new StringBuilder(person).reverse().toString())
          .build();
    } else {
      return failed(this).feedback("http-basics.empty").build();
    }
  }
}
