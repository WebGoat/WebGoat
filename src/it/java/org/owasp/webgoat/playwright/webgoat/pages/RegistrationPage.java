/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages;

import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static org.owasp.webgoat.playwright.webgoat.PlaywrightTest.webGoatUrl;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

public class RegistrationPage {

  private final Page page;
  @Getter private final Locator signUpButton;

  public RegistrationPage(Page page) {
    this.page = page;
    this.signUpButton = this.page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Sign up"));
  }

  public void open() {
    page.navigate(webGoatUrl("registration"));
  }

  public void register(String username, String password) {
    page.getByPlaceholder("Username").fill(username);
    page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).fill(password);
    page.getByLabel("Confirm password").fill(password);
    page.getByLabel("Agree with the terms and").check();
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign up")).click();
  }
}
