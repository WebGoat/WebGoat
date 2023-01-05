package org.owasp.webgoat.lessons.challenges.challenge1;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

/**
 * @author nbaars
 * @since 3/21/17.
 */
@Component
public class Challenge1 extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.CHALLENGE;
  }

  @Override
  public String getTitle() {
    return "challenge1.title";
  }
}
