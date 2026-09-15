/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommandInjectionTask5ServiceTest {

  private final CommandInjectionTask5Service service = new CommandInjectionTask5Service();

  @Test
  void shellConfigurationShouldFailEvenWithAllowlist() {
    var configuration =
        new CommandInjectionTask5Service.Configuration(
            CommandInjectionTask5Service.ExecutionMode.SHELL, true, true);

    var result = service.evaluate(configuration);

    assertThat(result.success()).isFalse();
    assertThat(result.messageKey()).isEqualTo("commandinjection.task5.failure.shell");
  }

  @Test
  void allowlistExecutionShouldSucceed() {
    var configuration =
        new CommandInjectionTask5Service.Configuration(
            CommandInjectionTask5Service.ExecutionMode.ALLOWLIST_ONLY, true, true);

    var result = service.evaluate(configuration);

    assertThat(result.success()).isTrue();
    assertThat(result.messageKey()).isEqualTo("commandinjection.task5.success");
  }
}
