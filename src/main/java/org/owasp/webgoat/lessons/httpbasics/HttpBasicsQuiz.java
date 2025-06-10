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
@AssignmentHints({
  "http-basics.hints.http_basic_quiz.1",
  "http-basics.hints.http_basic_quiz.2",
  "http-basics.hints.http_basic_quiz.3"
})
public class HttpBasicsQuiz implements AssignmentEndpoint {

  @PostMapping("/HttpBasics/attack2")
  @ResponseBody
  public AttackResult completed(
      @RequestParam String answer,
      @RequestParam String magic_answer,
      @RequestParam String magic_num) {
    if ("POST".equalsIgnoreCase(answer) && magic_answer.equals(magic_num)) {
      return success(this).build();
    } else {
      if (!"POST".equalsIgnoreCase(answer)) {
        return failed(this).feedback("http-basics.incorrect").build();
      }
      if (!magic_answer.equals(magic_num)) {
        return failed(this).feedback("http-basics.magic").build();
      }
    }
    return failed(this).build();
  }
}
