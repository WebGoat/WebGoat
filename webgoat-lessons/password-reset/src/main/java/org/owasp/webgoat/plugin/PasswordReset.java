package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;

import java.util.ArrayList;
import java.util.List;

public class PasswordReset extends NewLesson {
    @Override
    public Category getDefaultCategory() {
        return Category.AUTHENTICATION;
    }

    @Override
    public List<String> getHints() {
        return new ArrayList();
    }

    @Override
    public Integer getDefaultRanking() {
        return 10;
    }

    @Override
    public String getTitle() {
        return "password-reset.title";
    }

    @Override
    public String getId() {
        return "PasswordReset";
    }
}
