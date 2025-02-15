package org.owasp.webgoat.lessons.logging;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;


@Component
public class LogSpoofing extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A9;
  }

  @Override
  public String getTitle() {
    return "logging.title";
  }
}
