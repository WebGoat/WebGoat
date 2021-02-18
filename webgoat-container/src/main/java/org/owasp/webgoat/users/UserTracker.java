
package org.owasp.webgoat.users;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.lessons.Assignment;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * ************************************************************************************************
 * <p>
 * <p>
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
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 29, 2003
 */
@Slf4j
@Entity
public class UserTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "username")
    private String user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<LessonTracker> lessonTrackers = new HashSet<>();

    private UserTracker() {}

    public UserTracker(final String user) {
        this.user = user;
    }

    /**
     * Returns an existing lesson tracker or create a new one based on the lesson
     *
     * @param lesson the lesson
     * @return a lesson tracker created if not already present
     */
    public LessonTracker getLessonTracker(Lesson lesson) {
        Optional<LessonTracker> lessonTracker = lessonTrackers
                .stream().filter(l -> l.getLessonName().equals(lesson.getId())).findFirst();
        if (!lessonTracker.isPresent()) {
            LessonTracker newLessonTracker = new LessonTracker(lesson);
            lessonTrackers.add(newLessonTracker);
            return newLessonTracker;
        } else {
            return lessonTracker.get();
        }
    }

    /**
     * Query method for finding a specific lesson tracker based on id
     *
     * @param id the id of the lesson
     * @return optional due to the fact we can only create a lesson tracker based on a lesson
     */
    public Optional<LessonTracker> getLessonTracker(String id) {
        return lessonTrackers.stream().filter(l -> l.getLessonName().equals(id)).findFirst();
    }

    public void assignmentSolved(Lesson lesson, String assignmentName) {
        LessonTracker lessonTracker = getLessonTracker(lesson);
        lessonTracker.incrementAttempts();
        lessonTracker.assignmentSolved(assignmentName);
    }

    public void assignmentFailed(Lesson lesson) {
        LessonTracker lessonTracker = getLessonTracker(lesson);
        lessonTracker.incrementAttempts();
    }

    public void reset(Lesson al) {
        LessonTracker lessonTracker = getLessonTracker(al);
        lessonTracker.reset();
    }

    public int numberOfLessonsSolved() {
        int numberOfLessonsSolved = 0;
        for (LessonTracker lessonTracker : lessonTrackers) {
            if (lessonTracker.isLessonSolved()) {
                numberOfLessonsSolved = numberOfLessonsSolved + 1;
            }
        }
        return numberOfLessonsSolved;
    }

    public int numberOfAssignmentsSolved() {
        int numberOfAssignmentsSolved = 0;
        for (LessonTracker lessonTracker : lessonTrackers) {
            Map<Assignment, Boolean> lessonOverview = lessonTracker.getLessonOverview();
            numberOfAssignmentsSolved = lessonOverview.values().stream().filter(b -> b).collect(Collectors.counting()).intValue();
        }
        return numberOfAssignmentsSolved;
    }
}
