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
  "commandinjection.task4.hint1",
  "commandinjection.task4.hint2",
  "commandinjection.task4.hint3",
  "commandinjection.task4.hint4"
})
public class CommandInjectionTask4Key implements AssignmentEndpoint, Initializable {

  private final CommandInjectionTask4Service service;

  public CommandInjectionTask4Key(CommandInjectionTask4Service service) {
    this.service = service;
  }

  @PostMapping(
      value = "/CommandInjection/task4/key",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult submitKey(
      @CurrentUser WebGoatUser user, @RequestParam("apikey") String submittedKey) {

    service.ensureInitialized(user);

    if (submittedKey == null || submittedKey.isBlank()) {
      return failed(this).feedback("commandinjection.task4.failure.blank").build();
    }

    if (service.validateApiKey(user, submittedKey)) {
      return success(this).feedback("commandinjection.task4.success").build();
    }

    return failed(this).feedback("commandinjection.task4.failure.invalid").build();
  }

  @Override
  public void initialize(WebGoatUser user) {
    service.initialize(user);
  }
}
