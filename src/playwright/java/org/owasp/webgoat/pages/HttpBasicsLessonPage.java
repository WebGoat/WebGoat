package org.owasp.webgoat.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

public class HttpBasicsLessonPage extends LessonPage {

  @Getter private final Locator enterYourName;
  @Getter private final Locator goButton;

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
