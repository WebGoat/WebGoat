package org.owasp.webgoat.lessons.clientsidefiltering;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class ClientSideFiltering extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.CLIENT_SIDE;
  }

  @Override
  public String getTitle() {
    return "client.side.filtering.title";
  }
}
