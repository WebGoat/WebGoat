package org.owasp.webgoat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import static org.junit.Assert.assertThat;

import java.util.Map;

import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;

public class TestHelper {
	
	//This also allows to test the application with HTTPS when outside testing option is used
	protected RestAssuredConfig restConfig = RestAssuredConfig.newConfig().sslConfig(new SSLConfig().relaxedHTTPSValidation());

	/**
	 * At start of a lesson. The .lesson.lesson is visited and the lesson is reset.
	 * @param cookie
	 * @param url
	 * @param lessonName
	 */
	public void startLesson(String cookie, String url, String lessonName) {
		given()
		.when()
		.config(restConfig)
		.cookie("JSESSIONID", cookie)
		.get(url+"/WebGoat/"+lessonName+".lesson.lesson")
		.then()
		.statusCode(200);
		
		given()
		.when()
		.config(restConfig)
		.cookie("JSESSIONID", cookie)
		.get(url+"/WebGoat/service/restartlesson.mvc")
		.then()
		.statusCode(200);
	}
	
	/**
	 * Helper method for most common type of test.
	 * POST with parameters.
	 * Checks for 200 and lessonCompleted as indicated by expectedResult
	 * @param webgoatCookie
	 * @param url
	 * @param params
	 * @param expectedResult
	 */
	public void checkAssignment(String webgoatCookie, String url, Map<String, ?> params, boolean expectedResult) {
		assertThat(
			given()
				.when()
					.config(restConfig)
					.cookie("JSESSIONID", webgoatCookie)
					.formParams(params)
				.post(url)
				.then()
					//.log().all()
					.statusCode(200)
					.extract().path("lessonCompleted"), is(expectedResult));
	}
	
	/**
	 * Helper method at the end of a lesson.
	 * Check if all path paramters are correct for the progress.
	 * Check if all are solved.
	 * @param webgoatCookie
	 * @param webgoatURL
	 * @param prefix
	 */
	public void checkResults(String webgoatCookie, String webgoatURL, String prefix) {
		assertThat(given()
				.when()
				.config(restConfig)
				.cookie("JSESSIONID", webgoatCookie)
				.get(webgoatURL+"/WebGoat/service/lessonoverview.mvc")
				.then()
				//.log().all()
				.statusCode(200).extract().jsonPath().getList("solved"),everyItem(is(true)));

		assertThat(given()
				.when()
				.config(restConfig)
				.cookie("JSESSIONID", webgoatCookie)
				.get(webgoatURL+"/WebGoat/service/lessonoverview.mvc")
				.then()
				//.log().all()
				.statusCode(200).extract().jsonPath().getList("assignment.path"),everyItem(startsWith(prefix)));

	}
}
