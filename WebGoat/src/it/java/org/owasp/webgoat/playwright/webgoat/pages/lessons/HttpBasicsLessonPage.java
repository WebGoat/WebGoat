/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

@Getter
public class HttpBasicsLessonPage extends LessonPage {

  private final Locator enterYourName;
  private final Locator goButton;

  public HttpBasicsLessonPage(Page page) {
    super(page);
    enterYourName = page.locator("input[name=\"person\"]");
    goButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Go!"));
  }

  public Locator getTitle() {
    return getPage()
        .getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("HTTP Basics"));
  }
}
