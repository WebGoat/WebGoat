/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class ActuatorExposureTaskTest {

  private ActuatorExposureTask task;

  @BeforeEach
  void setUp() {
    task = new ActuatorExposureTask();
  }

  @Test
  void envShouldExposeApiKey() {
    Map<String, Object> response = task.actuatorEnv();
    assertThat(response.get("systemApiKey")).isEqualTo(ActuatorExposureTask.LEAKED_API_KEY);
  }

  @Test
  void healthShouldReturnStatus() {
    Map<String, Object> response = task.actuatorHealth();
    assertThat(response.get("status")).isEqualTo("UP");
  }

  @Test
  void submitShouldFailWhenBlank() {
    AttackResult result = task.submitApiKey("");
    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task3.failure.blank");
  }

  @Test
  void submitShouldFailWhenIncorrect() {
    AttackResult result = task.submitApiKey("WRONG");
    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task3.failure.invalid");
  }

  @Test
  void submitShouldSucceedWithLeakedKey() {
    AttackResult result = task.submitApiKey(ActuatorExposureTask.LEAKED_API_KEY);
    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task3.success");
  }
}
