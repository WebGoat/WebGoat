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
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.model.LessonMenuItem;
import org.owasp.webgoat.lessons.model.LessonMenuItemType;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>LessonMenuService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
@AllArgsConstructor
public class LessonMenuService {

    private final Course course;
    private final UserTracker userTracker;
    private final WebSession webSession;

    /**
     * Returns the lesson menu which is used to build the left nav
     *
     * @return a {@link java.util.List} object.
     */
    @RequestMapping(path = "/service/lessonmenu.mvc", produces = "application/json")
    public
    @ResponseBody
    List<LessonMenuItem> showLeftNav() {
        List<LessonMenuItem> menu = new ArrayList<LessonMenuItem>();
        List<Category> categories = course.getCategories();

        for (Category category : categories) {
            LessonMenuItem categoryItem = new LessonMenuItem();
            categoryItem.setName(category.getName());
            categoryItem.setType(LessonMenuItemType.CATEGORY);
            // check for any lessons for this category
            List<AbstractLesson> lessons = course.getLessons(category);
            for (AbstractLesson lesson : lessons) {
                LessonMenuItem lessonItem = new LessonMenuItem();
                lessonItem.setName(lesson.getTitle());
                lessonItem.setLink(lesson.getLink());
                lessonItem.setType(LessonMenuItemType.LESSON);
                Optional<LessonTracker> lessonTracker = userTracker.getLessonTracker(lesson);
                lessonItem.setComplete(lessonTracker.isPresent() ? lessonTracker.get().getCompleted() : false);
                categoryItem.addChild(lessonItem);
            }
            menu.add(categoryItem);
        }
        return menu;

    }
}
