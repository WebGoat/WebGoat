
package org.owasp.webgoat.users;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;

import javax.persistence.*;
import java.util.List;
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
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 29, 2003
 */
@Entity
public class LessonTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    private String lessonName;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Assignment> solvedAssignments = Sets.newHashSet();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Assignment> allAssignments = Sets.newHashSet();
    @Getter
    private int numberOfAttempts = 0;

    private LessonTracker() {
        //JPA
    }

    public LessonTracker(AbstractLesson lesson) {
        lessonName = lesson.getId();
        allAssignments.addAll(lesson.getAssignments());
    }

    public Optional<Assignment> getAssignment(String name) {
        return allAssignments.stream().filter(a -> a.getName().equals(name)).findFirst();
    }

    /**
     * Mark an assignment as solved
     *
     * @param solvedAssignment the assignment which the user solved
     */
    public void assignmentSolved(String solvedAssignment) {
        getAssignment(solvedAssignment).ifPresent(a -> solvedAssignments.add(a));
    }

    /**
     * @return did they user solved all solvedAssignments for the lesson?
     */
    public boolean isLessonSolved() {
        return allAssignments.size() == solvedAssignments.size();
    }

    /**
     * Increase the number attempts to solve the lesson
     */
    public void incrementAttempts() {
        numberOfAttempts++;
    }

    /**
     * Reset the tracker. We do not reset the number of attempts here!
     */
    void reset() {
        solvedAssignments.clear();
    }

    /**
     * @return list containing all the assignments solved or not
     */
    public Map<Assignment, Boolean> getLessonOverview() {
        List<Assignment> notSolved = allAssignments.stream()
                .filter(i -> !solvedAssignments.contains(i))
                .collect(Collectors.toList());
        Map<Assignment, Boolean> overview = notSolved.stream().collect(Collectors.toMap(a -> a, b -> false));
        overview.putAll(solvedAssignments.stream().collect(Collectors.toMap(a -> a, b -> true)));
        return overview;
    }
}
