/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webgoat.pages.WebGoatLoginPage;

class LoginUITest extends PlaywrightTest {

  @Test
  void loginLogout(Browser browser) {
    var page = Authentication.tweety(browser);
    var loginPage = new WebGoatLoginPage(page);
    loginPage.open();
    loginPage.login(Authentication.getTweety().name(), Authentication.getTweety().password());

    // logout
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
    page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Logout")).click();

    assertThat(loginPage.getSignInButton()).isVisible();
  }
}
