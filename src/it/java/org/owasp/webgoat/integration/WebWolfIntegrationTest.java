/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class WebWolfIntegrationTest extends IntegrationTest {

  @Test
  public void runTests() {
    startLesson("WebWolfIntroduction");

    Map<String, Object> params = new HashMap<>();

    String uniqueCode = StringUtils.reverse(this.getUser());
      params.put("email", this.getUser() + "@webgoat.org");
        params.put("uniqueCode", uniqueCode);

      RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .queryParams(params)
        .get(webGoatUrlConfig.url("WebWolf/landing/password-reset"))
        .then()
        .statusCode(200);
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("WEBWOLFSESSION", getWebWolfCookie())
        .queryParams(params)
        .get(webWolfUrlConfig.url("landing"))
        .then()
        .statusCode(200);
    String responseBody =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("WEBWOLFSESSION", getWebWolfCookie())
            .get(webWolfUrlConfig.url("requests"))
            .then()
            .extract()
            .response()
            .getBody()
            .asString();
    assertTrue(responseBody.contains(uniqueCode));
    params.clear();
    params.put("uniqueCode", uniqueCode);
      checkAssignment(webGoatUrlConfig.url("WebWolf/landing"), params, true);

    checkResults("WebWolfIntroduction");
  }
}
