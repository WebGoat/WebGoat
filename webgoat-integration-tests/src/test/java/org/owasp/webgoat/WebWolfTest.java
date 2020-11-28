package org.owasp.webgoat;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class WebWolfTest extends IntegrationTest {
    
    @Test
    public void runTests() throws IOException {
        startLesson("WebWolfIntroduction");
        
        //Assignment 3
        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("email", getWebgoatUser()+"@webgoat.org");
        checkAssignment(url("/WebGoat/WebWolf/mail/send"), params, false);
        
        String responseBody = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .get(webWolfUrl("/WebWolf/mail"))
                .then()
                .extract().response().getBody().asString();
        
        String uniqueCode = responseBody.replace("%20", " ");
        uniqueCode = uniqueCode.substring(21+uniqueCode.lastIndexOf("your unique code is: "),uniqueCode.lastIndexOf("your unique code is: ")+(21+getWebgoatUser().length()));
        params.clear();
        params.put("uniqueCode", uniqueCode);
        checkAssignment(url("/WebGoat/WebWolf/mail"), params, true);
        
        //Assignment 4
        RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())   
                        .queryParams(params)
                        .get(url("/WebGoat/WebWolf/landing/password-reset"))                        
                        .then()
                        .statusCode(200);
        RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("WEBWOLFSESSION", getWebWolfCookie())
        .queryParams(params)
        .get(webWolfUrl("/landing"))                        
        .then()
        .statusCode(200);
        responseBody = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .get(webWolfUrl("/WebWolf/requests"))
                .then()
                .extract().response().getBody().asString();
        assertTrue(responseBody.contains(uniqueCode));
        params.clear();
        params.put("uniqueCode", uniqueCode);
        checkAssignment(url("/WebGoat/WebWolf/landing"), params, true);
        
        checkResults("/WebWolf");
        
    }
    
}
