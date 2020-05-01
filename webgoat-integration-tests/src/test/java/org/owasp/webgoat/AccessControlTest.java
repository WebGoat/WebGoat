package org.owasp.webgoat;


import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.Data;

public class AccessControlTest extends IntegrationTest {
	
	@Test
    public void testLesson() {
    	startLesson("MissingFunctionAC");      
    	
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("hiddenMenu1", "Users");
        params.put("hiddenMenu2", "Config");
       
    	
        checkAssignment(url("/WebGoat/access-control/hidden-menu"), params, true);
        String userHash = 
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .contentType(ContentType.JSON) 
                        .get(url("/WebGoat/users"))
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .get("find { it.username == \"" + getWebgoatUser() + "\" }.userHash");
        
    	params.clear();
       	params.put("userHash", userHash);
        checkAssignment(url("/WebGoat/access-control/user-hash"), params, true);
         
  
        checkResults("/access-control");        
    }
    
	@Data
    public class Item {
        private String username;
        private boolean admin;
        private String userHash;
    }
    
}
