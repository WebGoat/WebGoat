package org.owasp.webgoat.challenges;

import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.Lesson;

/**
 * @author nbaars
 * @since 3/21/17.
 */
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
