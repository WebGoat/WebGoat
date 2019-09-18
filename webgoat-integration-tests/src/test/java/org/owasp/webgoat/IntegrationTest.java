package org.owasp.webgoat;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import lombok.Getter;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.owasp.webwolf.WebWolf;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public abstract class IntegrationTest {

    protected static int WG_PORT = 8080;
    protected static int WW_PORT = 9090;
    private static String WEBGOAT_URL = "http://127.0.0.1:" + WG_PORT + "/WebGoat/";
    private static String WEBWOLF_URL = "http://127.0.0.1:" + WW_PORT + "/";

    //This also allows to test the application with HTTPS when outside testing option is used
    protected static RestAssuredConfig restConfig = RestAssuredConfig.newConfig().sslConfig(new SSLConfig().relaxedHTTPSValidation());

    @Getter
    private String webGoatCookie;
    @Getter
    private String webWolfCookie;
    @Getter
    private String webgoatUser = UUID.randomUUID().toString();

    private static boolean started = false;

    @BeforeClass
    public static void beforeAll() {
    	    	
        if (!started) {       
            started = true;
            if (!isAlreadyRunning(WG_PORT)) {
                SpringApplicationBuilder wgs = new SpringApplicationBuilder(StartWebGoat.class)
                        .properties(Map.of("spring.config.name", "application-webgoat,application-inttest", "WEBGOAT_PORT", WG_PORT));
                wgs.run();
           
            }
            if (!isAlreadyRunning(WW_PORT)) {
                SpringApplicationBuilder wws = new SpringApplicationBuilder(WebWolf.class)
                        .properties(Map.of("spring.config.name", "application-webwolf,application-inttest", "WEBWOLF_PORT", WW_PORT));
                wws.run();
            }
        }
    }

    private static boolean isAlreadyRunning(int port) {
        try (var ignored = new Socket("127.0.0.1", port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    protected String url(String url) {
        url = url.replaceFirst("/WebGoat/", "");
        url = url.replaceFirst("/WebGoat", "");
        url = url.startsWith("/") ? url.replaceFirst("/", "") : url;
        return WEBGOAT_URL + url;
    }

    protected String webWolfUrl(String url) {
        url = url.startsWith("/") ? url.replaceFirst("/", "") : url;
        return WEBWOLF_URL + url;
    }

    @Before
    public void login() {
        String location = given()
                .when()
                .config(restConfig)
                .formParam("username", webgoatUser)
                .formParam("password", "password")
                .post(url("login")).then()
                .cookie("JSESSIONID")
                .statusCode(302)
                .extract().header("Location");
        if (location.endsWith("?error")) {
            webGoatCookie = RestAssured.given()
                    .when()
                    .config(restConfig)
                    .formParam("username", webgoatUser)
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
                    .config(restConfig)
                    .formParam("username", webgoatUser)
                    .formParam("password", "password")
                    .post(url("login")).then()
                    .cookie("JSESSIONID")
                    .statusCode(302)
                    .extract().cookie("JSESSIONID");
        }

        webWolfCookie = RestAssured.given()
                .when()
                .config(restConfig)
                .formParam("username", webgoatUser)
                .formParam("password", "password")
                .post(WEBWOLF_URL + "login")
                .then()
                .cookie("WEBWOLFSESSION")
                .statusCode(302)
                .extract()
                .cookie("WEBWOLFSESSION");
    }

    @After
    public void logout() {
        RestAssured.given()
                .when()
                .config(restConfig)
                .get(url("logout"))
                .then()
                .statusCode(200);
    }

    /**
     * At start of a lesson. The .lesson.lesson is visited and the lesson is reset.
     *
     * @param lessonName
     */
    public void startLesson(String lessonName) {
        RestAssured.given()
                .when()
                .config(restConfig)
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url(lessonName + ".lesson.lesson"))
                .then()
                .statusCode(200);

        RestAssured.given()
                .when()
                .config(restConfig)
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("service/restartlesson.mvc"))
                .then()
                .statusCode(200);
    }

    /**
     * Helper method for most common type of test.
     * POST with parameters.
     * Checks for 200 and lessonCompleted as indicated by expectedResult
     *
     * @param url
     * @param params
     * @param expectedResult
     */
    public void checkAssignment(String url, Map<String, ?> params, boolean expectedResult) {
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .config(restConfig)
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .formParams(params)
                        .post(url)
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
    }

    /**
     * Helper method for most common type of test.
     * PUT with parameters.
     * Checks for 200 and lessonCompleted as indicated by expectedResult
     *
     * @param url
     * @param params
     * @param expectedResult
     */
    public void checkAssignmentWithPUT(String url, Map<String, ?> params, boolean expectedResult) {
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .config(restConfig)
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .formParams(params)
                        .put(url)
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
    }

    public void checkResults(String prefix) {
        Assert.assertThat(RestAssured.given()
                .when()
                .config(restConfig)
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("service/lessonoverview.mvc"))
                .then()                
                .statusCode(200).extract().jsonPath().getList("solved"), CoreMatchers.everyItem(CoreMatchers.is(true)));

        Assert.assertThat(RestAssured.given()
                .when()
                .config(restConfig)
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("service/lessonoverview.mvc"))
                .then()
                .statusCode(200).extract().jsonPath().getList("assignment.path"), CoreMatchers.everyItem(CoreMatchers.startsWith(prefix)));

    }

    public void checkAssignment(String url, ContentType contentType, String body, boolean expectedResult) {
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .config(restConfig)
                        .contentType(contentType)
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .body(body)
                        .post(url)
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
    }
    
}

