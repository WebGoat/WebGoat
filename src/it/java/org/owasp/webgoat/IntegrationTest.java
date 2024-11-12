package org.owasp.webgoat;

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
import org.springframework.http.HttpStatus;

public abstract class IntegrationTest {

  private static String webGoatPort = System.getenv().getOrDefault("WEBGOAT_PORT", "8080");
  @Getter private static String webWolfPort = System.getenv().getOrDefault("WEBWOLF_PORT", "9090");

  @Getter
  private static String webWolfHost = System.getenv().getOrDefault("WEBWOLF_HOST", "127.0.0.1");

  private static String webGoatContext =
      System.getenv().getOrDefault("WEBGOAT_CONTEXT", "/WebGoat/");
  private static String webWolfContext =
      System.getenv().getOrDefault("WEBWOLF_CONTEXT", "/WebWolf/");

  @Getter private String webGoatCookie;
  @Getter private String webWolfCookie;
  @Getter private final String user = "webgoat";

  protected String url(String url) {
    return "http://localhost:%s%s%s".formatted(webGoatPort, webGoatContext, url);
  }

  protected class WebWolfUrlBuilder {

    private boolean attackMode = false;
    private String path = null;

    protected String build() {
      return "http://localhost:%s%s%s"
          .formatted(webWolfPort, webWolfContext, path != null ? path : "");
    }

    /**
     * In attack mode it means WebGoat calls WebWolf to perform an attack. In this case we need to
     * use port 9090 in a Docker environment.
     */
    protected WebWolfUrlBuilder attackMode() {
      attackMode = true;
      return this;
    }

    protected WebWolfUrlBuilder path(String path) {
      this.path = path;
      return this;
    }

    protected WebWolfUrlBuilder path(String path, String... uriVariables) {
      this.path = path.formatted(uriVariables);
      return this;
    }
  }

  /**
   * Debugging options: install TestContainers Desktop and map port 5005 to the host machine with
   * https://newsletter.testcontainers.com/announcements/set-fixed-ports-to-easily-debug-development-services
   *
   * <p>Start the test and connect a remote debugger in IntelliJ to localhost:5005 and attach it.
   */
  //  private static GenericContainer<?> webGoatContainer =
  //      new GenericContainer(new ImageFromDockerfile("webgoat").withFileFromPath("/",
  // Paths.get(".")))
  //          .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("webgoat")))
  //          .withExposedPorts(8080, 9090, 5005)
  //          .withEnv(
  //              "_JAVA_OPTIONS",
  //              "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005")
  //          .waitingFor(Wait.forHealthcheck());
  //
  //  static {
  //    webGoatContainer.start();
  //  }

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
            .post(url("login"))
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
              .post(url("register.mvc"))
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
              .post(url("login"))
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
            .post(new WebWolfUrlBuilder().path("login").build())
            .then()
            .statusCode(302)
            .cookie("WEBWOLFSESSION")
            .extract()
            .cookie("WEBWOLFSESSION");
  }

  @AfterEach
  public void logout() {
    RestAssured.given().when().relaxedHTTPSValidation().get(url("logout")).then().statusCode(200);
  }

  public void startLesson(String lessonName) {
    startLesson(lessonName, false);
  }

  public void startLesson(String lessonName, boolean restart) {
    RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .get(url(lessonName + ".lesson.lesson"))
        .then()
        .statusCode(200);

    if (restart) {
      RestAssured.given()
          .when()
          .relaxedHTTPSValidation()
          .cookie("JSESSIONID", getWebGoatCookie())
          .get(url("service/restartlesson.mvc/%s.lesson".formatted(lessonName)))
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
            .get(url("service/lessonoverview.mvc/%s.lesson".formatted(lesson)))
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
            .get(url("service/lessonoverview.mvc"))
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
            .get(new WebWolfUrlBuilder().path("file-server-location").build())
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
        .get(url("server-directory"))
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
        .delete(new WebWolfUrlBuilder().path("mail").build())
        .then()
        .statusCode(HttpStatus.ACCEPTED.value());
  }
}
