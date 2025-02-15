

package org.owasp.webgoat.lessons.lessontemplate;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonTemplate extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.GENERAL;
  }

  @Override
  public String getTitle() {
    return "lesson-template.title";
  }
}
