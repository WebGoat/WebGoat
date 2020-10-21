package org.owasp.webgoat.cia;

import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.Lesson;
import org.springframework.stereotype.Component;

/**
 * @author BenediktStuhrmann
 * @since 11/2/18.
 */
@Component
public class CIA extends Lesson {

    @Override
    public Category getDefaultCategory() {
        return Category.GENERAL;
    }

    @Override
    public String getTitle() {
        return "4.cia.title";//4th lesson in general
    }
}