/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages;

import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static org.owasp.webgoat.playwright.webgoat.PlaywrightTest.webGoatUrl;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;

public class WebGoatLoginPage {

  private final Page page;
  @Getter private final Locator signInButton;

  public WebGoatLoginPage(Page page) {
    this.page = page;
    this.signInButton = this.page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Sign in"));
  }

  public void open() {
    page.navigate(webGoatUrl("login"));
  }

  public void login(String username, String password) {
    page.getByPlaceholder("Username").fill(username);
    page.getByPlaceholder("Password").fill(password);
    page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
  }
}
