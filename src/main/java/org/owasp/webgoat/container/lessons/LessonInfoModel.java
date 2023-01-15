package org.owasp.webgoat.container.lessons;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LessonInfoModel class.
 *
 * @author dm
 * @version $Id: $Id
 */
@Getter
@AllArgsConstructor
public class LessonInfoModel {

  private String lessonTitle;
  private boolean hasSource;
  private boolean hasSolution;
  private boolean hasPlan;
}
