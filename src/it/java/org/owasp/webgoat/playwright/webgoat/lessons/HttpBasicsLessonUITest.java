/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.owasp.webgoat.ServerUrlConfig;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webgoat.pages.lessons.HttpBasicsLessonPage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpBasicsLessonUITest extends PlaywrightTest {

    private HttpBasicsLessonPage lessonPage;

    @BeforeEach
    void navigateToLesson(Browser browser) {
        var lessonName = new LessonName("HttpBasics");
        var page = Authentication.sylvester(browser);

        this.lessonPage = new HttpBasicsLessonPage(page);
        lessonPage.resetLesson(lessonName);
        lessonPage.open(lessonName);
    }

    @Test
    @Order(1)
    void shouldShowDefaultPage() {
        assertThat(lessonPage.getTitle()).hasText("HTTP Basics");
        Assertions.assertThat(lessonPage.noAssignmentsCompleted()).isTrue();
        Assertions.assertThat(lessonPage.numberOfAssignments()).isEqualTo(4);
    }

    @Test
    @Order(2)
    @DisplayName("When the user enters their name, the server should reverse it then the assignment should be"
            + " solved")
    void solvePage2() {
        lessonPage.navigateTo(2);
        lessonPage.getEnterYourName().fill("John Doe");
        lessonPage.getGoButton().click();

        assertThat(lessonPage.getAssignmentOutput())
                .containsText("The server has reversed your name: eoD nhoJ");
        Assertions.assertThat(lessonPage.isAssignmentSolved(2)).isTrue();
    }

    @Test
    @Order(3)
    @DisplayName("When the user enters nothing then the server should display an error message")
    void invalidPage2() {
        lessonPage.navigateTo(2);
        lessonPage.getEnterYourName().fill("");
        lessonPage.getGoButton().click();

        assertThat(lessonPage.getAssignmentOutput()).containsText("Try again, name cannot be empty.");
    }

    @Test
    @Order(4)
    @DisplayName("When the user sends the right request, they should be given a secret code. When the code is entered the lesson should be completed")
    void solvePage3() {
        var external_endpoint = lessonPage.requestToExternal("{\"external\":true}", "application/json", "attacker");
        assertEquals(external_endpoint.ok(), true);
        var code = external_endpoint.text()
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .replace("secret_code", "")
                .replace(": ", "")
                .strip();

        lessonPage.navigateTo(3);
        lessonPage.getCode().fill(code);
        lessonPage.getSubmitButton().click();

        assertThat(lessonPage.getAssignmentOutput())
                .containsText("Congratulations. You have successfully completed the assignment.");
    }

    @Test
    @Order(5)
    @DisplayName("When the user enters nothing then the server should display an error message")
    void invalidPage3() {
        lessonPage.navigateTo(3);
        lessonPage.getCode().fill("");
        lessonPage.getSubmitButton().click();

        assertThat(lessonPage.getAssignmentOutput())
                .containsText("You need to send the right request to /HttpBasics/external to get the secret!");

        var options = RequestOptions.create().setData("{\"external\":true}")
                .setHeader("Content-Type", "Application/Json")
                .setHeader("User-Agent", "Attacker");
        lessonPage.getPage().request().put(ServerUrlConfig.webGoat().url("HttpBasics/external"),
                options);
        lessonPage.getCode().fill("");
        lessonPage.getSubmitButton().click();

        assertThat(lessonPage.getAssignmentOutput())
                .containsText("Sorry the solution is not correct, please try again.");
    }

    @Test
    @Order(6)
    @DisplayName("When the user makes requests to the external endpoint they should be given an error if the request is not correct")
    void invalidPage3Request() {
        assertNotEquals(lessonPage.requestToExternal(null, null, null).text().contains("status"), 200);
        assertNotEquals(lessonPage.requestToExternal("bad", "bad", "bad").status(), 200);
        assertNotEquals(lessonPage.requestToExternal(null, "bad", null).status(), 200);
        assertNotEquals(lessonPage.requestToExternal("{\"external\":false}", "appliaction/json", "attacker").status(),
                200);
        assertNotEquals(
                lessonPage.requestToExternal("{\"external\":true}", "text/plain", "Firefox").status(), 200);
        assertEquals(lessonPage.requestToExternal("{\"external\":true}", "application/json", "attacker").status(), 200);
        assertEquals(lessonPage.requestToExternal("{\"external\":true}", "application/json", "aTTACker").status(), 200);

    }

    @Test
    @Order(7)
    @DisplayName("When the user submits an invalid quiz there should be an error saying which ones are wrong")
    void invalidQuiz() {
        lessonPage.navigateTo(6);
        lessonPage.solveQuiz(false);
        lessonPage.getPage().waitForSelector("#q_container .quiz_question.incorrect:nth-of-type(7)");
        assertThat(lessonPage.getAssignmentOutput())
                .containsText("Sorry the solution is not correct, please try again.");
    }

    @Test
    @Order(8)
    @DisplayName("When a user solves the quiz successfully they should be notified.")
    void solveQuiz() {
        lessonPage.navigateTo(6);
        lessonPage.solveQuiz(true);
        lessonPage.getPage().waitForSelector("#q_container .quiz_question.correct:nth-of-type(7)");
        assertThat(lessonPage.getAssignmentOutput())
                .containsText("Congratulations. You have successfully completed the assignment.");
    }

    @Test
    @Order(9)
    @DisplayName("Given Sylvester solves the first assignment when Tweety logs in then the first assignment should NOT be solved")
    void shouldNotSolvePage1(Browser browser) {
        lessonPage.navigateTo(2);
        lessonPage.getEnterYourName().fill("John Doe");
        lessonPage.getGoButton().click();

        var tweetyLessonPage = new HttpBasicsLessonPage(Authentication.tweety(browser));
        tweetyLessonPage.open(new LessonName("HttpBasics"));
        Assertions.assertThat(tweetyLessonPage.noAssignmentsCompleted()).isTrue();
    }
}
