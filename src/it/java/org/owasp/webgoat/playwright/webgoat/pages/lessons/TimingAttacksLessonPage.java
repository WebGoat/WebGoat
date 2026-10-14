/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import static org.owasp.webgoat.playwright.webgoat.PlaywrightTest.webGoatUrl;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.Comparator;
import java.util.stream.IntStream;

public class TimingAttacksLessonPage extends LessonPage {

  public TimingAttacksLessonPage(Page page) {
    super(page);
  }

  public Locator title() {
    return getPage().locator("#lesson-title");
  }

  public String recoverFirstByte() {
    return IntStream.range(0, 256)
        .mapToObj(candidate -> new Candidate(candidate, minimumDuration(candidate, 3)))
        .max(Comparator.comparingLong(Candidate::durationNanos))
        .map(candidate -> "%02x".formatted(candidate.value()))
        .orElseThrow();
  }

  public void submitFirstByte(String firstByte) {
    var form = getPage().locator("form[action$='/crypto/timing/first-byte']");
    form.locator("input[name='firstByte']").fill(firstByte);
    form.locator("button[type='submit']").click();
  }

  public Locator firstByteFeedback() {
    return getPage()
        .locator("form[action$='/crypto/timing/first-byte'] ~ .attack-feedback");
  }

  public void submitMitigation(String cause, String mitigation) {
    var form = getPage().locator("form[action$='/crypto/timing/mitigation']");
    form.locator("input[name='cause'][value='" + cause + "']").check();
    form.locator("input[name='mitigation'][value='" + mitigation + "']").check();
    form.locator("button[type='submit']").click();
  }

  public Locator mitigationFeedback() {
    return getPage().locator("form[action$='/crypto/timing/mitigation'] ~ .attack-feedback");
  }

  private long minimumDuration(int candidate, int samples) {
    return IntStream.range(0, samples)
        .mapToLong(ignored -> measure(candidate))
        .min()
        .orElseThrow();
  }

  private long measure(int candidate) {
    String signature = "%02x000000".formatted(candidate);
    long start = System.nanoTime();
    APIResponse response =
        getPage()
            .request()
            .get(
                webGoatUrl(
                    "crypto/timing/verify?message=WebGoat&signature=" + signature));
    long duration = System.nanoTime() - start;
    try {
      if (!response.ok()) {
        throw new IllegalStateException("Timing oracle returned HTTP " + response.status());
      }
      return duration;
    } finally {
      response.dispose();
    }
  }

  private record Candidate(int value, long durationNanos) {}
}
