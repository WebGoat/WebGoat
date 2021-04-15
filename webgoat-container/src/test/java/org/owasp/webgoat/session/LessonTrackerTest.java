package org.owasp.webgoat.session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.users.LessonTracker;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ************************************************************************************************
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
 * <p>
 *
 * @author nbaars
 * @version $Id: $Id
 * @since November 25, 2016
 */
class LessonTrackerTest {

    @Test
    void allAssignmentsSolvedShouldMarkLessonAsComplete() {
        Lesson lesson = mock(Lesson.class);
        when(lesson.getAssignments()).thenReturn(List.of(new Assignment("assignment", "assignment", List.of(""))));
        LessonTracker lessonTracker = new LessonTracker(lesson);
        lessonTracker.assignmentSolved("assignment");

        Assertions.assertThat(lessonTracker.isLessonSolved()).isTrue();
    }

    @Test
    void noAssignmentsSolvedShouldMarkLessonAsInComplete() {
        Lesson lesson = mock(Lesson.class);
        Assignment a1 = new Assignment("a1");
        Assignment a2 = new Assignment("a2");
        List<Assignment> assignments = List.of(a1, a2);
        when(lesson.getAssignments()).thenReturn(assignments);
        LessonTracker lessonTracker = new LessonTracker(lesson);
        lessonTracker.assignmentSolved("a1");

        Map<Assignment, Boolean> lessonOverview = lessonTracker.getLessonOverview();
        assertThat(lessonOverview.get(a1)).isTrue();
        assertThat(lessonOverview.get(a2)).isFalse();
    }

    @Test
    void solvingSameAssignmentShouldNotAddItTwice() {
        Lesson lesson = mock(Lesson.class);
        Assignment a1 = new Assignment("a1");
        List<Assignment> assignments = List.of(a1);
        when(lesson.getAssignments()).thenReturn(assignments);
        LessonTracker lessonTracker = new LessonTracker(lesson);
        lessonTracker.assignmentSolved("a1");
        lessonTracker.assignmentSolved("a1");

        assertThat(lessonTracker.getLessonOverview().size()).isEqualTo(1);
    }


}
