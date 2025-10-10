/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webgoat.pages.lessons.OpenRedirectLessonPage;

public class OpenRedirectLessonUITest extends PlaywrightTest {

  private OpenRedirectLessonPage lessonPage;

  @BeforeEach
  void navigateToLesson(Browser browser) {
    var lessonName = new LessonName("OpenRedirect");
    var page = Authentication.sylvester(browser);

    this.lessonPage = new OpenRedirectLessonPage(page);
    lessonPage.resetLesson(lessonName);
    lessonPage.open(lessonName);
  }

  @Test
  @DisplayName("Complete all Open Redirect assignments via the UI")
  void shouldSolveOpenRedirectLesson() {

    // Task 1
    lessonPage.navigateTo(3);
    lessonPage.solveTask1("https://evil.example");
    assertThat(lessonPage.task1Output()).containsText("Would redirect to:");

    // Task 2
    lessonPage.navigateTo(4);
    lessonPage.solveTask2("https://webgoat.org.attacker.com");
    assertThat(lessonPage.task2Output()).containsText("Bypassed naive filter");

    // Task 3
    lessonPage.navigateTo(5);
    lessonPage.solveTask3("https://webgoat.local@evil.com", "abc123");
    assertThat(lessonPage.task3Output()).containsText("Bypassed flawed normalization");

    // Task 4 (double decode bypass)
    lessonPage.navigateTo(6);
    lessonPage.solveTask4("https://webgoat.local%2540evil.com");
    assertThat(lessonPage.task4Output()).containsText("Double decode reveals external host");

    // Quiz and mitigation
    lessonPage.navigateTo(9);
    lessonPage.solveQuiz();

    lessonPage.navigateTo(10);
    lessonPage.submitMitigation("https://attacker.integration");
    assertThat(lessonPage.mitigationOutput()).containsText("safe internal path");
  }
}
