package org.owasp.webgoat;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.helpers.Authentication;
import org.owasp.webgoat.pages.HttpBasicsLessonPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpBasicsLessonUITest extends PlaywrightTest {

  private HttpBasicsLessonPage lessonPage;

  @BeforeEach
  void navigateToLesson(Page page) {

    this.lessonPage = new HttpBasicsLessonPage(page);
    lessonPage.open(new LessonName("HttpBasics"));
  }

  @Test
  @Order(1)
  void shouldShowDefaultPage(Page page) {
      var auth =  page.context().storageState(new BrowserContext.StorageStateOptions());
    assertThat(lessonPage.getTitle()).hasText("HTTP Basics");
    Assertions.assertThat(lessonPage.noAssignmentsCompleted()).isTrue();
    Assertions.assertThat(lessonPage.numberOfAssignments()).isEqualTo(2);
  }

  @Test
  @Order(2)
  @DisplayName(
      "When the user enters their name, the server should reverse it then the assignment should be solved")
  void solvePage2() {
    lessonPage.navigateTo(2);
    lessonPage.getEnterYourName().fill("John Doe");
    lessonPage.getGoButton().click();

    assertThat(lessonPage.getAssignmentOutput())
        .containsText("The server has reversed your name: eoD nhoJ");
    Assertions.assertThat(lessonPage.isAssignmentSolved(2)).isTrue();
  }

  @Test
  @Order(3)
  @DisplayName("When the user enters nothing then the server should display an error message")
  void invalidPage2() {
    lessonPage.navigateTo(2);
    lessonPage.getEnterYourName().fill("");
    lessonPage.getGoButton().click();

    assertThat(lessonPage.getAssignmentOutput()).containsText("Try again, name cannot be empty.");
  }
}
