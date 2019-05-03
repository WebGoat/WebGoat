package org.owasp.webgoat.plugin;

import com.beust.jcommander.internal.Lists;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;

import java.util.List;

/**
 * @author BenediktStuhrmann
 * @since 12/2/18.
 */
public class SecurePasswords extends NewLesson {

    @Override
    public Category getDefaultCategory() {
        return Category.AUTHENTICATION;
    }

    @Override
    public List<String> getHints() {
        return Lists.newArrayList();
    }

    @Override
    public Integer getDefaultRanking() {
        return 3;
    }

    @Override
    public String getTitle() {
        return "secure-passwords.title";
    }

    @Override
    public String getId() {
        return "SecurePasswords";
    }
}
