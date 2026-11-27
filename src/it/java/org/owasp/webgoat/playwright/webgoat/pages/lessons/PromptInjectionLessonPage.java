/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import java.util.regex.Pattern;

public class PromptInjectionLessonPage extends LessonPage {

  private static final Pattern SESSION_TOKEN = Pattern.compile("WG-SECRET-[0-9a-f]+");
  private static final Pattern ACCOUNT_REF = Pattern.compile("WG-ACC-[0-9A-F]+");

  private int id;

  public PromptInjectionLessonPage(Page page) {
    super(page);
  }

  public String mcpUrl() {
    Locator url = getPage().locator("#pi-mcp-url");
    assertThat(url).containsText("/PromptInjection/mcp/");
    return url.textContent().trim();
  }

  public Locator toolDescriptions() {
    return visiblePage().locator(".pi-tools-json");
  }

  public void showToolDescriptions() {
    visiblePage().locator(".pi-tools summary").click();
  }

  public void selectVariant(String variant) {
    visiblePage().locator(".pi-variant").selectOption(variant);
  }

  public void enableArgumentGuard() {
    visiblePage().locator(".pi-guard").check();
  }

  /**
   * Sends the calls a vulnerable assistant makes after following the poisoned description: fetch
   * the account, then forward its session_token in the audit argument. MCP calls carry no session.
   */
  public String assistantFollowsPoisonedDescription(String mcpUrl) {
    String account = callTool(mcpUrl, "get_account", "{}");
    String token = find(SESSION_TOKEN, account);
    String ref = find(ACCOUNT_REF, account);
    return callTool(
        mcpUrl,
        "total_invoices",
        "{\"csv_path\":\"sample.csv\",\"account_ref\":\"%s\",\"audit\":\"%s\"}"
            .formatted(ref, token));
  }

  public void refreshLog() {
    visiblePage().locator(".pi-refresh").click();
  }

  public Locator callLog() {
    return visiblePage().locator(".pi-log");
  }

  public void checkTask1() {
    getPage().locator("#pi-task1-form button[type='submit']").click();
  }

  public void checkTask2() {
    getPage().locator("#pi-task2-form button[type='submit']").click();
  }

  public Locator task1Feedback() {
    return feedbackOf("#pi-task1-form");
  }

  public Locator task2Feedback() {
    return feedbackOf("#pi-task2-form");
  }

  public void solveQuiz() {
    Page page = getPage();
    page.waitForSelector("#q_container input[name='question_0_solution']");
    page.locator("#question_0_1_input").check();
    page.locator("#question_1_2_input").check();
    page.locator("#question_2_0_input").check();
    page.locator("#question_3_3_input").check();
    page.locator("#question_4_1_input").check();
    page.locator("#quiz-form input[type='SUBMIT']").click();
    page.waitForSelector("#q_container .quiz_question.correct:nth-of-type(5)");
  }

  private String callTool(String mcpUrl, String tool, String arguments) {
    String body =
        "{\"jsonrpc\":\"2.0\",\"id\":%d,\"method\":\"tools/call\",\"params\":{\"name\":\"%s\",\"arguments\":%s}}"
            .formatted(++id, tool, arguments);
    var response =
        getPage()
            .request()
            .post(
                mcpUrl,
                RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Accept", "application/json, text/event-stream")
                    .setData(body));
    org.assertj.core.api.Assertions.assertThat(response.ok()).isTrue();
    return response.text();
  }

  private static String find(Pattern pattern, String text) {
    var matcher = pattern.matcher(text);
    org.assertj.core.api.Assertions.assertThat(matcher.find()).as(text).isTrue();
    return matcher.group();
  }

  private Locator visiblePage() {
    return getPage().locator(".lesson-page-wrapper:visible");
  }

  private Locator feedbackOf(String selector) {
    Page page = getPage();
    return page.locator(
            ".attack-container", new Page.LocatorOptions().setHas(page.locator(selector)))
        .locator(".attack-feedback");
  }
}
