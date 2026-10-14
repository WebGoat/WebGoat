/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.path.json.JsonPath;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class OpenRedirectIntegrationTest extends IntegrationTest {

  @Test
  public void runTests() {
    startLesson("OpenRedirect");

    Map<String, Object> params = new HashMap<>();

    // Task 1: basic external URL
    params.put("url", "https://evil.test");
    checkAssignment(webGoatUrlConfig.url("OpenRedirect/task1"), params, true);

    // Task 2: naive substring filter bypass
    params.clear();
    params.put("url", "https://webgoat.org.evil.com");
    checkAssignment(webGoatUrlConfig.url("OpenRedirect/task2"), params, true);

    // Task 3: userinfo based host confusion
    params.clear();
    params.put("target", "https://webgoat.local@evil.com");
    params.put("token", "abc123");
    checkAssignment(webGoatUrlConfig.url("OpenRedirect/task3"), params, true);

    // Task 4: double-encoding bypass
    params.clear();
    params.put("target", "https://webgoat.local%2540evil.com");
    JsonPath task4Response =
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParams(params)
            .post(webGoatUrlConfig.url("OpenRedirect/task4"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();

    assertThat(task4Response.getBoolean("lessonCompleted"), is(true));
    assertThat(
        task4Response.getString("output"), containsString("Double decode reveals external host"));
    assertThat(task4Response.getString("output"), containsString("2nd host: evil.com"));

    // Quiz completion with correct solutions
    JsonPath quizResponse =
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("question_0_solution", "Solution 0")
            .formParam("question_1_solution", "Solution 2")
            .formParam("question_2_solution", "Solution 0")
            .formParam("question_3_solution", "Solution 0")
            .post(webGoatUrlConfig.url("OpenRedirect/quiz"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();

    assertThat(quizResponse.getBoolean("lessonCompleted"), is(true));

    // Mitigation check requires external absolute URL
    params.clear();
    params.put("url", "https://attacker.integration");
    JsonPath mitigationResponse =
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParams(params)
            .post(webGoatUrlConfig.url("OpenRedirect/mitigation"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();

    assertThat(mitigationResponse.getBoolean("lessonCompleted"), is(true));
    assertThat(mitigationResponse.getString("output"), containsString("safe internal path"));

    checkResults("OpenRedirect");
  }
}
