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
import org.owasp.webgoat.playwright.webgoat.pages.lessons.TimingAttacksLessonPage;

public class TimingAttacksLessonUITest extends PlaywrightTest {

  private TimingAttacksLessonPage lessonPage;

  @BeforeEach
  void navigateToLesson(Browser browser) {
    var lessonName = new LessonName("TimingAttacks");
    var page = Authentication.sylvester(browser);

    lessonPage = new TimingAttacksLessonPage(page);
    lessonPage.resetLesson(lessonName);
    lessonPage.open(lessonName);
  }

  @Test
  @DisplayName("Discover and submit the first HMAC byte through response timing")
  void shouldRecoverTheFirstByte() {
    assertThat(lessonPage.title()).hasText("Timing Attacks");
    assertThat(lessonPage.numberOfAssignments()).isEqualTo(4);

    lessonPage.navigateTo(2);
    String firstByte = lessonPage.recoverFirstByte();
    lessonPage.submitFirstByte(firstByte);

    assertThat(lessonPage.firstByteFeedback())
        .containsText("The response timing revealed the first byte");
  }

  @Test
  @DisplayName("Reject an incomplete mitigation and accept the constant-time comparison API")
  void shouldReviewTheMitigation() {
    lessonPage.navigateTo(5);

    lessonPage.submitMitigation("early-exit", "random-delay");
    assertThat(lessonPage.mitigationFeedback())
        .containsText("merely makes measurement harder");

    lessonPage.submitMitigation("early-exit", "message-digest");
    assertThat(lessonPage.mitigationFeedback())
        .containsText("MessageDigest.isEqual is the appropriate Java comparison");
  }
}
