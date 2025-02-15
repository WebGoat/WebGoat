package org.owasp.webgoat.lessons.htmltampering;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;


@Component
public class HtmlTampering extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.CLIENT_SIDE;
  }

  @Override
  public String getTitle() {
    return "html-tampering.title";
  }
}
