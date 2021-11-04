package org.owasp.webgoat;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AccessControlTest extends IntegrationTest {

    @Test
    public void testLesson() {
        startLesson("MissingFunctionAC");
        assignment1();
        assignment2();
        assignment3();

        checkResults("/access-control");
    }

    private void assignment3() {
        //direct call should fail if user has not been created
        RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .contentType(ContentType.JSON)
                .get(url("/WebGoat/access-control/users-admin-fix"))
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        //create user
        var userTemplate = """
                {"username":"%s","password":"%s","admin": "true"}
                """;
        RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .contentType(ContentType.JSON)
                .body(String.format(userTemplate, getWebgoatUser(), getWebgoatUser()))
                .post(url("/WebGoat/access-control/users"))
                .then()
                .statusCode(HttpStatus.SC_OK);

        //get the users
        var userHash =
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .contentType(ContentType.JSON)
                        .get(url("/WebGoat/access-control/users-admin-fix"))
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .get("find { it.username == \"Jerry\" }.userHash");

        checkAssignment(url("/WebGoat/access-control/user-hash-fix"), Map.of("userHash", userHash), true);
    }

    private void assignment2() {
        var userHash =
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .contentType(ContentType.JSON)
                        .get(url("/WebGoat/access-control/users"))
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .get("find { it.username == \"Jerry\" }.userHash");

        checkAssignment(url("/WebGoat/access-control/user-hash"), Map.of("userHash", userHash), true);
    }

    private void assignment1() {
        var params = Map.of("hiddenMenu1", "Users", "hiddenMenu2", "Config");
        checkAssignment(url("/WebGoat/access-control/hidden-menu"), params, true);
    }
}
