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
	
   public class test {
	private String AWS_SECRET_KEY = "TestKeyPassPwasdlfsdfasdfd";
	private String AWS_ACCESS_KEY = "askdfhlksfdjadslkfjsfklj1232l3klsdkfjdsf";
	private String DSA_PRIVATE_KEY = "My Private Key is here";
	private String gpg_key = "8sa7d8sa7d0sa7dsa7d98sad7sa6d7sa6d98sads8a7d0sad7sa87d89sa7d98sa7d98sa7d8sad";
	private String git_token = "3cb43625bb5a9a7f58498517asdsadas7sa7d687a5sd78sa6d";
	private String ssh_key = "Y2QwMWU0ZWUtOWZkMy00YjIxLThkMmYtMmVkYWFhZTBhMDI2OjEwZmYxMTQ3LWY4ZTAtNDU3ZC05ZGRlLWZhMWMzN2E1MWRjMA==";
	private String AKC_TOKEN = "sakdfjlsdf";
   }
    
}
