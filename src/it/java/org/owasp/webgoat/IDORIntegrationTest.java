package org.owasp.webgoat;


import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;

public class IDORIntegrationTest extends IntegrationTest {
	
	@BeforeEach
    @SneakyThrows
    public void init() {
    	startLesson("IDOR");        
    }

    @TestFactory
    Iterable<DynamicTest> testIDORLesson() {
    	return Arrays.asList(
    			dynamicTest("login",()-> loginIDOR()),
    			dynamicTest("profile", () -> profile())
    			);
    }
	
    @AfterEach
    public void shutdown() throws IOException {
        checkResults("/IDOR");        
    }
    
    private void loginIDOR() throws IOException {
    	
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("username", "tom");
        params.put("password", "cat");
       
    	
        checkAssignment(url("/WebGoat/IDOR/login"), params, true);
        	
    }
    
    private void profile() {
    	MatcherAssert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .get(url("/WebGoat/IDOR/profile"))
                        .then()
                        .statusCode(200)
                        .extract().path("userId"), CoreMatchers.is("2342384"));
    	Map<String, Object> params = new HashMap<>();
    	params.clear();
        params.put("attributes", "userId,role");
        checkAssignment(url("/WebGoat/IDOR/diff-attributes"), params, true);
        params.clear();
        params.put("url", "WebGoat/IDOR/profile/2342384");
        checkAssignment(url("/WebGoat/IDOR/profile/alt-path"), params, true);
        
        MatcherAssert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .get(url("/WebGoat/IDOR/profile/2342388"))
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));
        
        MatcherAssert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())    
                        .contentType(ContentType.JSON) //part of the lesson
                        .body("{\"role\":\"1\", \"color\":\"red\", \"size\":\"large\", \"name\":\"Buffalo Bill\", \"userId\":\"2342388\"}")
                        .put(url("/WebGoat/IDOR/profile/2342388"))
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));       
        
        
    }
    
}
