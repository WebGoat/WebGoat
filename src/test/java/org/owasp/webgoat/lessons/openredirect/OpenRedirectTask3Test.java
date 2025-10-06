/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class OpenRedirectTask3Test {

  private final OpenRedirectTask3 task = new OpenRedirectTask3();

  @Test
  void userinfoBypassMarksAssignmentSolved() {
    AttackResult result = task.challenge("https://webgoat.local@evil.com", null);

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getOutput()).contains("RealHost: evil.com");
    assertThat(result.getOutput()).contains("Bypassed flawed normalization");
  }

  @Test
  void obviousExternalTargetFailsChallenge() {
    AttackResult result = task.challenge("https://attacker.example", null);

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("AppearsInternal: false");
  }

  @Test
  void tokenOutputIsEscaped() {
    AttackResult result = task.challenge("https://webgoat.local@evil.com", "<script>alert(1)</script>");

    assertThat(result.getOutput()).contains("Token: &lt;script&gt;alert(1)&lt;/script&gt;");
  }
}
