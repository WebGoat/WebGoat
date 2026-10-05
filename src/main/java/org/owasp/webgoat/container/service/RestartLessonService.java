/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@AllArgsConstructor
@Slf4j
public class RestartLessonService {

  private final Course course;
  private final UserProgressRepository userTrackerRepository;
  private final Function<String, Flyway> flywayLessons;
  private final List<Initializable> lessonsToInitialize;

  @GetMapping(path = "/service/restartlesson.mvc/{lesson}")
  @ResponseStatus(value = HttpStatus.OK)
  public void restartLesson(
      @PathVariable("lesson") LessonName lessonName, @CurrentUser WebGoatUser user) {
    var lesson = course.getLessonByName(lessonName);

    UserProgress userTracker = userTrackerRepository.findByUser(user.getUsername());
    userTracker.reset(lesson);
    userTrackerRepository.save(userTracker);

    var flyway = flywayLessons.apply(user.getUsername());
    flyway.clean();
    flyway.migrate();

    lessonsToInitialize.forEach(i -> i.initialize(user));
  }
}
