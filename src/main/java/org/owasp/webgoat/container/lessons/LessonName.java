package org.owasp.webgoat.container.lessons;

import org.springframework.util.Assert;

/**
 * Wrapper class for the name of a lesson. This class is used to ensure that the lesson name is not
 * null and does not contain the ".lesson" suffix. The front-end passes the lesson name as a string
 * to the back-end, which then creates a new LessonName object with the lesson name as a parameter.
 * The constructor of the LessonName class checks if the lesson name is null and removes the
 * ".lesson" suffix if it is present.
 *
 * @param lessonName
 */
public record LessonName(String lessonName) {
  public LessonName {
    Assert.notNull(lessonName, "Lesson name cannot be null");
    if (lessonName.contains(".lesson")) {
      lessonName = lessonName.substring(0, lessonName.indexOf(".lesson"));
    }
  }
}
