/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.owasp.webgoat.lessons.commandinjection.CommandInjectionSafetyGate.SafetyStatus;
import org.springframework.web.server.ResponseStatusException;

class CommandInjectionSafetyGateTest {

  private CommandInjectionSafetyGate gate;
  private CommandInjectionSafetyService safetyService;
  private WebGoatUser user;

  @BeforeEach
  void setUp() {
    safetyService = new CommandInjectionSafetyService();
    gate = new CommandInjectionSafetyGate(safetyService, false);
    user = new WebGoatUser("alice", "password");
  }

  @Test
  void acknowledgementShouldBeRequiredPhrase() {
    AttackResult result = gate.acknowledge(user, "I understand commands will execute");

    assertThat(result.assignmentSolved()).isTrue();
    assertThatCode(() -> safetyService.requireAcknowledgement(user)).doesNotThrowAnyException();
  }

  @Test
  void incorrectAcknowledgementShouldFail() {
    AttackResult result = gate.acknowledge(user, "ok");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.safety.failure");
    assertThatThrownBy(() -> safetyService.requireAcknowledgement(user))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void lessonInitializationShouldLockCommandExecutionAgain() {
    gate.acknowledge(user, "I understand commands will execute");

    safetyService.initialize(user);

    assertThatThrownBy(() -> safetyService.requireAcknowledgement(user))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void statusShouldReportRuntimeAndAcknowledgementState() {
    SafetyStatus lockedStatus = gate.status(user);

    assertThat(lockedStatus.runningInDocker()).isFalse();
    assertThat(lockedStatus.operatingSystem()).isNotBlank();
    assertThat(lockedStatus.commandShell()).isNotBlank();
    assertThat(lockedStatus.commandExecutionUnlocked()).isFalse();

    gate.acknowledge(user, "I understand commands will execute");

    assertThat(gate.status(user).commandExecutionUnlocked()).isTrue();
  }
}
