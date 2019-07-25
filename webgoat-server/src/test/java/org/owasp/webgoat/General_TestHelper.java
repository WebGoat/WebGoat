package org.owasp.webgoat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import io.restassured.http.ContentType;


public class General_TestHelper extends TestHelper {

	
	public void httpBasics(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "HttpBasics");
		Map<String, Object> params = new HashMap<>();
		params.clear();
		params.put("person", "goatuser");		
		checkAssignment(cookie, webgoatURL+"/WebGoat/HttpBasics/attack1", params, true);
	
		params.clear();
		params.put("answer", "POST");	
		params.put("magic_answer", "33");
		params.put("magic_num", "4");
		checkAssignment(cookie, webgoatURL+"/WebGoat/HttpBasics/attack2", params, false);
	
		params.clear();
		params.put("answer", "POST");	
		params.put("magic_answer", "33");
		params.put("magic_num", "33");
		checkAssignment(cookie, webgoatURL+"/WebGoat/HttpBasics/attack2", params, true);
	
		checkResults(cookie, webgoatURL, "/HttpBasics/");
	
	}
	
	public void httpProxies(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "HttpProxies");
		assertThat(given()
				.when().config(restConfig).cookie("JSESSIONID", cookie).header("x-request-intercepted", "true")
				.contentType(ContentType.JSON).log().all()
				.get(webgoatURL + "/WebGoat/HttpProxies/intercept-request?changeMe=Requests are tampered easily").then()
				.log().all().statusCode(200).extract().path("lessonCompleted"), is(true));

		checkResults(cookie, webgoatURL, "/HttpProxies/");

	}
	
	public void cia(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "CIA");
		Map<String, Object> params = new HashMap<>();
		params.clear();
		params.put("question_0_solution", "Solution 3: By stealing a database where names and emails are stored and uploading it to a website.");	
		params.put("question_1_solution", "Solution 1: By changing the names and emails of one or more users stored in a database.");
		params.put("question_2_solution", "Solution 4: By launching a denial of service attack on the servers.");
		params.put("question_3_solution", "Solution 2: The systems security is compromised even if only one goal is harmed.");
		checkAssignment(cookie, webgoatURL+"/WebGoat/cia/quiz", params, true);
		checkResults(cookie, webgoatURL, "/cia/");
	
	}
	
	public void securePasswords(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "SecurePasswords");
		Map<String, Object> params = new HashMap<>();
		params.clear();
		params.put("password", "ajnaeliclm^&&@kjn.");	
		checkAssignment(cookie, webgoatURL+"/WebGoat/SecurePasswords/assignment", params, true);
		checkResults(cookie, webgoatURL, "SecurePasswords/");
	
		startLesson(cookie, webgoatURL, "AuthBypass");
		params.clear();
		params.put("secQuestion2", "John");
		params.put("secQuestion3", "Main");
		params.put("jsEnabled", "1");
		params.put("verifyMethod", "SEC_QUESTIONS");
		params.put("userId", "12309746");
		checkAssignment(cookie, webgoatURL + "/WebGoat/auth-bypass/verify-account", params, true);
		checkResults(cookie, webgoatURL, "/auth-bypass/");

		startLesson(cookie, webgoatURL, "HttpProxies");
		assertThat(given().when().config(restConfig).cookie("JSESSIONID", cookie).header("x-request-intercepted", "true")
				.contentType(ContentType.JSON).log().all()
				.get(webgoatURL + "/WebGoat/HttpProxies/intercept-request?changeMe=Requests are tampered easily").then()
				.log().all().statusCode(200).extract().path("lessonCompleted"), is(true));
		checkResults(cookie, webgoatURL, "/HttpProxies/");
		
	}
	
	public void chrome(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "ChromeDevTools");
		
		Map<String, Object> params = new HashMap<>();
		params.clear();
		params.put("param1", "42");
		params.put("param2", "24");
		
		String result =
				given()
					.when()
						.config(restConfig)
						.cookie("JSESSIONID", cookie)
						.header("webgoat-requested-by","dom-xss-vuln")
						.header("X-Requested-With", "XMLHttpRequest")
						.formParams(params)
					.post(webgoatURL+"/WebGoat/CrossSiteScripting/phone-home-xss")
					.then()
						//.log().all()
						.statusCode(200)
						.extract().path("output");
		String secretNumber = result.substring("phoneHome Response is ".length());
		
		params.clear();
		params.put("successMessage", secretNumber);	
		checkAssignment(cookie, webgoatURL+"/WebGoat/ChromeDevTools/dummy", params, true);

		params.clear();
		params.put("number", "24");
		params.put("network_num", "24");	
		checkAssignment(cookie, webgoatURL+"/WebGoat/ChromeDevTools/network", params, true);

		checkResults(cookie, webgoatURL, "/ChromeDevTools/");
	
	}
	
	
}
