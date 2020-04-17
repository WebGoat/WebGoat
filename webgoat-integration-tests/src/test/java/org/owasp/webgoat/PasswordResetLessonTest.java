package org.owasp.webgoat;

import io.restassured.RestAssured;
import lombok.SneakyThrows;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Map;

public class PasswordResetLessonTest extends IntegrationTest {

	@BeforeEach
    @SneakyThrows
    public void init() {
    	startLesson("/PasswordReset");
    }
	
	@TestFactory
    Iterable<DynamicTest> testPathTraversal() {
    	return Arrays.asList(
    			dynamicTest("assignment 6 - check email link",()-> sendEmailShouldBeAvailabeInWebWolf()),
    			dynamicTest("assignment 6 - solve assignment",()-> solveAssignment()),
    			dynamicTest("assignment 2 - simple reset",()-> assignment2()),
    			dynamicTest("assignment 4 - guess questions",()-> assignment4()),
    			dynamicTest("assignment 5 - simple questions",()-> assignment5())
    			);
    }
	public void assignment2() {
		
		checkAssignment(url("PasswordReset/simple-mail/reset"), Map.of("emailReset", getWebgoatUser()+"@webgoat.org"), false);
		checkAssignment(url("PasswordReset/simple-mail"), Map.of("email", getWebgoatUser()+"@webgoat.org", "password", StringUtils.reverse(getWebgoatUser())), true);
	}
	
	public void assignment4() {
        
        checkAssignment(url("PasswordReset/questions"), Map.of("username", "tom", "securityQuestion", "purple"), true);
    }
	
	public void assignment5() {
        
        checkAssignment(url("PasswordReset/SecurityQuestions"), Map.of("question", "What is your favorite animal?"), false);
        checkAssignment(url("PasswordReset/SecurityQuestions"), Map.of("question", "What is your favorite color?"), true);
    }

	
    public void solveAssignment() {
        //WebGoat
        clickForgotEmailLink("tom@webgoat-cloud.org");

        //WebWolf
        var link = getPasswordResetLinkFromLandingPage();

        //WebGoat
        changePassword(link);
        checkAssignment(url("PasswordReset/reset/login"), Map.of("email", "tom@webgoat-cloud.org", "password", "123456"), true);
    }

    public void sendEmailShouldBeAvailabeInWebWolf() {

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
    
    @AfterEach
    public void shutdown() {
    	//this will run only once after the list of dynamic tests has run, this is to test if the lesson is marked complete
    	checkResults("/PasswordReset");
    }

    private void changePassword(String link) {
        RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .formParams("resetLink", link, "password", "123456")
                .post(url("PasswordReset/reset/change-password"))
                .then()
                .statusCode(200);
    }

    private String getPasswordResetLinkFromLandingPage() {
        var responseBody = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .get(webWolfUrl("WebWolf/requests"))
                .then()
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
