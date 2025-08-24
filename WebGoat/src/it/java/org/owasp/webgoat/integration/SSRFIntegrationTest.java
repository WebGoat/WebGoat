/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SSRFIntegrationTest extends IntegrationTest {

  @Test
  public void runTests() {
    startLesson("SSRF");

    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("url", "images/jerry.png");

      checkAssignment(webGoatUrlConfig.url("SSRF/task1"), params, true);
    params.clear();
    params.put("url", "http://ifconfig.pro");

      checkAssignment(webGoatUrlConfig.url("SSRF/task2"), params, true);

    checkResults("SSRF");
  }
}
