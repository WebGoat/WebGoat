package org.owasp.webgoat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.Getter;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public abstract class IntegrationTest {

    private static String webGoatPort = Objects.requireNonNull(System.getProperty("webgoatport"));
    @Getter
    private static String webWolfPort = Objects.requireNonNull(System.getProperty("webwolfport"));
    private static boolean useSSL = false;
    private static String webgoatUrl = (useSSL ? "https:" : "http:") + "//localhost:" + webGoatPort + "/WebGoat/";
    private static String webWolfUrl = (useSSL ? "https:" : "http:") + "//localhost:" + webWolfPort + "/";
    @Getter
    private String webGoatCookie;
    @Getter
    private String webWolfCookie;
    @Getter
    private String user = "webgoat";

    protected String url(String url) {
        url = url.replaceFirst("/WebGoat/", "");
        url = url.replaceFirst("/WebGoat", "");
        url = url.startsWith("/") ? url.replaceFirst("/", "") : url;
        return webgoatUrl + url;
    }

    protected String webWolfUrl(String url) {
        url = url.replaceFirst("/WebWolf/", "");
        url = url.replaceFirst("/WebWolf", "");
        url = url.startsWith("/") ? url.replaceFirst("/", "") : url;
        return webWolfUrl + url;
    }

    @BeforeEach
    public void login() {
        String location = given()
                .when()
                .relaxedHTTPSValidation()
                .formParam("username", user)
                .formParam("password", "password")
                .post(url("login")).then()
                .cookie("JSESSIONID")
                .statusCode(302)
                .extract().header("Location");
        if (location.endsWith("?error")) {
            webGoatCookie = RestAssured.given()
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
            webGoatCookie = given()
                    .when()
                    .relaxedHTTPSValidation()
                    .formParam("username", user)
                    .formParam("password", "password")
                    .post(url("login")).then()
                    .cookie("JSESSIONID")
                    .statusCode(302)
                    .extract().cookie("JSESSIONID");
        }

        webWolfCookie = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .formParam("username", user)
                .formParam("password", "password")
                .post(webWolfUrl("login"))
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
                .get(url("logout"))
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
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
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
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
    }

    //TODO is prefix useful? not every lesson endpoint needs to start with a certain prefix (they are only required to be in the same package)
    public void checkResults(String prefix) {
        checkResults();

        MatcherAssert.assertThat(RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("service/lessonoverview.mvc"))
                .then()
                .statusCode(200).extract().jsonPath().getList("assignment.path"), CoreMatchers.everyItem(CoreMatchers.startsWith(prefix)));

    }

    public void checkResults() {
        var result = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("service/lessonoverview.mvc"))
                .andReturn();

        MatcherAssert.assertThat(result.then()
                .statusCode(200).extract().jsonPath().getList("solved"), CoreMatchers.everyItem(CoreMatchers.is(true)));
    }

    public void checkAssignment(String url, ContentType contentType, String body, boolean expectedResult) {
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
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
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
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
    }

    public String getWebWolfFileServerLocation() {
        String result = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .get(webWolfUrl("/file-server-location"))
                .then()
                .extract().response().getBody().asString();
        result = result.replace("%20", " ");
        return result;
    }

    public String webGoatServerDirectory() {
        return RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("/server-directory"))
                .then()
                .extract().response().getBody().asString();
    }

}

