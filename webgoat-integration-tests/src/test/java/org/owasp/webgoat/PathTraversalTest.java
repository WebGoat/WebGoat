package org.owasp.webgoat;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.security.core.token.Sha512DigestUtils;

import io.restassured.RestAssured;
import lombok.SneakyThrows;

public class PathTraversalTest extends IntegrationTest {
        
	//the JUnit5 way
    @TempDir
    Path tempDir;
    
    private File fileToUpload = null;
    
    @BeforeEach
    @SneakyThrows
    public void init() {
    	fileToUpload = Files.createFile(
                tempDir.resolve("test.jpg")).toFile();
    	Files.write(fileToUpload.toPath(), "This is a test" .getBytes());
    	startLesson("PathTraversal");
    }

    @TestFactory
    Iterable<DynamicTest> testPathTraversal() {
    	return Arrays.asList(
    			dynamicTest("assignment 1 - profile upload",()-> assignment1()),
    			dynamicTest("assignment 2 - profile upload fix",()-> assignment2()),
    			dynamicTest("assignment 3 - profile upload remove user input",()-> assignment3()),
    			dynamicTest("assignment 4 - profile upload random pic",()-> assignment4())
    			);
    }
    
    public void assignment1() throws IOException {
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .multiPart("uploadedFile", "test.jpg", Files.readAllBytes(fileToUpload.toPath()))
                        .param("fullName", "../John Doe")
                        .post("/WebGoat/PathTraversal/profile-upload")
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));
    }

    public void assignment2() throws IOException {
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .multiPart("uploadedFileFix", "test.jpg", Files.readAllBytes(fileToUpload.toPath()))
                        .param("fullNameFix", "..././John Doe")
                        .post("/WebGoat/PathTraversal/profile-upload-fix")
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));
    }

    public void assignment3() throws IOException {
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .multiPart("uploadedFileRemoveUserInput", "../test.jpg", Files.readAllBytes(fileToUpload.toPath()))
                        .post("/WebGoat/PathTraversal/profile-upload-remove-user-input")
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));
    }
    public void assignment4() throws IOException {
        var uri = "/WebGoat/PathTraversal/random-picture?id=%2E%2E%2F%2E%2E%2Fpath-traversal-secret";
        RestAssured.given().urlEncodingEnabled(false)
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(uri)
                .then()
                .statusCode(200)
                .content(CoreMatchers.is("You found it submit the SHA-512 hash of your username as answer"));

        checkAssignment("/WebGoat/PathTraversal/random", Map.of("secret", Sha512DigestUtils.shaHex(getWebgoatUser())), true);
    }
    
    @AfterEach
    public void shutdown() {
    	//this will run only once after the list of dynamic tests has run, this is to test if the lesson is marked complete
    	checkResults("/PathTraversal");
    }
}
