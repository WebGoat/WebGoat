/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class MultiUserProgressIntegrationTest extends IntegrationTest {

  @Test
  void twoUsersCanSolveTheSameCsrfAssignment() {
    String runId = UUID.randomUUID().toString().substring(0, 8);

    for (String username : List.of("progress-" + runId + "-a", "progress-" + runId + "-b")) {
      login(username);
      startLesson("CSRF");

      boolean solved =
          RestAssured.given()
              .relaxedHTTPSValidation()
              .cookie("JSESSIONID", getWebGoatCookie())
              .header("Referer", "https://example.invalid/form")
              .contentType(ContentType.TEXT)
              .body("{}")
              .post(webGoatUrlConfig.url("csrf/feedback/message"))
              .then()
              .statusCode(200)
              .extract()
              .path("lessonCompleted");

      assertThat(solved).isTrue();
    }
  }
}
