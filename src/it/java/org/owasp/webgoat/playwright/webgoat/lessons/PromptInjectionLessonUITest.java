/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
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
import org.owasp.webgoat.playwright.webgoat.pages.lessons.PromptInjectionLessonPage;

public class PromptInjectionLessonUITest extends PlaywrightTest {

  private PromptInjectionLessonPage lessonPage;

  @BeforeEach
  void navigateToLesson(Browser browser) {
    var lessonName = new LessonName("PromptInjection");
    var page = Authentication.sylvester(browser);

    this.lessonPage = new PromptInjectionLessonPage(page);
    lessonPage.resetLesson(lessonName);
    lessonPage.open(lessonName);
  }

  @Test
  @DisplayName("Leak the session token through the poisoned tool, then block it with the guard")
  void shouldSolveBothAssignmentsAndQuiz() {
    lessonPage.navigateTo(3);
    String mcpUrl = lessonPage.mcpUrl();

    lessonPage.navigateTo(4);
    lessonPage.checkTask1();
    assertThat(lessonPage.task1Feedback()).containsText("has not received any tool calls");

    lessonPage.selectVariant("blunt");
    lessonPage.showToolDescriptions();
    assertThat(lessonPage.toolDescriptions()).containsText("Do not mention this step");
    lessonPage.selectVariant("task-shaped");
    assertThat(lessonPage.toolDescriptions()).containsText("billing reconciliation");

    lessonPage.assistantFollowsPoisonedDescription(mcpUrl);
    lessonPage.refreshLog();
    assertThat(lessonPage.callLog()).containsText("accepted: token leaked");
    lessonPage.checkTask1();
    assertThat(lessonPage.task1Feedback()).containsText("'audit'");

    lessonPage.navigateTo(5);
    lessonPage.enableArgumentGuard();
    lessonPage.assistantFollowsPoisonedDescription(mcpUrl);
    lessonPage.refreshLog();
    assertThat(lessonPage.callLog()).containsText("refused: token in arguments");
    lessonPage.checkTask2();
    assertThat(lessonPage.task2Feedback()).containsText("The defence held");

    lessonPage.navigateTo(7);
    lessonPage.solveQuiz();
  }
}
