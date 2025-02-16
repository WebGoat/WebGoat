/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.users.AssignmentProgress;
import org.owasp.webgoat.container.users.LessonProgress;

class LessonTrackerTest {

  @Test
  void allAssignmentsSolvedShouldMarkLessonAsComplete() {
    Lesson lesson = mock(Lesson.class);
    when(lesson.getAssignments())
        .thenReturn(List.of(new Assignment("assignment", "assignment", List.of(""))));
    LessonProgress lessonTracker = new LessonProgress(lesson);
    lessonTracker.assignmentSolved("assignment");

    Assertions.assertThat(lessonTracker.isLessonSolved()).isTrue();
  }

  @Test
  @DisplayName("Given two assignments when only one is solved then lesson is not solved")
  void noAssignmentsSolvedShouldMarkLessonAsInComplete() {
    Lesson lesson = mock(Lesson.class);
    Assignment a1 = new Assignment("a1");
    Assignment a2 = new Assignment("a2");
    List<Assignment> assignments = List.of(a1, a2);
    when(lesson.getAssignments()).thenReturn(assignments);
    LessonProgress lessonTracker = new LessonProgress(lesson);
    lessonTracker.assignmentSolved("a1");

    Map<AssignmentProgress, Boolean> lessonOverview = lessonTracker.getLessonOverview();
    assertThat(lessonOverview.values()).containsExactlyInAnyOrder(true, false);
  }

  @Test
  void solvingSameAssignmentShouldNotAddItTwice() {
    Lesson lesson = mock(Lesson.class);
    Assignment a1 = new Assignment("a1");
    List<Assignment> assignments = List.of(a1);
    when(lesson.getAssignments()).thenReturn(assignments);
    LessonProgress lessonTracker = new LessonProgress(lesson);
    lessonTracker.assignmentSolved("a1");
    lessonTracker.assignmentSolved("a1");

    assertThat(lessonTracker.getLessonOverview().size()).isEqualTo(1);
  }
}
