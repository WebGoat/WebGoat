/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * @author Angel Olle Blazquez
 */
class SessionManagementIT extends IntegrationTest {

  private static final String HIJACK_LOGIN_CONTEXT_PATH = "HijackSession/login";

  @Test
  void hijackSessionTest() {
    startLesson("HijackSession");

    checkAssignment(
        url(HIJACK_LOGIN_CONTEXT_PATH),
        Map.of("username", "webgoat", "password", "webgoat"),
        false);
  }
}
