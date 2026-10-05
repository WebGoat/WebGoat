/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class ConfigHardeningTaskTest {

  private ConfigHardeningTask task;

  @BeforeEach
  void setUp() {
    task = new ConfigHardeningTask();
  }

  @Test
  void shouldFailWhenAnySettingIncorrect() {
    AttackResult result = task.submitConfig("true", "never", "admin", "password");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task4.failure.invalid");
  }

  @Test
  void shouldPassWhenSettingsMatchHardenedValues() {
    AttackResult result = task.submitConfig("false", "never", "", "");

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task4.success");
  }
}
