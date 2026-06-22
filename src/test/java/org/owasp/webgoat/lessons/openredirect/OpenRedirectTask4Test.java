/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class OpenRedirectTask4Test {

  private final OpenRedirectTask4 task = new OpenRedirectTask4();

  @Test
  void doubleEncodingBypassSucceeds() {
    AttackResult result = task.doubleDecode("https://webgoat.local%2540evil.com");

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getOutput()).contains("Double decode reveals external host");
    assertThat(result.getOutput()).contains("2nd host: evil.com");
  }

  @Test
  void internalHostRemainsFailedWhenSecondDecodeStaysInternal() {
    AttackResult result = task.doubleDecode("https://webgoat.local/profile");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getOutput()).contains("Bypass not achieved");
  }
}
