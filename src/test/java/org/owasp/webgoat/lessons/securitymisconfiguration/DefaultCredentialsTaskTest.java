/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class DefaultCredentialsTaskTest {

  private DefaultCredentialsTask task;

  @BeforeEach
  void setUp() {
    task = new DefaultCredentialsTask();
  }

  @Test
  void shouldFailWhenUsernameOrPasswordMissing() {
    AttackResult result = task.login("", "admin");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task1.failure.blank");
  }

  @Test
  void shouldFailWithWrongCredentials() {
    AttackResult result = task.login("admin", "wrong");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task1.failure.invalid");
  }

  @Test
  void shouldSucceedWithDefaultCredentials() {
    AttackResult result = task.login("admin", "admin");

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task1.success");
  }
}
