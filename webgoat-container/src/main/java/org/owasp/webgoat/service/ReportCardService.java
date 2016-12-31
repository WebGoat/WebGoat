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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import org.apache.catalina.User;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ReportCardService</p>
 *
 * @author nbaars
 * @version $Id: $Id
 */
@Controller
public class ReportCardService {

    private final UserTracker userTracker;
    private final Course course;

    public ReportCardService(UserTracker userTracker, Course course) {
        this.userTracker = userTracker;
        this.course = course;
    }

    /**
     * Endpoint which generates the report card for the current use to show the stats on the solved lessons
     */
    @GetMapping(path = "/service/reportcard.mvc", produces = "application/json")
    @ResponseBody
    public ReportCard reportCard() {
        List<AbstractLesson> lessons = course.getLessons();
        ReportCard reportCard = new ReportCard();
        reportCard.setTotalNumberOfLessons(course.getTotalOfLessons());
        reportCard.setTotalNumberOfAssignments(course.getTotalOfAssignments());
        reportCard.setNumberOfAssignmentsSolved(userTracker.numberOfAssignmentsSolved());
        reportCard.setNumberOfLessonsSolved(userTracker.numberOfLessonsSolved());
        for (AbstractLesson lesson : lessons) {
            LessonTracker lessonTracker = userTracker.getLessonTracker(lesson);
            LessonStatistics lessonStatistics = new LessonStatistics();
            lessonStatistics.setName(lesson.getTitle());
            lessonStatistics.setNumberOfAttempts(lessonTracker.getNumberOfAttempts());
            lessonStatistics.setSolved(lessonTracker.isLessonSolved());
            reportCard.lessonStatistics.add(lessonStatistics);
        }
        return reportCard;
    }

    @Getter
    @Setter
    private class ReportCard {

        private int totalNumberOfLessons;
        private int totalNumberOfAssignments;
        private int solvedLessons;
        private int numberOfAssignmentsSolved;
        private int numberOfLessonsSolved;
        private List<LessonStatistics> lessonStatistics = Lists.newArrayList();
    }

    @Setter
    @Getter
    private class LessonStatistics {
        private String name;
        private boolean solved;
        private int numberOfAttempts;
    }
}
