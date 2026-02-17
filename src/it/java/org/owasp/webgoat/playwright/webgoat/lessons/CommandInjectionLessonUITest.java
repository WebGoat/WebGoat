/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webgoat.pages.lessons.CommandInjectionLessonPage;

public class CommandInjectionLessonUITest extends PlaywrightTest {

  private CommandInjectionLessonPage lessonPage;

  @BeforeEach
  void navigateToLesson(Browser browser) {
    var lessonName = new LessonName("CommandInjection");
    var page = Authentication.sylvester(browser);

    this.lessonPage = new CommandInjectionLessonPage(page);
    lessonPage.resetLesson(lessonName);
    lessonPage.open(lessonName);
  }

  @Test
  @DisplayName("Complete the Command Injection lesson via the UI")
  void shouldSolveCommandInjectionLesson() {
    // Safety acknowledgement
    lessonPage.navigateTo(3);
    lessonPage.acknowledgeSafety();

    lessonPage.navigateTo(4);
    boolean isWindows = System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    String expectedCommand =
        isWindows ? "cmd.exe /c ping -n 1 localhost" : "/bin/sh -c ping -c 1 localhost";
    lessonPage.solveTask1(expectedCommand);
    assertThat(lessonPage.task1Output()).containsText(expectedCommand);

    lessonPage.navigateTo(5);
    lessonPage.solveTask2(isWindows);

    lessonPage.navigateTo(6);
    lessonPage.solveTask3();
    assertThat(lessonPage.task3FlagFeedback()).containsText("Flag captured");

    lessonPage.navigateTo(7);
    lessonPage.solveTask4();
    assertThat(lessonPage.task4KeyFeedback()).containsText("Great!");

    lessonPage.navigateTo(8);
    lessonPage.configureTask5();
    assertThat(lessonPage.task5Feedback()).containsText("Configuration hardened");

    // Best practices page loads
    lessonPage.navigateTo(9);
  }
}
