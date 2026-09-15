/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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

  static final String LEAKED_TOKEN =
      String.format(Locale.ROOT, "WEBGOAT_BUILD_TOKEN=%s", UUID.randomUUID().toString());

  @PostMapping(
      value = "/CommandInjection/task2/run",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult run(
      @RequestParam("base") String baseCommand,
      @RequestParam("payload") String payload,
      @RequestParam(value = "token", required = false) String token) {

    String command = buildCommand(baseCommand, payload);
    CommandResult result = executeCommand(command);
    String output = formatOutput(result);

    if (token == null || token.isBlank()) {
      return failed(this).feedback("commandinjection.task2.failure.blank").output(output).build();
    }

    if (LEAKED_TOKEN.endsWith(token.trim())) {
      return success(this).feedback("commandinjection.task2.success").output(output).build();
    }

    return failed(this).feedback("commandinjection.task2.failure.mismatch").output(output).build();
  }

  String buildCommand(String base, String payload) {
    String os = System.getProperty("os.name", "").toLowerCase(Locale.US);
    boolean isWindows = os.contains("win");
    String shell = isWindows ? "cmd.exe /c " : "/bin/sh -c ";
    String safeBase = (base == null || base.isBlank()) ? (isWindows ? "ver" : "uname -a") : base;
    String userPart = (payload == null) ? "" : payload.trim();
    return shell + safeBase + (userPart.isEmpty() ? "" : " " + userPart);
  }

  private CommandResult executeCommand(String command) {
    boolean isWindows = System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    String shell = isWindows ? "cmd.exe" : "/bin/sh";
    String switchArg = isWindows ? "/c" : "-c";

    ProcessBuilder builder = new ProcessBuilder(shell, switchArg, command);
    builder.redirectErrorStream(true);
    builder.environment().put("WEBGOAT_BUILD_TOKEN", LEAKED_TOKEN);

    String output = "";
    boolean timedOut = false;
    String executionError = null;

    try {
      Process process = builder.start();
      boolean finished = process.waitFor(5, TimeUnit.SECONDS);
      if (!finished) {
        timedOut = true;
        process.destroyForcibly();
        process.waitFor(1, TimeUnit.SECONDS);
      }
      try (InputStream is = process.getInputStream()) {
        output = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (IOException | InterruptedException e) {
      executionError = e.getMessage();
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
    }

    return new CommandResult(command, output, timedOut, executionError);
  }

  private String formatOutput(CommandResult result) {
    StringBuilder console = new StringBuilder();
    console.append("Command: ").append(result.command()).append("\n");
    console.append(result.output());
    if (result.timedOut()) {
      console.append("\n[Process terminated after timeout]\n");
    }
    if (result.executionError() != null) {
      console.append("\n[Execution error] ").append(result.executionError());
    }
    return console.toString();
  }

  private record CommandResult(
      String command, String output, boolean timedOut, String executionError) {}
}
