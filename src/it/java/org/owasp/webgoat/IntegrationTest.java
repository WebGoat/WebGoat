package org.owasp.webgoat;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import java.nio.file.Paths;
import java.util.Map;
import lombok.Getter;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-webgoat.properties")
public abstract class IntegrationTest {

  @Getter
  @Value("${webwolf.port}")
  private String webWolfPort;

  @Getter
  @Value("${webwolf.host}")
  private String webWolfHost;

  @Getter private String webGoatCookie;
  @Getter private String webWolfCookie;
  @Getter private final String user = "webgoat";

  protected String url(String url) {
    return "http://localhost:%d/WebGoat/%s".formatted(webGoatContainer.getMappedPort(8080), url);
  }

  protected class WebWolfUrlBuilder {

    private boolean attackMode = false;
    private String path = null;

    protected String build() {
      return "http://localhost:%d/WebWolf/%s"
          .formatted(
              !attackMode ? webGoatContainer.getMappedPort(9090) : 9090, path != null ? path : "");
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

  private static GenericContainer<?> webGoatContainer =
      new GenericContainer(new ImageFromDockerfile().withFileFromPath("/", Paths.get(".")))
          .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("webgoat")))
          .withExposedPorts(8080, 9090, 5005)
          .withEnv(
              "_JAVA_OPTIONS",
              "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005")
          .waitingFor(Wait.forHealthcheck());

  static {
    webGoatContainer.start();
  }

  @BeforeEach
  public void login() {
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
          .get(url("service/restartlesson.mvc"))
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

  // TODO is prefix useful? not every lesson endpoint needs to start with a certain prefix (they are
  // only required to be in the same package)
  public void checkResults(String prefix) {
    checkResults();

    MatcherAssert.assertThat(
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .get(url("service/lessonoverview.mvc"))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("assignment.path"),
        CoreMatchers.everyItem(CoreMatchers.startsWith(prefix)));
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
