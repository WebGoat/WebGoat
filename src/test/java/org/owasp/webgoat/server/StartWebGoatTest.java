/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StartWebGoatTest {

  @Test
  void mapsSharedServerPortToWebGoatOnly() {
    assertThat(
            StartWebGoat.normalizeServerPort(
                new String[] {
                  "--server.address=127.0.0.1", "--server.port=9000", "--webwolf.port=9091"
                }))
        .containsExactly(
            "--server.address=127.0.0.1", "--webgoat.port=9000", "--webwolf.port=9091");
  }
}
