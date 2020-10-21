/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at
 * https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.owasp.webgoat.i18n.PluginMessages;
import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.LessonTracker;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>ReportCardService</p>
 *
 * @author nbaars
 * @version $Id: $Id
 */
@Controller
@AllArgsConstructor
public class ReportCardService {

    private final WebSession webSession;
    private final UserTrackerRepository userTrackerRepository;
    private final Course course;
    private final PluginMessages pluginMessages;

    /**
     * Endpoint which generates the report card for the current use to show the stats on the solved lessons
     */
    @GetMapping(path = "/service/reportcard.mvc", produces = "application/json")
    @ResponseBody
    public ReportCard reportCard() {
        final ReportCard reportCard = new ReportCard();
        reportCard.setTotalNumberOfLessons(course.getTotalOfLessons());
        reportCard.setTotalNumberOfAssignments(course.getTotalOfAssignments());

        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        reportCard.setNumberOfAssignmentsSolved(userTracker.numberOfAssignmentsSolved());
        reportCard.setNumberOfLessonsSolved(userTracker.numberOfLessonsSolved());
        for (Lesson lesson : course.getLessons()) {
            LessonTracker lessonTracker = userTracker.getLessonTracker(lesson);
            final LessonStatistics lessonStatistics = new LessonStatistics();
            lessonStatistics.setName(pluginMessages.getMessage(lesson.getTitle()));
            lessonStatistics.setNumberOfAttempts(lessonTracker.getNumberOfAttempts());
            lessonStatistics.setSolved(lessonTracker.isLessonSolved());
            reportCard.lessonStatistics.add(lessonStatistics);
        }
        return reportCard;
    }

    @Getter
    @Setter
    private final class ReportCard {

        private int totalNumberOfLessons;
        private int totalNumberOfAssignments;
        private int solvedLessons;
        private int numberOfAssignmentsSolved;
        private int numberOfLessonsSolved;
        private List<LessonStatistics> lessonStatistics =  new ArrayList<>();
    }

    @Setter
    @Getter
    private final class LessonStatistics {
        private String name;
        private boolean solved;
        private int numberOfAttempts;
    }
}
