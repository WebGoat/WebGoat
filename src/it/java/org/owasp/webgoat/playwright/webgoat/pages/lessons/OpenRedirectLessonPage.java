/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class OpenRedirectLessonPage extends LessonPage {

  public OpenRedirectLessonPage(Page page) {
    super(page);
  }

  public void solveTask1(String url) {
    getPage().locator("input[id=\"t1url\"]").fill(url);
    submitTask("/OpenRedirect/task1");
  }

  public Locator task1Output() {
    return taskOutput("/OpenRedirect/task1");
  }

  public void solveTask2(String url) {
    getPage().locator("input[id=\"t2url\"]").fill(url);
    submitTask("/OpenRedirect/task2");
  }

  public Locator task2Output() {
    return taskOutput("/OpenRedirect/task2");
  }

  public void solveTask3(String target, String token) {
    getPage().locator("#t3target").fill(target);
    if (token != null) {
      getPage().locator("#t3token").fill(token);
    }
    submitTask("/OpenRedirect/task3");
  }

  public Locator task3Output() {
    return taskOutput("/OpenRedirect/task3");
  }

  public void solveTask4(String payload) {
    getPage().locator("#t4target").fill(payload);
    submitTask("/OpenRedirect/task4");
  }

  public void autofillTask4() {
    getPage().locator("#task4-autofill").click();
    submitTask("/OpenRedirect/task4");
  }

  public Locator task4Output() {
    return taskOutput("/OpenRedirect/task4");
  }

  public void submitMitigation(String url) {
    getPage().locator("#mitigationUrl").fill(url);
    submitTask("/OpenRedirect/mitigation");
  }

  public Locator mitigationOutput() {
    return taskOutput("/OpenRedirect/mitigation");
  }

  public com.microsoft.playwright.APIResponse invokeSafeRedirect(int destId) {
    return getPage()
        .context()
        .request()
        .get("/OpenRedirect/safe?destId=" + destId);
  }

  public void solveQuiz() {
    Page page = getPage();
    page.waitForSelector("#q_container input[name='question_0_solution']");
    page.locator("#question_0_0_input").check();
    page.locator("#question_1_2_input").check();
    page.locator("#question_2_0_input").check();
    page.locator("#question_3_0_input").check();

    page.locator("#quiz-form input[type='SUBMIT']").click();
    page.waitForSelector("#q_container .quiz_question.correct:nth-of-type(4)");
  }

  private void submitTask(String actionSuffix) {
    getPage()
        .locator("form[action$='" + actionSuffix + "'] button[type='submit']")
        .click();
  }

  private Locator taskOutput(String actionSuffix) {
    return getPage().locator("form[action$='" + actionSuffix + "'] ~ .attack-output");
  }

  public Locator quizContainer() {
    return getPage().locator("#q_container");
  }

  public Locator solvedAssignments() {
    return getPage().locator(".attack-link.solved-true");
  }
}
