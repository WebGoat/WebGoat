/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
    Assertions.assertThat(lessonPage.numberOfAssignments()).isEqualTo(2);
  }

  @Test
  @Order(2)
  @DisplayName(
      "When the user enters their name, the server should reverse it then the assignment should be"
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
  @DisplayName(
      "Given Sylvester solves the first assignment when Tweety logs in then the first assignment should NOT be solved")
  void shouldNotSolvePage1(Browser browser) {
    lessonPage.navigateTo(2);
    lessonPage.getEnterYourName().fill("John Doe");
    lessonPage.getGoButton().click();

    var tweetyLessonPage = new HttpBasicsLessonPage(Authentication.tweety(browser));
    tweetyLessonPage.open(new LessonName("HttpBasics"));
    Assertions.assertThat(tweetyLessonPage.noAssignmentsCompleted()).isTrue();
  }
}
