/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webwolf.pages;

import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static org.owasp.webgoat.playwright.webgoat.PlaywrightTest.webWolfURL;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

public class WebWolfLoginPage {

  private final Page page;
  @Getter private final Locator signInButton;
  private final Locator signOutButton;

  public WebWolfLoginPage(Page page) {
    this.page = page;
    this.signInButton = this.page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Sign In"));
    this.signOutButton =
        this.page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign out"));
  }

  public void open() {
    page.navigate(webWolfURL("login"));
  }

  public void login(String username, String password) {
    page.getByPlaceholder("Username WebGoat").fill(username);
    page.getByPlaceholder("Password WebGoat").fill(password);
    signInButton.click();
  }

  public void logout() {
    this.signOutButton.click();
  }
}
