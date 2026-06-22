/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import static org.owasp.webgoat.playwright.webgoat.PlaywrightTest.webGoatUrl;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.owasp.webgoat.container.lessons.LessonName;

public class SecurityMisconfigurationLessonPage extends LessonPage {

  private static final Pattern TOKEN_PATTERN = Pattern.compile("SYSTEM_API_TOKEN=([\\w-]+)");
  private static final Pattern API_KEY_PATTERN =
      Pattern.compile("\"systemApiKey\"\s*:\s*\"([^\"]+)\"");

  public SecurityMisconfigurationLessonPage(Page page) {
    super(page);
  }

  public void open(LessonName lessonName) {
    getPage().navigate(webGoatUrl("start.mvc#lesson/%s".formatted(lessonName.lessonName())));
  }

  public void fillDefaultCredentials(String username, String password) {
    getPage().locator("[name='username']").fill(username);
    getPage().locator("[name='password']").fill(password);
  }

  public void submitTask1() {
    getPage().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Attempt login")).click();
  }

  public Locator task1Output() {
    return getAssignmentOutput();
  }

  public void triggerDebugLeak() {
    Page page = getPage();
    page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Trigger debug error")).click();
    page.waitForTimeout(500); // allow fetch to complete
  }

  public String debugOutput() {
    return getPage().locator("#debug-output").textContent();
  }

  public String extractTokenFromDebug() {
    Matcher matcher = TOKEN_PATTERN.matcher(debugOutput());
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }

  public void submitTask2(String token) {
    Page page = getPage();
    page.getByLabel("Leaked token").fill(token);
    page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Submit token")).click();
  }

  public String requestActuatorEnv() {
    Page page = getPage();
    page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("GET /actuator/env")).click();
    page.waitForTimeout(300);
    return page.locator("#actuator-output").textContent();
  }

  public String extractApiKey(String json) {
    Matcher matcher = API_KEY_PATTERN.matcher(json);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }

  public void submitTask3(String apiKey) {
    Page page = getPage();
    page.getByLabel("System API key").fill(apiKey);
    page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Submit key")).click();
  }

  public Locator task3Output() {
    return getPage().locator(".lesson-page-wrapper").nth(3).locator(".attack-output");
  }

  public void applyHardeningConfig() {
    Page page = getPage();
    page.getByLabel("management.endpoint.env.enabled").selectOption("false");
    page.getByLabel("management.endpoint.health.show-details").selectOption("never");
    page.getByLabel("spring.security.user.name").fill("");
    page.getByLabel("spring.security.user.password").fill("");
    page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Apply configuration")).click();
  }

  public Locator task4Output() {
    return getPage().locator(".lesson-page-wrapper").locator(".attack-output").last();
  }
}
