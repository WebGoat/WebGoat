package org.owasp.webgoat.plugin;

import com.beust.jcommander.internal.Lists;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;

import java.util.List;

/**
 * @author nbaars
 * @since 3/22/17.
 */
public class JWT extends NewLesson {

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
        return 40;
    }

    @Override
    public String getTitle() {
        return "jwt.title";
    }

    @Override
    public String getId() {
        return "JWT";
    }
}
