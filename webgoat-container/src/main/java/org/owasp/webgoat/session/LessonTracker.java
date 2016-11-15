
package org.owasp.webgoat.session;

import com.google.common.collect.Sets;
import org.owasp.webgoat.lessons.AbstractLesson;

import java.io.Serializable;
import java.util.List;
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
public class LessonTracker implements Serializable {
    private static final long serialVersionUID = 5410058267505412928L;
    private final List<String> assignments;
    private final Set<String> solvedAssignments = Sets.newHashSet();
    private int numberOfAttempts = 0;

    public LessonTracker(AbstractLesson lesson) {
        this.assignments = lesson.getAssignments().stream().map(a -> a.getSimpleName()).collect(Collectors.toList());
    }

    /**
     * Mark an assingment as solved
     *
     * @param solvedAssignment the assignment which the user solved
     */
    public void assignmentSolved(String solvedAssignment) {
        solvedAssignments.add(solvedAssignment);
    }

    /**
     * @return did they user solved all assignments for the lesson?
     */
    public boolean isLessonSolved() {
        return solvedAssignments.size() == assignments.size();
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
    public void reset() {
        solvedAssignments.clear();
    }
}
