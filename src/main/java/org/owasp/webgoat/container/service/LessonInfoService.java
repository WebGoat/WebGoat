/*
 * SPDX-FileCopyrightText: Copyright © 2015 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.lessons.LessonInfoModel;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.container.session.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LessonInfoService {

  private final Course course;

  @GetMapping(path = "/service/lessoninfo.mvc/{lesson}")
  public @ResponseBody LessonInfoModel getLessonInfo(
      @PathVariable("lesson") LessonName lessonName) {
    var lesson = course.getLessonByName(lessonName);
    return new LessonInfoModel(lesson.getTitle(), false, false, false);
  }
}
