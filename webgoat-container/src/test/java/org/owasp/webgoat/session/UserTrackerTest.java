package org.owasp.webgoat.session;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * @since November 15, 2016
 */
public class UserTrackerTest {

    private File home;

    @Before
    public void init() throws IOException {
        home = File.createTempFile("test", "test");
        home.deleteOnExit();
    }

    @Test
    public void writeAndRead() {
        UserTracker userTracker = new UserTracker(home.getParent(), "test");
        AbstractLesson lesson = mock(AbstractLesson.class);
        when(lesson.getAssignments()).thenReturn(Lists.newArrayList(new Assignment("assignment", "assignment")));
        userTracker.getLessonTracker(lesson);
        userTracker.assignmentSolved(lesson, lesson.getAssignments().get(0).getName());

        userTracker = new UserTracker(home.getParent(), "test");
        userTracker.load();
        assertThat(userTracker.getLessonTracker(lesson).isLessonSolved()).isTrue();
    }

    @Test
    public void assignmentFailedShouldIncrementAttempts() {
        UserTracker userTracker = new UserTracker(home.getParent(), UUID.randomUUID().toString());
        AbstractLesson lesson = mock(AbstractLesson.class);
        when(lesson.getAssignments()).thenReturn(Lists.newArrayList(new Assignment("assignment", "assignment")));
        userTracker.getLessonTracker(lesson);
        userTracker.assignmentFailed(lesson);
        userTracker.assignmentFailed(lesson);

        assertThat(userTracker.getLessonTracker(lesson).getNumberOfAttempts()).isEqualTo(2);
    }

    @Test
    public void resetShouldClearSolvedAssignment() {
        UserTracker userTracker = new UserTracker(home.getParent(), "test");
        AbstractLesson lesson = mock(AbstractLesson.class);
        when(lesson.getAssignments()).thenReturn(Lists.newArrayList(new Assignment("assignment", "assignment")));
        userTracker.getLessonTracker(lesson);
        userTracker.assignmentSolved(lesson, "assignment");

        assertThat(userTracker.getLessonTracker(lesson).isLessonSolved()).isTrue();
        userTracker.reset(lesson);
        assertThat(userTracker.getLessonTracker(lesson).isLessonSolved()).isFalse();
    }

    @Test
    public void totalAssignmentsSolved() {
        UserTracker userTracker = new UserTracker(home.getParent(), "test");
        AbstractLesson lesson = mock(AbstractLesson.class);
        when(lesson.getAssignments()).thenReturn(Lists.newArrayList(new Assignment("assignment", "assignment")));
        userTracker.getLessonTracker(lesson);
        userTracker.assignmentSolved(lesson, "assignment");

        assertThat(userTracker.numberOfAssignmentsSolved()).isEqualTo(1);
        assertThat(userTracker.numberOfLessonsSolved()).isEqualTo(1);
    }
}
