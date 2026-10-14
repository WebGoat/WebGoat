/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.util.Locale;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Task 1: recognise the vulnerable command wrapper. */
@RestController
@AssignmentHints({"commandinjection.task1.hint1", "commandinjection.task1.hint2"})
public class CommandInjectionTask1 implements AssignmentEndpoint {

  @PostMapping(
      value = "/CommandInjection/task1/run",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult runCommand(
      @RequestParam("host") String host,
      @RequestParam(value = "custom") String custom,
      @RequestParam(value = "observed", required = false) String observedCommand) {

    String sanitizedHost = host == null ? "" : host.trim();
    String userCommand = custom == null ? "" : custom.trim();

    String baseCommand = buildCommand(sanitizedHost, userCommand);

    if (observedCommand == null || observedCommand.isBlank()) {
      return failed(this)
          .feedback("commandinjection.task1.failure.empty")
          .output(baseCommand)
          .build();
    }

    if (baseCommand.equals(observedCommand.trim())) {
      return success(this)
          .feedback("commandinjection.task1.success")
          .output(baseCommand)
          .build();
    }

    return failed(this)
        .feedback("commandinjection.task1.failure.mismatch")
        .output(baseCommand)
        .build();
  }

  String buildCommand(String host, String custom) {
    String os = System.getProperty("os.name", "").toLowerCase(Locale.US);
    boolean isWindows = os.contains("win");
    String base = isWindows ? "cmd.exe /c ping -n 1 " : "/bin/sh -c ping -c 1 ";
    if (custom.isEmpty()) {
      return base + host;
    }
    return base + custom;
  }
}
