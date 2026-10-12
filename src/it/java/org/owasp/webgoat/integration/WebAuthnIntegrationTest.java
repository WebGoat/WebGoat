/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.webauthn.VirtualAuthenticator;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport;

public class WebAuthnIntegrationTest extends IntegrationTest {

  @Test
  public void runTests() {
    startLesson("WebAuthn", true);

    // Registration: bind a new key to Jerry's account by changing the user handle
    Map<String, Object> registrationOptions = post("WebAuthn/registration/options", Map.of());
    Map<String, Object> credential = post("WebAuthn/authenticator/create", registrationOptions);
    String jerrysUserHandle =
        WebAuthnSupport.base64Url("jerry".getBytes(StandardCharsets.UTF_8));
    checkAssignment(
        webGoatUrlConfig.url("WebAuthn/registration/finish"),
        ContentType.JSON,
        json(Map.of("userHandle", jerrysUserHandle, "credential", credential)),
        true);

    // Authentication: replay Jerry's captured login, the challenge is never checked
    String capturedLogin =
        given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(webGoatUrlConfig.url("WebAuthn/support-ticket"))
            .then()
            .statusCode(200)
            .extract()
            .asString();
    post("WebAuthn/authentication/options", Map.of("username", "jerry"));
    checkAssignment(
        webGoatUrlConfig.url("WebAuthn/authentication/finish"),
        ContentType.JSON,
        capturedLogin,
        true);

    // Quiz
    checkAssignment(
        webGoatUrlConfig.url("WebAuthn/quiz"),
        Map.of(
            "question_0_solution", "Solution 2",
            "question_1_solution", "Solution 2",
            "question_2_solution", "Solution 2",
            "question_3_solution", "Solution 2",
            "question_4_solution", "Solution 1"),
        true);

    // Bonus: a software security key against the secure implementation
    URI base = URI.create(webGoatUrlConfig.getBaseUrl());
    String origin = webGoatUrlConfig.getBaseUrl();
    String rpId = base.getHost();
    var securityKey = new VirtualAuthenticator();
    Map<String, Object> skOptions = post("WebAuthn/securitykey/registration/options", Map.of());
    var skCredential =
        securityKey.create(
            origin,
            rpId,
            (String) skOptions.get("challenge"),
            WebAuthnSupport.fromBase64Url((String) ((Map<?, ?>) skOptions.get("user")).get("id")));
    assertThat(
        post("WebAuthn/securitykey/registration/finish", skCredential).get("registered"),
        is(true));

    Map<String, Object> loginOptions = post("WebAuthn/securitykey/authentication/options", Map.of());
    List<String> allowed =
        ((List<?>) loginOptions.get("allowCredentials"))
            .stream().map(d -> (String) ((Map<?, ?>) d).get("id")).toList();
    var assertion =
        securityKey.get(origin, rpId, (String) loginOptions.get("challenge"), allowed);
    checkAssignment(
        webGoatUrlConfig.url("WebAuthn/securitykey/authentication/finish"),
        ContentType.JSON,
        json(assertion),
        true);

    // replaying the same assertion against the secure implementation fails
    post("WebAuthn/securitykey/authentication/options", Map.of());
    postAssignment("WebAuthn/securitykey/authentication/finish", json(assertion))
        .body("lessonCompleted", is(false))
        .body("feedback", containsString("challenge mismatch"));

    checkResults("WebAuthn");
  }

  private ValidatableResponse postAssignment(String path, String body) {
    return given()
        .when()
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .cookie("JSESSIONID", getWebGoatCookie())
        .body(body)
        .post(webGoatUrlConfig.url(path))
        .then()
        .statusCode(200);
  }

  private Map<String, Object> post(String path, Object body) {
    return given()
        .when()
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .cookie("JSESSIONID", getWebGoatCookie())
        .body(json(body))
        .post(webGoatUrlConfig.url(path))
        .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getMap("$");
  }

  private static String json(Object value) {
    return WebAuthnSupport.toJson(value);
  }
}
