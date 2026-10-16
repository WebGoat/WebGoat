/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class OpenRedirectTask2Test {

  private final OpenRedirectTask2 task = new OpenRedirectTask2();

  @Test
  void substringBypassWithExternalHostSucceeds() {
    AttackResult result = task.simulate("https://webgoat.org.attacker.com/path");

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getOutput()).contains("Bypassed naive filter");
  }

  @Test
  void allowedHostKeepsAssignmentFailed() {
    AttackResult result = task.simulate("https://webgoat.org/profile");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("Host still allowed");
  }

  @Test
  void missingKeywordFailsValidation() {
    AttackResult result = task.simulate("https://attacker.example");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("Must contain 'webgoat'");
  }
}
