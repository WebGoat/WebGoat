/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.regex.Pattern;

public class PasswordStorageLessonPage extends LessonPage {

  private static final Pattern RESULT_PATTERN =
      Pattern.compile("Match found after \\d+ candidates: (\\S+)");
  private static final Pattern PEPPER_PATTERN =
      Pattern.compile("blog\\.password\\.pepper=([^\\r\\n]+)");
  private static final Pattern ARGON_PATTERN =
      Pattern.compile(
          "\\$argon2id\\$v=(\\d+)\\$m=(\\d+),t=(\\d+),p=(\\d+)\\$([^$]+)\\$[^\\s]+$");

  public PasswordStorageLessonPage(Page page) {
    super(page);
  }

  public Locator title() {
    return getPage().locator("#lesson-title");
  }

  public void runStage1Wordlist() {
    getPage().locator("#password-storage-stage1-crack").click();
  }

  public Locator stage1ToolOutput() {
    return getPage().locator("#password-storage-stage1-output");
  }

  public void submitStage1Password(String password) {
    Locator form = assignmentForm("stage1");
    form.locator("input[name='password']").fill(password);
    form.locator("button[type='submit']").click();
  }

  public Locator stage1Feedback() {
    return assignmentFeedback("stage1");
  }

  public void runStage2Wordlist() {
    getPage().locator("#password-storage-stage2-crack").click();
  }

  public Locator stage2ToolOutput() {
    return getPage().locator("#password-storage-stage2-output");
  }

  public void submitStage2(String password) {
    Locator form = assignmentForm("stage2");
    form.locator("select[name='inference']").selectOption("different-salts-hide-equality");
    form.locator("input[name='password']").fill(password);
    form.locator("button[type='submit']").click();
  }

  public Locator stage2Feedback() {
    return assignmentFeedback("stage2");
  }

  public void submitStage3Parameters() {
    ArgonParameters parameters = parseArgonParameters(stage3Dump().textContent());
    Locator form = assignmentForm("stage3");
    form.locator("input[name='algorithm']").fill("argon2id");
    form.locator("input[name='version']").fill(parameters.version());
    form.locator("input[name='memory']").fill(parameters.memory());
    form.locator("input[name='iterations']").fill(parameters.iterations());
    form.locator("input[name='parallelism']").fill(parameters.parallelism());
    form.locator("input[name='salt']").fill(parameters.salt());
    form.locator("select[name='comparison']").selectOption("argon2id-costs-more-per-guess");
    form.locator("button[type='submit']").click();
  }

  public Locator stage3Dump() {
    return getPage().locator("#password-storage-stage3-dump");
  }

  public Locator stage3Feedback() {
    return assignmentFeedback("stage3");
  }

  public void submitStage4Explanation() {
    Locator form = assignmentForm("stage4");
    form.locator("select[name='databaseSufficient']").selectOption("no");
    form.locator("select[name='missingValue']").selectOption("pepper");
    form.locator("button[type='submit']").click();
  }

  public Locator stage4Feedback() {
    return assignmentFeedback("stage4");
  }

  public void retrieveConfiguration() {
    getPage().locator("#password-storage-load-config").click();
  }

  public Locator configurationOutput() {
    return getPage().locator("#password-storage-config-output");
  }

  public String pepperFromConfiguration() {
    return requireMatch(PEPPER_PATTERN, configurationOutput().textContent(), "pepper");
  }

  public void runStage5Attack(String pepper) {
    getPage().locator("#password-storage-pepper").fill(pepper);
    getPage().locator("#password-storage-stage5-crack").click();
  }

  public Locator stage5ToolOutput() {
    return getPage().locator("#password-storage-stage5-output");
  }

  public void submitStage5Password(String password) {
    Locator form = assignmentForm("stage5");
    form.locator("input[name='password']").fill(password);
    form.locator("button[type='submit']").click();
  }

  public Locator stage5Feedback() {
    return assignmentFeedback("stage5");
  }

  public String passwordFromTool(Locator output) {
    return requireMatch(RESULT_PATTERN, output.textContent(), "password");
  }

  private Locator assignmentForm(String stage) {
    return getPage().locator("form[action$='/PasswordStorage/" + stage + "']");
  }

  private Locator assignmentFeedback(String stage) {
    return getPage()
        .locator("form[action$='/PasswordStorage/" + stage + "'] ~ .attack-feedback");
  }

  private ArgonParameters parseArgonParameters(String encodedHash) {
    var matcher = ARGON_PATTERN.matcher(encodedHash.strip());
    if (!matcher.find()) {
      throw new IllegalStateException("The Argon2id record did not use the expected PHC format");
    }
    return new ArgonParameters(
        matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
  }

  private String requireMatch(Pattern pattern, String text, String valueName) {
    var matcher = pattern.matcher(text);
    if (!matcher.find()) {
      throw new IllegalStateException("Could not extract the " + valueName + " from: " + text);
    }
    return matcher.group(1);
  }

  private record ArgonParameters(
      String version, String memory, String iterations, String parallelism, String salt) {}
}
