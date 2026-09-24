/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.owasp.webgoat.lessons.commandinjection.CommandExecutionService.CommandExecutionResult;
import org.springframework.stereotype.Service;

@Service
public class CommandInjectionTask5Service {

  private static final String INJECTION_MARKER = "WEBGOAT_COMMAND_INJECTION_SUCCEEDED";
  private static final Set<String> ALLOWLIST = Set.of("hostname", "uptime");
  private static final List<String> SANITIZED_TOKENS = List.of(";", "&&", "|", "\r", "\n");

  private final CommandExecutionService commandExecutionService;

  public CommandInjectionTask5Service(CommandExecutionService commandExecutionService) {
    this.commandExecutionService = commandExecutionService;
  }

  public EvaluationResult evaluate(Configuration configuration) {
    ProbeResult status = execute(configuration, "hostname");
    ProbeResult injection = execute(configuration, injectionPayload());
    ProbeResult unapproved = execute(configuration, "whoami");

    boolean statusAllowed = status.succeeded();
    boolean injectionBlocked =
        injection.rejected() || !injection.output().contains(INJECTION_MARKER);
    boolean unapprovedBlocked = unapproved.rejected();
    boolean usesShell = configuration.executionMode() == ExecutionMode.SHELL;
    boolean secure = statusAllowed && injectionBlocked && unapprovedBlocked && !usesShell;

    String report = buildReport(statusAllowed, injectionBlocked, unapprovedBlocked, usesShell);
    if (secure) {
      return new EvaluationResult(true, "commandinjection.task5.success", report);
    }

    return new EvaluationResult(
        false,
        buildFailureMessage(statusAllowed, injectionBlocked, unapprovedBlocked, usesShell),
        report);
  }

  private ProbeResult execute(Configuration configuration, String requestedCommand) {
    String command =
        configuration.sanitiserEnabled() ? sanitise(requestedCommand) : requestedCommand;
    boolean usesFixedAllowlist = configuration.executionMode() == ExecutionMode.ALLOWLIST_ONLY;
    if ((configuration.allowlistEnabled() || usesFixedAllowlist)
        && !ALLOWLIST.contains(command.trim())) {
      return ProbeResult.rejectedCommand();
    }

    CommandExecutionResult result =
        configuration.executionMode() == ExecutionMode.SHELL
            ? commandExecutionService.execute(null, command)
            : commandExecutionService.executeDirect(null, List.of(command.trim()), Map.of());
    return ProbeResult.executed(result);
  }

  private String injectionPayload() {
    return CommandExecutionService.isWindows()
        ? "hostname & echo " + INJECTION_MARKER
        : "hostname; echo " + INJECTION_MARKER;
  }

  private String sanitise(String command) {
    String sanitised = command;
    for (String token : SANITIZED_TOKENS) {
      sanitised = sanitised.replace(token, " ");
    }
    return sanitised.trim();
  }

  private String buildFailureMessage(
      boolean statusAllowed,
      boolean injectionBlocked,
      boolean unapprovedBlocked,
      boolean usesShell) {
    if (!statusAllowed) {
      return "commandinjection.task5.failure.status";
    }
    if (!injectionBlocked || !unapprovedBlocked) {
      return "commandinjection.task5.failure.injection";
    }
    if (usesShell) {
      return "commandinjection.task5.failure.shell";
    }
    return "commandinjection.task5.failure.generic";
  }

  private String buildReport(
      boolean statusAllowed,
      boolean injectionBlocked,
      boolean unapprovedBlocked,
      boolean usesShell) {
    return "Status command: "
        + passFail(statusAllowed)
        + "\nChained injection: "
        + blockedAllowed(injectionBlocked)
        + "\nUnapproved command: "
        + blockedAllowed(unapprovedBlocked)
        + "\nShell execution: "
        + (usesShell ? "enabled" : "disabled");
  }

  private String passFail(boolean passed) {
    return passed ? "passed" : "failed";
  }

  private String blockedAllowed(boolean blocked) {
    return blocked ? "blocked" : "allowed";
  }

  public record Configuration(
      ExecutionMode executionMode, boolean allowlistEnabled, boolean sanitiserEnabled) {}

  public enum ExecutionMode {
    SHELL,
    DIRECT_PROCESS,
    ALLOWLIST_ONLY
  }

  public record EvaluationResult(boolean success, String messageKey, String report) {}

  private record ProbeResult(
      boolean rejected, CommandExecutionResult executionResult) {

    private static ProbeResult rejectedCommand() {
      return new ProbeResult(true, null);
    }

    private static ProbeResult executed(CommandExecutionResult executionResult) {
      return new ProbeResult(false, executionResult);
    }

    private boolean succeeded() {
      return !rejected
          && !executionResult.timedOut()
          && executionResult.executionError() == null
          && Integer.valueOf(0).equals(executionResult.exitCode());
    }

    private String output() {
      return executionResult == null ? "" : executionResult.output();
    }
  }
}
