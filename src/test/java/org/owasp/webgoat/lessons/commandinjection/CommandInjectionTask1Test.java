/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class CommandInjectionTask1Test {

  private CommandInjectionTask1 task;

  @BeforeEach
  void setUp() {
    task = new CommandInjectionTask1();
  }

  @Test
  void shouldBuildCommandBasedOnHost() {
    String command = task.buildCommand("localhost", "");

    assertThat(command).contains("localhost");
  }

  @Test
  void shouldFailWhenObservedCommandMissing() {
    AttackResult result = task.runCommand("localhost", "", "");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task1.failure.empty");
  }

  @Test
  void shouldSucceedWhenObservedMatchesActualCommand() {
    String base = task.buildCommand("localhost", "");

    AttackResult result = task.runCommand("localhost", "", base);

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task1.success");
  }
}
