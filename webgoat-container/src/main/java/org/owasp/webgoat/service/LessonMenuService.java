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
import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonMenuItem;
import org.owasp.webgoat.lessons.LessonMenuItemType;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.LessonTracker;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>LessonMenuService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
@AllArgsConstructor
public class LessonMenuService {

    public static final String URL_LESSONMENU_MVC = "/service/lessonmenu.mvc";
    private final Course course;
    private final WebSession webSession;
    private UserTrackerRepository userTrackerRepository;

    /**
     * Returns the lesson menu which is used to build the left nav
     *
     * @return a {@link java.util.List} object.
     */
    @RequestMapping(path = URL_LESSONMENU_MVC, produces = "application/json")
    public
    @ResponseBody
    List<LessonMenuItem> showLeftNav() {
        List<LessonMenuItem> menu = new ArrayList<>();
        List<Category> categories = course.getCategories();
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());

        for (Category category : categories) {
            LessonMenuItem categoryItem = new LessonMenuItem();
            categoryItem.setName(category.getName());
            categoryItem.setType(LessonMenuItemType.CATEGORY);
            // check for any lessons for this category
            List<Lesson> lessons = course.getLessons(category);
            lessons = lessons.stream().sorted(Comparator.comparing(l -> l.getTitle())).collect(Collectors.toList());
            for (Lesson lesson : lessons) {
                LessonMenuItem lessonItem = new LessonMenuItem();
                lessonItem.setName(lesson.getTitle());
                lessonItem.setLink(lesson.getLink());
                lessonItem.setType(LessonMenuItemType.LESSON);
                LessonTracker lessonTracker = userTracker.getLessonTracker(lesson);
                boolean lessonSolved = lessonCompleted(lessonTracker.getLessonOverview(), lesson);
                lessonItem.setComplete(lessonSolved);
                categoryItem.addChild(lessonItem);
            }
            categoryItem.getChildren().sort((o1, o2) -> o1.getRanking() - o2.getRanking());
            menu.add(categoryItem);
        }
        return menu;

    }

    private boolean lessonCompleted(Map<Assignment, Boolean> map, Lesson currentLesson) {
        boolean result = true;
        for (Map.Entry<Assignment, Boolean> entry : map.entrySet()) {
        	    Assignment storedAssignment = entry.getKey();
            	for (Assignment lessonAssignment: currentLesson.getAssignments()) {
            		if (lessonAssignment.getName().equals(storedAssignment.getName())) {
            			result = result && entry.getValue();
            			break;
            		}
            	}
            
        }
        return result;
    }
}
