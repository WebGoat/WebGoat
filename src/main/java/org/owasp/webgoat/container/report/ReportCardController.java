/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
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

  public record ReportCard(
      int totalNumberOfLessons,
      int totalNumberOfAssignments,
      long numberOfAssignmentsSolved,
      long numberOfLessonsSolved,
      List<LessonStatistics> lessonStatistics) {}

  public record LessonStatistics(String name, boolean solved, int numberOfAttempts) {}
}
