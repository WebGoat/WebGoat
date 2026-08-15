/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class CommandInjectionTask5Service {

  private static final Set<String> ALLOWLIST = Set.of("hostname", "uptime");

  public EvaluationResult evaluate(Configuration configuration) {
    boolean statusAllowed = allowsStatus(configuration);
    boolean injectionBlocked = blocksInjection(configuration);
    boolean usesShell = configuration.executionMode() == ExecutionMode.SHELL;

    if (statusAllowed && injectionBlocked && !usesShell) {
      return EvaluationResult.success(
          "commandinjection.task5.success", "commandinjection.task5.remediation");
    }

    return EvaluationResult.failure(
        buildFailureMessage(statusAllowed, injectionBlocked, usesShell), configuration);
  }

  private boolean allowsStatus(Configuration configuration) {
    return switch (configuration.executionMode()) {
      case SHELL ->
          configuration.sanitiserEnabled()
              && configuration.allowlistEnabled()
              && ALLOWLIST.contains("hostname");
      case DIRECT_PROCESS, ALLOWLIST_ONLY ->
          configuration.allowlistEnabled() && ALLOWLIST.contains("hostname");
    };
  }

  private boolean blocksInjection(Configuration configuration) {
    return switch (configuration.executionMode()) {
      case SHELL -> configuration.sanitiserEnabled() && configuration.allowlistEnabled();
      case DIRECT_PROCESS -> configuration.allowlistEnabled();
      case ALLOWLIST_ONLY -> true;
    };
  }

  private String buildFailureMessage(
      boolean statusAllowed, boolean injectionBlocked, boolean usesShell) {
    if (!statusAllowed && !injectionBlocked) {
      return "commandinjection.task5.failure.both";
    }
    if (usesShell && statusAllowed && injectionBlocked) {
      return "commandinjection.task5.failure.shell";
    }
    if (!statusAllowed) {
      return "commandinjection.task5.failure.status";
    }
    if (!injectionBlocked) {
      return "commandinjection.task5.failure.injection";
    }
    return "commandinjection.task5.failure.generic";
  }

  public record Configuration(
      ExecutionMode executionMode, boolean allowlistEnabled, boolean sanitiserEnabled) {}

  public enum ExecutionMode {
    SHELL,
    DIRECT_PROCESS,
    ALLOWLIST_ONLY
  }

  public record EvaluationResult(boolean success, String messageKey, String remediationHint) {

    public static EvaluationResult success(String messageKey, String remediationHint) {
      return new EvaluationResult(true, messageKey, remediationHint);
    }

    public static EvaluationResult failure(String messageKey, Configuration configuration) {
      String hint;
      if (configuration.executionMode() == ExecutionMode.SHELL) {
        hint = "commandinjection.task5.hint.shell";
      } else if (!configuration.allowlistEnabled()) {
        hint = "commandinjection.task5.hint.allowlist";
      } else {
        hint = "commandinjection.task5.hint.sanitiser";
      }
      return new EvaluationResult(false, messageKey, hint);
    }
  }
}
