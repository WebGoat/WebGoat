/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.assignments.AttackResultBuilder;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "commandinjection.task5.hint1",
  "commandinjection.task5.hint2",
  "commandinjection.task5.hint3",
  "commandinjection.task5.hint4"
})
public class CommandInjectionTask5Evaluation implements AssignmentEndpoint, Initializable {

  private final CommandInjectionTask5Service service;

  public CommandInjectionTask5Evaluation(CommandInjectionTask5Service service) {
    this.service = service;
  }

  @PostMapping(
      value = "/CommandInjection/task5/evaluate",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult evaluate(
      @CurrentUser WebGoatUser user,
      @RequestParam("mode") String mode,
      @RequestParam(value = "allowlist", defaultValue = "false") boolean allowlist,
      @RequestParam(value = "sanitiser", defaultValue = "false") boolean sanitiser) {

    CommandInjectionTask5Service.Configuration configuration =
        new CommandInjectionTask5Service.Configuration(
            CommandInjectionTask5Service.ExecutionMode.valueOf(mode), allowlist, sanitiser);

    CommandInjectionTask5Service.EvaluationResult result = service.evaluate(configuration);

    if (result.success()) {
      return AttackResultBuilder.success(this)
          .feedback(result.messageKey())
          .output(result.remediationHint())
          .build();
    }

    return AttackResultBuilder.failed(this)
        .feedback(result.messageKey())
        .output(result.remediationHint())
        .build();
  }
}
