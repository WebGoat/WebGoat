/*
 * SPDX-FileCopyrightText: Copyright © 2022 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LessonProgressService {

  private final UserProgressRepository userProgressRepository;
  private final Course course;

  /**
   * Endpoint for fetching the complete lesson overview which informs the user about whether all the
   * assignments are solved. Used as the last page of the lesson to generate a lesson overview.
   *
   * @return list of assignments
   */
  @GetMapping(value = "/service/lessonoverview.mvc/{lesson}")
  @ResponseBody
  public List<LessonOverview> lessonOverview(
      @PathVariable("lesson") LessonName lessonName, @CurrentUsername String username) {
    var userProgress = userProgressRepository.findByUser(username);
    var lesson = course.getLessonByName(lessonName);

    var lessonProgress = userProgress.getLessonProgress(lesson);
    return lessonProgress.getLessonOverview().entrySet().stream()
        .map(entry -> new LessonOverview(entry.getKey().getAssignment(), entry.getValue()))
        .toList();
  }

  @AllArgsConstructor
  @Getter
  // Jackson does not really like returning a map of <Assignment, Boolean> directly, see
  // http://stackoverflow.com/questions/11628698/can-we-make-object-as-key-in-map-when-using-json
  // so creating intermediate object is the easiest solution
  private static class LessonOverview {

    private Assignment assignment;
    private Boolean solved;
  }
}
