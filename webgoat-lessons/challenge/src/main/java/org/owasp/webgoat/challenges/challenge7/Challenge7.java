package org.owasp.webgoat.challenges.challenge7;

import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.Lesson;
import org.springframework.stereotype.Component;

/**
 * @author nbaars
 * @since 3/21/17.
 */
@Component
public class Challenge7 extends Lesson {

    @Override
    public Category getDefaultCategory() {
        return Category.CHALLENGE;
    }

    @Override
    public String getTitle() {
        return "challenge7.title";
    }

    @Override
    public String getId() {
        return "Challenge7";
    }
}
