/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class OpenRedirectMitigationCheckTest {

  private final OpenRedirectMitigationCheck assignment = new OpenRedirectMitigationCheck();

  @Test
  void externalUrlReportsMitigationSuccess() {
    AttackResult result = assignment.check("https://attacker.example");

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getOutput())
        .contains("Attempted external host: attacker.example blocked")
        .contains("safe internal path");
  }

  @Test
  void internalHostIsRejected() {
    AttackResult result = assignment.check("https://webgoat.local/home");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("This host is internal");
  }

  @Test
  void relativeUrlIsRejected() {
    AttackResult result = assignment.check("/relative");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("Provide an absolute external URL");
  }
}
