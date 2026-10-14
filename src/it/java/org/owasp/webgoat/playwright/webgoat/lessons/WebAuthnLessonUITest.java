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
import org.owasp.webgoat.playwright.webgoat.pages.lessons.WebAuthnLessonPage;

public class WebAuthnLessonUITest extends PlaywrightTest {

  private WebAuthnLessonPage lessonPage;

  @BeforeEach
  void navigateToLesson(Browser browser) {
    var lessonName = new LessonName("WebAuthn");
    var page = Authentication.sylvester(browser);

    this.lessonPage = new WebAuthnLessonPage(page);
    lessonPage.resetLesson(lessonName);
    lessonPage.open(lessonName);
  }

  @Test
  @DisplayName("Solve the GoatBank registration and authentication assignments and the quiz")
  void shouldSolveGoatBankAssignments() {
    lessonPage.navigateTo(4);
    lessonPage.registerKeyForJerry();
    assertThat(lessonPage.registrationFeedback()).containsText("Jerry");
    assertThat(lessonPage.registrationFeedback()).containsText("Well done");

    lessonPage.navigateTo(6);
    lessonPage.replayCapturedLogin();
    assertThat(lessonPage.authenticationFeedback()).containsText("signed in as Jerry");

    lessonPage.navigateTo(8);
    lessonPage.solveQuiz();
  }

  @Test
  @DisplayName("Register and sign in with a (virtual) roaming security key")
  void shouldSignInWithSecurityKey() {
    lessonPage.attachVirtualSecurityKey();
    lessonPage.navigateTo(10);

    lessonPage.registerSecurityKey();
    assertThat(lessonPage.securityKeyRegistration()).containsText("\"registered\": true");
    assertThat(lessonPage.securityKeyRegistration()).containsText("BE=false");

    lessonPage.signInWithSecurityKey();
    assertThat(lessonPage.securityKeyFeedback()).containsText("You signed in with your security key");
  }
}
