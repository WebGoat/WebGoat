package org.owasp.webgoat.lessons.deserialization;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;


@Component
public class InsecureDeserialization extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A8;
  }

  @Override
  public String getTitle() {
    return "insecure-deserialization.title";
  }
}
