/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.util.Locale;
import java.util.UUID;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Task 2: demonstrate simple command chaining injection. */
@RestController
@AssignmentHints({"commandinjection.task2.hint1", "commandinjection.task2.hint2"})
public class CommandInjectionTask2 implements AssignmentEndpoint {

  static final String LEAKED_TOKEN = UUID.randomUUID().toString();

  @PostMapping(
      value = "/CommandInjection/task2/run",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult run(
      @RequestParam("base") String baseCommand,
      @RequestParam("payload") String payload,
      @RequestParam(value = "token", required = false) String token) {

    String command = buildCommand(baseCommand, payload);
    String output = simulateExecution(command);

    if (token == null || token.isBlank()) {
      return failed(this)
          .feedback("commandinjection.task2.failure.blank")
          .output(output)
          .build();
    }

    if (LEAKED_TOKEN.equals(token.trim())) {
      return success(this)
          .feedback("commandinjection.task2.success")
          .output(output)
          .build();
    }

    return failed(this)
        .feedback("commandinjection.task2.failure.mismatch")
        .output(output)
        .build();
  }

  String buildCommand(String base, String payload) {
    String os = System.getProperty("os.name", "").toLowerCase(Locale.US);
    boolean isWindows = os.contains("win");
    String shell = isWindows ? "cmd.exe /c " : "/bin/sh -c ";
    String safeBase = (base == null || base.isBlank()) ? (isWindows ? "ver" : "uname -a") : base;
    String userPart = (payload == null) ? "" : payload.trim();
    return shell + safeBase + (userPart.isEmpty() ? "" : " " + userPart);
  }

  String simulateExecution(String command) {
    return command + "\n" + "WEBGOAT_BUILD_TOKEN=" + LEAKED_TOKEN;
  }
}
