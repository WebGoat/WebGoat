
package org.owasp.webgoat.session;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.SerializationUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
@Slf4j
public class UserTracker {

    private final String webgoatHome;
    private final String user;
    private Map<String, LessonTracker> storage = new HashMap<>();

    public UserTracker(final String webgoatHome, final String user) {
        this.webgoatHome = webgoatHome;
        this.user = user;
    }

    /**
     * Returns the lesson tracker for a specific lesson if available.
     *
     * @param lesson the lesson
     * @return the optional lesson tracker
     */
    public LessonTracker getLessonTracker(AbstractLesson lesson) {
        LessonTracker lessonTracker = storage.get(lesson.getTitle());
        if (lessonTracker == null) {
            lessonTracker = new LessonTracker(lesson);
            storage.put(lesson.getTitle(), lessonTracker);
        }
        return lessonTracker;
    }

    public void assignmentSolved(AbstractLesson lesson, String assignmentName) {
        LessonTracker lessonTracker = getLessonTracker(lesson);
        lessonTracker.incrementAttempts();
        lessonTracker.assignmentSolved(assignmentName);
        save();
    }

    public void assignmentFailed(AbstractLesson lesson) {
        LessonTracker lessonTracker = getLessonTracker(lesson);
        lessonTracker.incrementAttempts();
        save();
    }

    public void load() {
        File file = new File(webgoatHome, user + ".progress");
        if (file.exists() && file.isFile()) {
            try {
                this.storage = (Map<String, LessonTracker>) SerializationUtils.deserialize(FileCopyUtils.copyToByteArray(file));
            } catch (Exception e) {
                log.error("Unable to read the progress file, creating a new one...");
                this.storage = Maps.newHashMap();
            }
        }
    }

    @SneakyThrows
    private void save() {
        File file = new File(webgoatHome, user + ".progress");
        FileCopyUtils.copy(SerializationUtils.serialize(this.storage), file);
    }


    public void reset(AbstractLesson al) {
        getLessonTracker(al).reset();
        save();
    }

    public int numberOfLessonsSolved() {
        int numberOfLessonsSolved = 0;
        for (LessonTracker lessonTracker : storage.values()) {
            if (lessonTracker.isLessonSolved()) {
                numberOfLessonsSolved = numberOfLessonsSolved + 1;
            }
        }
        return numberOfLessonsSolved;
    }

    public int numberOfAssignmentsSolved() {
        int numberOfAssignmentsSolved = 0;
        for (LessonTracker lessonTracker : storage.values()) {
            Map<Assignment, Boolean> lessonOverview = lessonTracker.getLessonOverview();
            numberOfAssignmentsSolved = lessonOverview.values().stream().filter(b -> b).collect(Collectors.counting()).intValue();
        }
        return numberOfAssignmentsSolved;
    }
}
