package org.owasp.webgoat;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Map;

public class PasswordResetLessonTest extends IntegrationTest {

    @Test
    public void solveAssignment() {
        //WebGoat
        startLesson("PasswordReset");
        clickForgotEmailLink("tom@webgoat-cloud.org");

        //WebWolf
        var link = getPasswordResetLinkFromLandingPage();

        //WebGoat
        changePassword(link);
        checkAssignment(url("PasswordReset/reset/login"), Map.of("email", "tom@webgoat-cloud.org", "password", "123456"), true);
    }

    @Test
    public void sendEmailShouldBeAvailabeInWebWolf() {
        startLesson("PasswordReset");
        clickForgotEmailLink(getWebgoatUser() + "@webgoat.org");

        var responseBody = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .get(webWolfUrl("/WebWolf/mail"))
                .then()
                .extract().response().getBody().asString();

        Assertions.assertThat(responseBody).contains("Hi, you requested a password reset link");
    }

    private void changePassword(String link) {
        RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .formParams("resetLink", link, "password", "123456")
                .post(url("PasswordReset/reset/change-password"))
                .then()
                .log().all()
                .statusCode(200);
    }

    private String getPasswordResetLinkFromLandingPage() {
        var responseBody = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .get(webWolfUrl("WebWolf/requests"))
                .then()
                .log().all()
                .extract().response().getBody().asString();
        int startIndex = responseBody.lastIndexOf("/PasswordReset/reset/reset-password/");
        var link = responseBody.substring(startIndex + "/PasswordReset/reset/reset-password/".length(), responseBody.indexOf(",", startIndex) - 1);
        return link;
    }

    private void clickForgotEmailLink(String user) {
        RestAssured.given()
                .when()
                .header("host", getWebWolfHostHeader())
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .formParams("email", user)
                .post(url("PasswordReset/ForgotPassword/create-password-reset-link"))
                .then()
                .statusCode(200);
    }
}
