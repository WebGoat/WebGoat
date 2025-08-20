/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class IDORIntegrationTest extends IntegrationTest {

  @BeforeEach
  public void init() {
    startLesson("IDOR");
  }

  @TestFactory
  Iterable<DynamicTest> testIDORLesson() {
    return Arrays.asList(
        dynamicTest("assignment 2 - login", this::loginIDOR),
        dynamicTest("profile", this::profile));
  }

  @AfterEach
  public void shutdown() {
    checkResults("IDOR");
  }

  private void loginIDOR() {

    Map<String, Object> params = new HashMap<>();
    params.put("username", "tom");
    params.put("password", "cat");

      checkAssignment(webGoatUrlConfig.url("IDOR/login"), params, true);
  }

  private void profile() {

    // View profile - assignment 3a
      MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(webGoatUrlConfig.url("IDOR/profile"))
            .then()
            .statusCode(200)
            .extract()
            .path("userId"),
        CoreMatchers.is("2342384"));

    // Show difference - assignment 3b
    Map<String, Object> params = new HashMap<>();
    params.put("attributes", "userId,role");
      checkAssignment(webGoatUrlConfig.url("IDOR/diff-attributes"), params, true);

    // View profile another way - assignment 4
    params.clear();
    params.put("url", "WebGoat/IDOR/profile/2342384");
      checkAssignment(webGoatUrlConfig.url("IDOR/profile/alt-path"), params, true);

    // assignment 5a
      MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(webGoatUrlConfig.url("IDOR/profile/2342388"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(true));

    // assignment 5b
      MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .contentType(ContentType.JSON) // part of the lesson
            .body(
                "{\"role\":\"1\", \"color\":\"red\", \"size\":\"large\", \"name\":\"Buffalo Bill\","
                    + " \"userId\":\"2342388\"}")
            .put(webGoatUrlConfig.url("IDOR/profile/2342388"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(true));
  }
}
