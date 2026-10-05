/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import java.util.Map;
import org.junit.jupiter.api.Test;

class SessionManagementIT extends IntegrationTest {

  private static final String HIJACK_LOGIN_CONTEXT_PATH = "HijackSession/login";

  @Test
  void hijackSessionTest() {
    startLesson("HijackSession");

      checkAssignment(
              webGoatUrlConfig.url(HIJACK_LOGIN_CONTEXT_PATH),
        Map.of("username", "webgoat", "password", "webgoat"),
        false);
  }
}
