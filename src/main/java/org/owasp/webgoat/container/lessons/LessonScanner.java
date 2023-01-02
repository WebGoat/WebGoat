package org.owasp.webgoat.container.lessons;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LessonScanner {

  private static final Pattern lessonPattern = Pattern.compile("^.*/lessons/([^/]*)/.*$");

  @Getter private final Set<String> lessons = new HashSet<>();

  public LessonScanner(ResourcePatternResolver resourcePatternResolver) {
    try {
      var resources = resourcePatternResolver.getResources("classpath:/lessons/*/*");
      for (var resource : resources) {
        // WG can run as a fat jar or as directly from file system we need to support both so use
        // the URL
        var url = resource.getURL();
        var matcher = lessonPattern.matcher(url.toString());
        if (matcher.matches()) {
          lessons.add(matcher.group(1));
        }
      }
      log.debug("Found {} lessons", lessons.size());
    } catch (IOException e) {
      log.warn("No lessons found...");
    }
  }

  public List<String> applyPattern(String pattern) {
    return lessons.stream().map(lesson -> String.format(pattern, lesson)).toList();
  }
}
