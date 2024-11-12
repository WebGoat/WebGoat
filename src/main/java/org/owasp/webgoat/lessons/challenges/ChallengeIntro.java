package org.owasp.webgoat.lessons.challenges;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

/**
 * @author nbaars
 * @since 3/21/17.
 */
@Component
public class ChallengeIntro extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.CHALLENGE;
  }

  @Override
  public String getTitle() {
    return "challenge0.title";
  }
}
