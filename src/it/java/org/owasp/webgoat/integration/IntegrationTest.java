/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import java.util.Map;
import lombok.Getter;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.owasp.webgoat.ServerUrlConfig;
import org.springframework.http.HttpStatus;

public abstract class IntegrationTest {

  protected final ServerUrlConfig webGoatUrlConfig = ServerUrlConfig.webGoat();
  protected final ServerUrlConfig webWolfUrlConfig = ServerUrlConfig.webWolf();

  @Getter private String webGoatCookie;
  @Getter private String webWolfCookie;
  @Getter private final String user = "webgoat";

  @BeforeEach
  public void login() {
    login("webgoat");
  }

  protected void login(String user) {
    String location =
        given()
            .when()
            .relaxedHTTPSValidation()
            .formParam("username", user)
            .formParam("password", "password")
            .post(webGoatUrlConfig.url("login"))
            .then()
            .log()
            .ifValidationFails(LogDetail.ALL) // Log the response details if validation fails
            .cookie("JSESSIONID")
            .statusCode(302)
            .extract()
            .header("Location");
    if (location.endsWith("?error")) {
      webGoatCookie =
          RestAssured.given()
              .when()
              .relaxedHTTPSValidation()
              .formParam("username", user)
              .formParam("password", "password")
              .formParam("matchingPassword", "password")
              .formParam("agree", "agree")
              .post(webGoatUrlConfig.url("register.mvc"))
              .then()
              .cookie("JSESSIONID")
              .statusCode(302)
              .extract()
              .cookie("JSESSIONID");
    } else {
      webGoatCookie =
          given()
              .when()
              .relaxedHTTPSValidation()
              .formParam("username", user)
              .formParam("password", "password")
              .post(webGoatUrlConfig.url("login"))
              .then()
              .cookie("JSESSIONID")
              .statusCode(302)
              .extract()
              .cookie("JSESSIONID");
    }

    webWolfCookie =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .formParam("username", user)
            .formParam("password", "password")
            .post(webWolfUrlConfig.url("login"))
            .then()
            .statusCode(302)
            .cookie("WEBWOLFSESSION")
            .extract()
            .cookie("WEBWOLFSESSION");
  }

  @AfterEach
  public void logout() {
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .get(webGoatUrlConfig.url("logout"))
        .then()
        .statusCode(200);
  }

  public void startLesson(String lessonName) {
    startLesson(lessonName, false);
  }

  public void startLesson(String lessonName, boolean restart) {
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .get(webGoatUrlConfig.url(lessonName + ".lesson.lesson"))
        .then()
        .statusCode(200);

    if (restart) {
      RestAssured.given()
          .when()
          .relaxedHTTPSValidation()
          .cookie("JSESSIONID", getWebGoatCookie())
          .get(webGoatUrlConfig.url("service/restartlesson.mvc/%s.lesson".formatted(lessonName)))
          .then()
          .statusCode(200);
    }
  }

  public void checkAssignment(String url, Map<String, ?> params, boolean expectedResult) {
    MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParams(params)
            .post(url)
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(expectedResult));
  }

  public void checkAssignmentWithPUT(String url, Map<String, ?> params, boolean expectedResult) {
    MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParams(params)
            .put(url)
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(expectedResult));
  }

  public void checkResults(String lesson) {
    var result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(webGoatUrlConfig.url("service/lessonoverview.mvc/%s.lesson".formatted(lesson)))
            .andReturn();

    MatcherAssert.assertThat(
        result.then().statusCode(200).extract().jsonPath().getList("solved"),
        CoreMatchers.everyItem(CoreMatchers.is(true)));
  }

  public void checkResults() {
    var result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(webGoatUrlConfig.url("service/lessonoverview.mvc"))
            .andReturn();

    MatcherAssert.assertThat(
        result.then().statusCode(200).extract().jsonPath().getList("solved"),
        CoreMatchers.everyItem(CoreMatchers.is(true)));
  }

  public void checkAssignment(
      String url, ContentType contentType, String body, boolean expectedResult) {
    MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .contentType(contentType)
            .cookie("JSESSIONID", getWebGoatCookie())
            .body(body)
            .post(url)
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(expectedResult));
  }

  public void checkAssignmentWithGet(String url, Map<String, ?> params, boolean expectedResult) {
    MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .queryParams(params)
            .get(url)
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(expectedResult));
  }

  public String getWebWolfFileServerLocation() {
    String result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("WEBWOLFSESSION", getWebWolfCookie())
            .get(webWolfUrlConfig.url("file-server-location"))
            .then()
            .extract()
            .response()
            .getBody()
            .asString();
    result = result.replace("%20", " ");
    return result;
  }

  public String webGoatServerDirectory() {
    return RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .get(webGoatUrlConfig.url("server-directory"))
        .then()
        .extract()
        .response()
        .getBody()
        .asString();
  }

  public void cleanMailbox() {
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("WEBWOLFSESSION", getWebWolfCookie())
        .delete(webWolfUrlConfig.url("mail"))
        .then()
        .statusCode(HttpStatus.ACCEPTED.value());
  }
}
