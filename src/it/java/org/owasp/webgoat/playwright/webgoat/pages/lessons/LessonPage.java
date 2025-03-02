/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webgoat.pages.lessons;

import static org.owasp.webgoat.playwright.webgoat.PlaywrightTest.webGoatUrl;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.owasp.webgoat.container.lessons.LessonName;

@Getter
public class LessonPage {

  private final Page page;

  public LessonPage(Page page) {
    this.page = page;
  }

  public void navigateTo(int pageNumber) {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("" + pageNumber)).click();
  }

  public void open(LessonName lessonName) {
    page.navigate(webGoatUrl("start.mvc#lesson/%s".formatted(lessonName.lessonName())));
  }

  /**
   * Force a reload for the UI to response, this is normally done by a JavaScript reloading every 5
   * seconds
   */
  public void refreshPage() {
    page.reload();
  }

  public void resetLesson(LessonName lessonName) {
    Assertions.assertThat(
            page.request()
                .get(webGoatUrl("service/restartlesson.mvc/%s".formatted(lessonName)))
                .ok())
        .isTrue();
    refreshPage();
  }

  public int numberOfAssignments() {
    return page.locator(".attack-link.solved-false").count()
        + page.locator(".attack-link.solved-true").count();
  }

  public boolean isAssignmentSolved(int pageNumber) {
    var solvedAssignments = page.locator(".attack-link.solved-true");
    solvedAssignments.waitFor();
    return solvedAssignments.all().stream().anyMatch(l -> l.textContent().equals("" + pageNumber));
  }

  public boolean noAssignmentsCompleted() {
    return page.locator(".attack-link.solved-true").count() == 0;
  }

  public Locator getAssignmentOutput() {
    return page.locator("#lesson-content-wrapper");
  }

  public Locator getHintsOutput() {
    return page.locator("#lesson-hint");
  }
}
