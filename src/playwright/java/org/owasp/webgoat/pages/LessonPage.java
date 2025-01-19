package org.owasp.webgoat.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;
import org.owasp.webgoat.container.lessons.LessonName;

import static org.owasp.webgoat.PlaywrightTest.webGoatURL;

class LessonPage {

  @Getter private final Page page;

  public LessonPage(Page page) {
    this.page = page;
  }

  public void navigateTo(int pageNumber) {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("" + pageNumber)).click();
  }

  public void open(LessonName lessonName) {
    page.navigate(webGoatURL("start.mvc#lesson/%s".formatted(lessonName.lessonName())));
  }

  public int numberOfAssignments() {
    return page.locator(".attack-link.solved-false").count()
        + page.locator(".attack-link.solved-true").count();
  }

  public boolean isAssignmentSolved(int pageNumber) {
    var solvedAssignments = page.locator(".attack-link.solved-true").all();
    return solvedAssignments.stream().anyMatch(l -> l.textContent().equals("" + pageNumber));
  }

  public boolean noAssignmentsCompleted() {
    return page.locator(".attack-link.solved-true").count() == 0;
  }

  public Locator getAssignmentOutput() {
    return page.locator("#lesson-content-wrapper");
  }
}
