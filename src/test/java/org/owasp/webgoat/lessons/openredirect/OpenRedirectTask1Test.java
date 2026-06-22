/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class OpenRedirectTask1Test {

  private final OpenRedirectTask1 task = new OpenRedirectTask1();

  @Test
  void externalAbsoluteUrlMarksAssignmentSolved() {
    AttackResult result = task.simulate("https://evil.example");

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getOutput()).contains("Would redirect to: https://evil.example");
  }

  @Test
  void internalHostIsRejected() {
    AttackResult result = task.simulate("https://webgoat.local/dashboard");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("Internal host");
  }

  @Test
  void nonAbsoluteUrlFailsWithHelpfulMessage() {
    AttackResult result = task.simulate("/relative/path");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("Needs absolute URL with http/https");
  }
}
