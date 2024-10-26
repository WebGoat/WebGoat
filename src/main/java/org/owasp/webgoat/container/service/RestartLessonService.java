/***************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
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
