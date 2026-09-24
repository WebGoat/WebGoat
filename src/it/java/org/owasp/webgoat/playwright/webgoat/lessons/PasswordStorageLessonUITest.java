/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webgoat.pages.lessons.PasswordStorageLessonPage;

public class PasswordStorageLessonUITest extends PlaywrightTest {

  private PasswordStorageLessonPage lessonPage;

  @BeforeEach
  void navigateToLesson(Browser browser) {
    var lessonName = new LessonName("PasswordStorage");
    var page = Authentication.sylvester(browser);

    lessonPage = new PasswordStorageLessonPage(page);
    lessonPage.resetLesson(lessonName);
    lessonPage.open(lessonName);
  }

  @Test
  @DisplayName("Complete the password storage progression through the browser")
  void shouldCompleteAllPasswordStorageStages() {
    assertThat(lessonPage.title()).hasText("Password Storage");
    assertThat(lessonPage.numberOfAssignments()).isEqualTo(5);

    lessonPage.navigateTo(2);
    lessonPage.runStage1Wordlist();
    assertThat(lessonPage.stage1ToolOutput()).containsText("Match found after 4 candidates");
    lessonPage.submitStage1Password(
        lessonPage.passwordFromTool(lessonPage.stage1ToolOutput()));
    assertThat(lessonPage.stage1Feedback()).containsText("Fast, unsalted SHA-256");

    lessonPage.navigateTo(3);
    lessonPage.runStage2Wordlist();
    assertThat(lessonPage.stage2ToolOutput()).containsText("Public salt used");
    lessonPage.submitStage2(
        lessonPage.passwordFromTool(lessonPage.stage2ToolOutput()));
    assertThat(lessonPage.stage2Feedback()).containsText("Unique salts hide password equality");

    lessonPage.navigateTo(4);
    assertThat(lessonPage.stage3Dump()).containsText("$argon2id$v=19$m=19456,t=2,p=1$");
    lessonPage.submitStage3Parameters();
    assertThat(lessonPage.stage3Feedback()).containsText("Argon2id makes every candidate");

    lessonPage.navigateTo(5);
    lessonPage.submitStage4Explanation();
    assertThat(lessonPage.stage4Feedback()).containsText("database lacks the pepper");

    lessonPage.navigateTo(6);
    lessonPage.retrieveConfiguration();
    assertThat(lessonPage.configurationOutput()).containsText("blog.password.pepper=");
    lessonPage.runStage5Attack(lessonPage.pepperFromConfiguration());
    assertThat(lessonPage.stage5ToolOutput()).containsText("Match found after 4 candidates");
    lessonPage.submitStage5Password(
        lessonPage.passwordFromTool(lessonPage.stage5ToolOutput()));
    assertThat(lessonPage.stage5Feedback()).containsText("removed the pepper's protection");
  }
}
