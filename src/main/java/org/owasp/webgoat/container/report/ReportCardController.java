/**
 * *************************************************************************************************
 *
 * <p>
 *
 * <p>This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * <p>Copyright (c) 2002 - 2014 Bruce Mayhew
 *
 * <p>This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * <p>Getting Source ==============
 *
 * <p>Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 */
package org.owasp.webgoat.container.report;

import java.util.List;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportCardController {

  private final UserProgressRepository userProgressRepository;
  private final Course course;
  private final PluginMessages pluginMessages;

  public ReportCardController(
      UserProgressRepository userProgressRepository, Course course, PluginMessages pluginMessages) {
    this.userProgressRepository = userProgressRepository;
    this.course = course;
    this.pluginMessages = pluginMessages;
  }

  /**
   * Endpoint which generates the report card for the current use to show the stats on the solved
   * lessons
   */
  @GetMapping(path = "/service/reportcard.mvc", produces = "application/json")
  @ResponseBody
  public ReportCard reportCard(@CurrentUsername String username) {
    var userProgress = userProgressRepository.findByUser(username);
    var lessonStatistics =
        course.getLessons().stream()
            .map(
                lesson -> {
                  var lessonTracker = userProgress.getLessonProgress(lesson);
                  return new LessonStatistics(
                      pluginMessages.getMessage(lesson.getTitle()),
                      lessonTracker.isLessonSolved(),
                      lessonTracker.getNumberOfAttempts());
                })
            .toList();
    return new ReportCard(
        course.getTotalOfLessons(),
        course.getTotalOfAssignments(),
        userProgress.numberOfAssignmentsSolved(),
        userProgress.numberOfLessonsSolved(),
        lessonStatistics);
  }

  private record ReportCard(
      int totalNumberOfLessons,
      int totalNumberOfAssignments,
      long numberOfAssignmentsSolved,
      long numberOfLessonsSolved,
      List<LessonStatistics> lessonStatistics) {}

  private record LessonStatistics(String name, boolean solved, int numberOfAttempts) {}
}
