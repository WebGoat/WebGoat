package org.owasp.webgoat.plugin;

import com.beust.jcommander.internal.Lists;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;

import java.util.List;

/**
 * Created by jason on 9/29/17.
 */
public class CSRF extends NewLesson  {
    @Override
    public Category getDefaultCategory() {
        return Category.REQUEST_FORGERIES;
    }

    @Override
    public List<String> getHints() {
        return Lists.newArrayList();
    }

    @Override
    public Integer getDefaultRanking() {
        return 1;
    }

    @Override
    public String getTitle() { return "csrf.title"; }

    @Override
    public String getId() {
        return "CSRF";
    }

}
