/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webgoat.pages.RegistrationPage;
import org.owasp.webgoat.playwright.webgoat.pages.WebGoatLoginPage;

public class RegistrationUITest extends PlaywrightTest {

  @Test
  @DisplayName("Should register a new user while logged in as other user")
  void registerWhileLoggedIn(Browser browser) {
    var page = Authentication.tweety(browser);
    var loginPage = new WebGoatLoginPage(page);
    loginPage.open();
    loginPage.login(Authentication.getTweety().name(), Authentication.getTweety().password());

    var newUsername = "newuser" + System.currentTimeMillis();
    var password = "password123";
    var registrationPage = new RegistrationPage(page);
    registrationPage.open();
    registrationPage.register(newUsername, password);

    assertThat(page.content()).contains(newUsername);
  }

  @Test
  @DisplayName("Should register a new user")
  void registerNewUser(Browser browser) {
    var page = browser.newContext(new Browser.NewContextOptions().setLocale("en-US")).newPage();
    var registrationPage = new RegistrationPage(page);
    registrationPage.open();

    var newUsername = "newuser" + System.currentTimeMillis();
    var password = "password123";
    registrationPage.register(newUsername, password);

    assertThat(page.content()).contains(newUsername);
  }
}
