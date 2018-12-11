package org.owasp.webgoat.plugin;

import com.beust.jcommander.internal.Lists;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;

import java.util.List;

/**
 * @author TMelzer
 * @since 30.11.18
 */
public class ChromeDevTools extends NewLesson {

    @Override
    public Category getDefaultCategory() {
      return Category.GENERAL;
    }

    @Override
    public List<String> getHints() {
      return Lists.newArrayList();
    }

    @Override
    public Integer getDefaultRanking() {
      return 4;
    }

    @Override
    public String getTitle() {
      return "chrome-dev-tools.title";
    }

    @Override
    public String getId() {
      return "ChromeDevTools";
    }
  }
