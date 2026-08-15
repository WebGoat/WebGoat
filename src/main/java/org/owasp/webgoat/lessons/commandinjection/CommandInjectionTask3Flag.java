/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "commandinjection.task3.hint1",
  "commandinjection.task3.hint2",
  "commandinjection.task3.hint3",
  "commandinjection.task3.hint4"
})
public class CommandInjectionTask3Flag implements AssignmentEndpoint, Initializable {

  private final CommandInjectionTask3Service service;

  public CommandInjectionTask3Flag(CommandInjectionTask3Service service) {
    this.service = service;
  }

  @PostMapping(
      value = "/CommandInjection/task3/flag",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult submitFlag(
      @CurrentUser WebGoatUser user, @RequestParam("flag") String submittedFlag) {
    if (submittedFlag == null || submittedFlag.isBlank()) {
      return failed(this).feedback("commandinjection.task3.failure.blank").build();
    }

    if (service.validateFlag(user, submittedFlag)) {
      return success(this).feedback("commandinjection.task3.success").build();
    }

    return failed(this).feedback("commandinjection.task3.failure.invalid").build();
  }

  @Override
  public void initialize(WebGoatUser user) {
    service.initialize(user);
  }
}
