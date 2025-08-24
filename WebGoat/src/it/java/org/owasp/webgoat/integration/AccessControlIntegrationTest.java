/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

class AccessControlIntegrationTest extends IntegrationTest {

  @Test
  void testLesson() {
    startLesson("MissingFunctionAC", true);
    assignment1();
    assignment2();
    assignment3();

    checkResults("MissingFunctionAC");
  }

  private void assignment3() {
    // direct call should fail if user has not been created
      RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .contentType(ContentType.JSON)
        .get(webGoatUrlConfig.url("access-control/users-admin-fix"))
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN);

    // create user
    var userTemplate =
        """
        {"username":"%s","password":"%s","admin": "true"}
        """;
      RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .contentType(ContentType.JSON)
        .body(String.format(userTemplate, this.getUser(), this.getUser()))
        .post(webGoatUrlConfig.url("access-control/users"))
        .then()
        .statusCode(HttpStatus.SC_OK);

    // get the users
      var userHash =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .contentType(ContentType.JSON)
            .get(webGoatUrlConfig.url("access-control/users-admin-fix"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .get("find { it.username == \"Jerry\" }.userHash");

      checkAssignment(webGoatUrlConfig.url("access-control/user-hash-fix"), Map.of("userHash", userHash), true);
  }

  private void assignment2() {
      var userHash =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .contentType(ContentType.JSON)
            .get(webGoatUrlConfig.url("access-control/users"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .get("find { it.username == \"Jerry\" }.userHash");

      checkAssignment(webGoatUrlConfig.url("access-control/user-hash"), Map.of("userHash", userHash), true);
  }

  private void assignment1() {
    var params = Map.of("hiddenMenu1", "Users", "hiddenMenu2", "Config");
      checkAssignment(webGoatUrlConfig.url("access-control/hidden-menu"), params, true);
  }
}
