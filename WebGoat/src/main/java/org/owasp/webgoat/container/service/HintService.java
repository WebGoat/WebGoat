/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.util.Collection;
import java.util.List;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.Hint;
import org.owasp.webgoat.container.session.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HintService {

  public static final String URL_HINTS_MVC = "/service/hint.mvc";
  private final List<Hint> allHints;

  public HintService(Course course) {
    this.allHints =
        course.getLessons().stream()
            .flatMap(lesson -> lesson.getAssignments().stream())
            .map(this::createHint)
            .flatMap(Collection::stream)
            .toList();
  }

  /**
   * Returns hints for current lesson
   *
   * @return a {@link java.util.List} object.
   */
  @GetMapping(path = URL_HINTS_MVC, produces = "application/json")
  @ResponseBody
  public List<Hint> getHints() {
    return allHints;
  }

  private List<Hint> createHint(Assignment a) {
    return a.getHints().stream().map(h -> new Hint(h, a.getPath())).toList();
  }
}
