/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class CommandInjectionSafetyGateTest {

  private CommandInjectionSafetyGate gate;

  @BeforeEach
  void setUp() {
    gate = new CommandInjectionSafetyGate();
  }

  @Test
  void acknowledgementShouldBeRequiredPhrase() {
    AttackResult result = gate.acknowledge("I understand commands will execute");

    assertThat(result.assignmentSolved()).isTrue();
  }

  @Test
  void incorrectAcknowledgementShouldFail() {
    AttackResult result = gate.acknowledge("ok");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.safety.failure");
  }
}
