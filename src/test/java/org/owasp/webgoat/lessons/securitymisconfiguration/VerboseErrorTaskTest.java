/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.ResponseEntity;

class VerboseErrorTaskTest {

  private VerboseErrorTask task;

  @BeforeEach
  void setUp() {
    task = new VerboseErrorTask();
  }

  @Test
  void triggerShouldLeakTokenInStackTrace() {
    ResponseEntity<String> response = task.triggerError();

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).contains(VerboseErrorTask.LEAKED_TOKEN);
  }

  @Test
  void shouldFailWhenTokenMissing() {
    AttackResult result = task.submitToken("");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task2.failure.blank");
  }

  @Test
  void shouldFailWithIncorrectToken() {
    AttackResult result = task.submitToken("WRONG");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task2.failure.invalid");
  }


  @Test
  void configEndpointShouldRequireToken() {
    var response = task.fetchConfig(null);
    assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.FORBIDDEN);
  }

  @Test
  void configEndpointShouldReturnConfigWhenTokenMatches() {
    var response = task.fetchConfig(VerboseErrorTask.LEAKED_TOKEN);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).contains("debug");
  }

  @Test
  void shouldPassWhenCorrectTokenProvided() {
    AttackResult result = task.submitToken(VerboseErrorTask.LEAKED_TOKEN);

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("securitymisconfiguration.task2.success");
  }
}
