/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.google.gson.JsonObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.regex.Pattern;

public class WebAuthnLessonPage extends LessonPage {

  public WebAuthnLessonPage(Page page) {
    super(page);
  }

  /** Registers a new key while replacing Tom's user handle ("dG9t") with Jerry's. */
  public void registerKeyForJerry() {
    Page page = getPage();
    page.locator("#webauthn-reg-begin").click();
    assertThat(page.locator("#webauthn-reg-touch")).isEnabled();
    page.locator("#webauthn-reg-touch").click();
    Locator body = page.locator("#webauthn-reg-body");
    assertThat(body).hasValue(Pattern.compile("attestationObject"));
    body.fill(body.inputValue().replace("\"dG9t\"", "\"amVycnk\""));
    page.locator("form[action$='/WebAuthn/registration/finish'] button[type='submit']").click();
  }

  public Locator registrationFeedback() {
    return feedbackOf("#webauthn-reg-body");
  }

  /** Copies the request from the support ticket into the sign in form and sends it. */
  public void replayCapturedLogin() {
    Page page = getPage();
    page.locator("#webauthn-ticket-open").click();
    Locator ticket = page.locator("#webauthn-ticket-content");
    assertThat(ticket).containsText("authenticatorData");
    String captured = ticket.textContent();
    page.locator("#webauthn-login-body").fill(captured.substring(captured.indexOf('{')));
    page.locator("form[action$='/WebAuthn/authentication/finish'] button[type='submit']").click();
  }

  public Locator authenticationFeedback() {
    return feedbackOf("#webauthn-login-body");
  }

  public void solveQuiz() {
    Page page = getPage();
    page.waitForSelector("#q_container input[name='question_0_solution']");
    page.locator("#question_0_2_input").check();
    page.locator("#question_1_2_input").check();
    page.locator("#question_2_2_input").check();
    page.locator("#question_3_2_input").check();
    page.locator("#question_4_1_input").check();
    page.locator("#quiz-form input[type='SUBMIT']").click();
    page.waitForSelector("#q_container .quiz_question.correct:nth-of-type(5)");
  }

  /**
   * Attaches a virtual roaming authenticator (CTAP2 over USB) to the browser through the Chrome
   * DevTools Protocol, the same as the WebAuthn panel in DevTools.
   */
  public void attachVirtualSecurityKey() {
    var cdp = getPage().context().newCDPSession(getPage());
    cdp.send("WebAuthn.enable");
    JsonObject options = new JsonObject();
    options.addProperty("protocol", "ctap2");
    options.addProperty("transport", "usb");
    options.addProperty("hasResidentKey", false);
    options.addProperty("hasUserVerification", false);
    options.addProperty("automaticPresenceSimulation", true);
    JsonObject arguments = new JsonObject();
    arguments.add("options", options);
    cdp.send("WebAuthn.addVirtualAuthenticator", arguments);
  }

  public void registerSecurityKey() {
    getPage().locator("#webauthn-sk-register").click();
  }

  public Locator securityKeyRegistration() {
    return getPage().locator("#webauthn-sk-registration");
  }

  public void signInWithSecurityKey() {
    getPage().locator("#webauthn-sk-login").click();
  }

  public Locator securityKeyFeedback() {
    return feedbackOf("#webauthn-sk-form");
  }

  private Locator feedbackOf(String selector) {
    Page page = getPage();
    return page.locator(
            ".attack-container", new Page.LocatorOptions().setHas(page.locator(selector)))
        .locator(".attack-feedback");
  }
}
