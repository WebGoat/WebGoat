/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class CommandInjectionTask2Test {

  private CommandInjectionTask2 task;

  @BeforeEach
  void setUp() {
    task = new CommandInjectionTask2();
  }

  @Test
  void shouldBuildCommandWithPayload() {
    String command = task.buildCommand("uname -a", "; whoami");

    assertThat(command).contains("whoami");
  }

  @Test
  void shouldFailWhenTokenMissing() {
    AttackResult result = task.run("uname -a", "; whoami", "");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task2.failure.blank");
  }

  @Test
  void shouldSucceedWhenTokenMatches() {
    String output = task.simulateExecution(task.buildCommand("uname -a", ""));
    String token = output.substring(output.indexOf('=') + 1).trim();

    AttackResult result = task.run("uname -a", "", token);

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task2.success");
  }
}
