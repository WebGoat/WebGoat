/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.mitigation;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.sqlinjection.advanced.SqlInjectionLesson6a;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints(
    value = {
      "SqlOnlyInputValidationOnKeywords-1",
      "SqlOnlyInputValidationOnKeywords-2",
      "SqlOnlyInputValidationOnKeywords-3"
    })
public class SqlOnlyInputValidationOnKeywords implements AssignmentEndpoint {

  private final SqlInjectionLesson6a lesson6a;

  public SqlOnlyInputValidationOnKeywords(SqlInjectionLesson6a lesson6a) {
    this.lesson6a = lesson6a;
  }

  @PostMapping("/SqlOnlyInputValidationOnKeywords/attack")
  @ResponseBody
  public AttackResult attack(
      @RequestParam("userid_sql_only_input_validation_on_keywords") String userId) {
    userId = userId.toUpperCase().replace("FROM", "").replace("SELECT", "");
    if (userId.contains(" ")) {
      return failed(this).feedback("SqlOnlyInputValidationOnKeywords-failed").build();
    }
    AttackResult attackResult = lesson6a.injectableQuery(userId);
    return new AttackResult(
        attackResult.isLessonCompleted(),
        attackResult.getFeedback(),
        attackResult.getFeedbackArgs(),
        attackResult.getOutput(),
        attackResult.getOutputArgs(),
        getClass().getSimpleName(),
        true);
  }
}
