/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.commandinjection.CommandInjectionTask5Service.Configuration;
import org.owasp.webgoat.lessons.commandinjection.CommandInjectionTask5Service.ExecutionMode;

class CommandInjectionTask5ServiceTest {

  private final CommandInjectionTask5Service service =
      new CommandInjectionTask5Service(new CommandExecutionService());

  @Test
  void shellConfigurationShouldFailEvenWithAllowlist() {
    var configuration = new Configuration(ExecutionMode.SHELL, true, true);

    var result = service.evaluate(configuration);

    assertThat(result.success()).isFalse();
    assertThat(result.messageKey()).isEqualTo("commandinjection.task5.failure.shell");
  }

  @Test
  void allowlistExecutionShouldSucceed() {
    var configuration = new Configuration(ExecutionMode.ALLOWLIST_ONLY, true, true);

    var result = service.evaluate(configuration);

    assertThat(result.success()).isTrue();
    assertThat(result.messageKey()).isEqualTo("commandinjection.task5.success");
  }

  @Test
  void directProcessWithoutAllowlistShouldRejectUnapprovedCommands() {
    var configuration = new Configuration(ExecutionMode.DIRECT_PROCESS, false, false);

    var result = service.evaluate(configuration);

    assertThat(result.success()).isFalse();
    assertThat(result.messageKey()).isEqualTo("commandinjection.task5.failure.injection");
    assertThat(result.report()).contains("Unapproved command: allowed");
  }

  @Test
  void fixedAllowlistModeShouldNotNeedTheSeparateAllowlistSwitch() {
    var configuration = new Configuration(ExecutionMode.ALLOWLIST_ONLY, false, false);

    assertThat(service.evaluate(configuration).success()).isTrue();
  }
}
