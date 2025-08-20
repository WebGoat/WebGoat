/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webwolf;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webwolf.pages.WebWolfLoginPage;

public class LoginUITest extends PlaywrightTest {

  @Test
  void login(Browser browser) {
    var page = Authentication.tweety(browser);
    var loginPage = new WebWolfLoginPage(page);
    loginPage.open();
    loginPage.login(Authentication.getTweety().name(), Authentication.getTweety().password());

    assertThat(loginPage.getSignInButton()).not().isVisible();

    // logout
    loginPage.logout();

    assertThat(loginPage.getSignInButton()).isVisible();
  }
}
