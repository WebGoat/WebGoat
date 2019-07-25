package org.owasp.webgoat;

import static io.restassured.RestAssured.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=WebGoat.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class WebGoatIntegrationTest extends TestHelper {

	/*
	 * Unique port for http server. Database port is not yet overruled. But this is
	 * not a problem for the integration test.
	 */
	@LocalServerPort
	int randomServerPort;
	
	private General_TestHelper generalTestHelper = new General_TestHelper();
	private SqlInjection_TestHelper sqlInjectionHelper = new SqlInjection_TestHelper();
	private SqlInjectionAdvanced_TestHelper sqlInjectionAdvancedHelper = new SqlInjectionAdvanced_TestHelper();
	private SqlInjectionMitigation_TestHelper sqlInjectionMitigationHelper = new SqlInjectionMitigation_TestHelper();
	
	String webgoatURL = System.getProperty("WEBGOAT_URL","");
	String webgoatUser = System.getProperty("WEBGOAT_USER","");
	String webgoatPassword = System.getProperty("WEBGOAT_PASSWORD","password");
	String cookie = "";

	@Before
	public void init() {
		
		/* 
		 * If no system properties are set, the test runs against the random port
		 * of the webgoat application that starts with this test.
		 * If set you can use it to test an oustide running application. If testing 
		 * against outside running applications, the tests that require WebWolf can be tested as well.
		 */
		//TODO add support for testing the lessons that require WebWolf as well.
		if (webgoatURL.equals("")) {
			webgoatURL = "http://127.0.0.1:"+randomServerPort;
		}
		
		/* 
		 * If not defined a random user will be registered and used in the test.
		 * If you run against an outside application and want to visually see the results,
		 * you can set a username.
		 */
		if (webgoatUser.equals("")) {
			webgoatUser = "tester"+Math.round(Math.random()*1000);
		}
		
		//check if user exists
		String location = given()
				.when()
				.config(restConfig)
				.formParam("username", webgoatUser)
				.formParam("password", "password")
				.post(webgoatURL+"/WebGoat/login")
				.then()
					//.log().all()
					.cookie("JSESSIONID")
					.statusCode(302)
					.extract().header("Location");
		
		//register when not existing, otherwise log in and save the cookie
		if (location.endsWith("error")) {		
		
		cookie = given()
			.when()
			.config(restConfig)
			.formParam("username", webgoatUser)
			.formParam("password", "password")
			.formParam("matchingPassword", "password")
			.formParam("agree", "agree")
			.post(webgoatURL+"/WebGoat/register.mvc")
			.then()
			.cookie("JSESSIONID")
			.statusCode(302)
			.extract()
			.cookie("JSESSIONID"); 
		
		} else {
		
		cookie = given()
				.when()
				.config(restConfig)
				.formParam("username", webgoatUser)
				.formParam("password", "password")
				.post(webgoatURL+"/WebGoat/login")
				.then()
					//.log().all()
					.cookie("JSESSIONID")
					.statusCode(302)
					.extract().cookie("JSESSIONID");
		}
	}
	
	@Test
	public void testGeneral_HttpBasics() {
		
		generalTestHelper.httpBasics(webgoatURL, cookie);
			
	}
	
	@Test
	public void testGeneral_HttpProxies() {
		
		generalTestHelper.httpProxies(webgoatURL, cookie);
			
	}
	
	@Test
	public void testGeneral_CIA() {
		
		generalTestHelper.cia(webgoatURL, cookie);
			
	}
	
	@Test
	public void testGeneral_Chrome() {
		
		generalTestHelper.chrome(webgoatURL, cookie);
			
	}
	
	@Test
	public void testSecurePassords() {
		
		generalTestHelper.securePasswords(webgoatURL, cookie);
			
	}
	
	@Test
	public void testSQLInjection() {
		
		sqlInjectionHelper.runTests(webgoatURL, cookie);
			
	}
	
	@Test
	public void testSQLInjectionAdvanced() {
		
		sqlInjectionAdvancedHelper.runTests(webgoatURL, cookie);
			
	}
	
	@Test
	public void testSQLInjectionMitigation() {
		
		sqlInjectionMitigationHelper.runTests(webgoatURL, cookie);
			
	}
	
	
}
