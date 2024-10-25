package org.owasp.webgoat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LabelAndHintIntegrationTest extends IntegrationTest {

  static final String ESCAPE_JSON_PATH_CHAR = "\'";

  @Test
  public void testSingleLabel() {
    Assertions.assertTrue(true);
    JsonPath jsonPath =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "en")
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(url("service/labels.mvc"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();

    Assertions.assertEquals(
        "Try again: but this time enter a value before hitting go.",
        jsonPath.getString(ESCAPE_JSON_PATH_CHAR + "http-basics.close" + ESCAPE_JSON_PATH_CHAR));

    // check if lang parameter overrules Accept-Language parameter
    jsonPath =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "en")
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(url("service/labels.mvc?lang=nl"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();
    Assertions.assertEquals(
        "Gebruikersnaam",
        jsonPath.getString(ESCAPE_JSON_PATH_CHAR + "username" + ESCAPE_JSON_PATH_CHAR));

    jsonPath =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "en")
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(url("service/labels.mvc?lang=de"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();
    Assertions.assertEquals(
        "Benutzername",
        jsonPath.getString(ESCAPE_JSON_PATH_CHAR + "username" + ESCAPE_JSON_PATH_CHAR));

    // check if invalid language returns english
    jsonPath =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "nl")
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(url("service/labels.mvc?lang=xx"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();
    Assertions.assertEquals(
        "Username", jsonPath.getString(ESCAPE_JSON_PATH_CHAR + "username" + ESCAPE_JSON_PATH_CHAR));

    // check if invalid language returns english
    jsonPath =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "xx_YY")
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(url("service/labels.mvc"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath();
    Assertions.assertEquals(
        "Username", jsonPath.getString(ESCAPE_JSON_PATH_CHAR + "username" + ESCAPE_JSON_PATH_CHAR));
  }

  @Test
  public void testHints() {
    JsonPath jsonPathLabels = getLabels("en");
    List<String> allLessons =
        List.of(
            "HttpBasics",
            "HttpProxies",
            "CIA",
            "InsecureLogin",
            "Cryptography",
            "PathTraversal",
            "XXE",
            "JWT",
            "IDOR",
            "SSRF",
            "WebWolfIntroduction",
            "CrossSiteScripting",
            "CSRF",
            "HijackSession",
            "SqlInjection",
            "SqlInjectionMitigations",
            "SqlInjectionAdvanced",
            "Challenge1");
    for (String lesson : allLessons) {
      startLesson(lesson);
      List<String> hintKeys = getHints();
      for (String key : hintKeys) {
        String keyValue =
            jsonPathLabels.getString(ESCAPE_JSON_PATH_CHAR + key + ESCAPE_JSON_PATH_CHAR);
        // System.out.println("key: " + key + " ,value: " + keyValue);
        Assertions.assertNotNull(keyValue);
        Assertions.assertNotEquals(key, keyValue);
      }
    }
    // Assertions.assertEquals("http-basics.hints.http_basics_lesson.1",
    // ""+jsonPath.getList("hint").get(0));
  }

  @Test
  public void testLabels() {

    JsonPath jsonPathLabels = getLabels("en");
    Properties propsDefault = getProperties("");
    for (String key : propsDefault.stringPropertyNames()) {
      String keyValue =
          jsonPathLabels.getString(ESCAPE_JSON_PATH_CHAR + key + ESCAPE_JSON_PATH_CHAR);
      Assertions.assertNotNull(keyValue);
    }
    checkLang(propsDefault, "nl");
    checkLang(propsDefault, "de");
    checkLang(propsDefault, "fr");
  }

  private Properties getProperties(String lang) {
    Properties prop = null;
    if (lang == null || lang.equals("")) {
      lang = "";
    } else {
      lang = "_" + lang;
    }
    try (InputStream input =
        new FileInputStream("src/main/resources/i18n/messages" + lang + ".properties")) {

      prop = new Properties();
      // load a properties file
      prop.load(input);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return prop;
  }

  private void checkLang(Properties propsDefault, String lang) {
    JsonPath jsonPath = getLabels(lang);
    Properties propsLang = getProperties(lang);

    for (String key : propsLang.stringPropertyNames()) {
      if (!propsDefault.containsKey(key)) {
        System.err.println("key: " + key + " in (" + lang + ") is missing from default properties");
        Assertions.fail();
      }
      if (!jsonPath
          .getString(ESCAPE_JSON_PATH_CHAR + key + ESCAPE_JSON_PATH_CHAR)
          .equals(propsLang.get(key))) {
        System.out.println(
            "key: " + key + " in (" + lang + ") has incorrect translation in label service");
        System.out.println(
            "actual:" + jsonPath.getString(ESCAPE_JSON_PATH_CHAR + key + ESCAPE_JSON_PATH_CHAR));
        System.out.println("expected: " + propsLang.getProperty(key));
        System.out.println();
        Assertions.fail();
      }
    }
  }

  private JsonPath getLabels(String lang) {
    return RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .header("Accept-Language", lang)
        .cookie("JSESSIONID", getWebGoatCookie())
        // .log().headers()
        .get(url("service/labels.mvc"))
        .then()
        // .log().all()
        .statusCode(200)
        .extract()
        .jsonPath();
  }

  private List<String> getHints() {
    JsonPath jsonPath =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(url("service/hint.mvc"))
            .then()
            // .log().all()
            .statusCode(200)
            .extract()
            .jsonPath();
    return jsonPath.getList("hint");
  }
}
