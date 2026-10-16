/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import static org.owasp.webgoat.lessons.commandinjection.CommandExecutionService.isWindows;

import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Safety confirmation before running command injection tasks. */
@RestController
@AssignmentHints({"commandinjection.safety.hint1"})
public class CommandInjectionSafetyGate implements AssignmentEndpoint {

  private static final String REQUIRED_PHRASE = "I understand commands will execute";
  private final CommandInjectionSafetyService safetyService;
  private final boolean runningInDocker;

  public CommandInjectionSafetyGate(
      CommandInjectionSafetyService safetyService,
      @Value("${running.in.docker:false}") boolean runningInDocker) {
    this.safetyService = safetyService;
    this.runningInDocker = runningInDocker;
  }

  @GetMapping(
      value = "/CommandInjection/safety/status",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public SafetyStatus status(@CurrentUser WebGoatUser user) {
    return new SafetyStatus(
        runningInDocker,
        System.getProperty("os.name", "Unknown"),
        isWindows() ? "cmd.exe" : "/bin/sh",
        safetyService.isAcknowledged(user));
  }

  @PostMapping(
      value = "/CommandInjection/safety",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public AttackResult acknowledge(
      @CurrentUser WebGoatUser user, @RequestParam("ack") String acknowledgement) {
    if (REQUIRED_PHRASE.equalsIgnoreCase(acknowledgement.trim())) {
      safetyService.acknowledge(user);
      return success(this)
          .feedback("commandinjection.safety.success")
          .output("Proceed with caution. Shell commands will run on your host.")
          .build();
    }
    return failed(this)
        .feedback("commandinjection.safety.failure")
        .build();
  }

  public record SafetyStatus(
      boolean runningInDocker,
      String operatingSystem,
      String commandShell,
      boolean commandExecutionUnlocked) {}
}
