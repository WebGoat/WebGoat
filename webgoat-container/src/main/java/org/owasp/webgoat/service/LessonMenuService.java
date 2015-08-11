/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at
 * https://github.com/WebGoat/WebGoat, a repository for free software projects.
 *
 * For details, please see http://webgoat.github.io
 */
package org.owasp.webgoat.service;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.RandomLessonAdapter;
import org.owasp.webgoat.lessons.model.LessonMenuItem;
import org.owasp.webgoat.lessons.model.LessonMenuItemType;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class LessonMenuService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(LessonMenuService.class);

    /**
     * Returns the lesson menu which is used to build the left nav
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/lessonmenu.mvc", produces = "application/json")
    public @ResponseBody
    List<LessonMenuItem> showLeftNav(HttpSession session) {
        List<LessonMenuItem> menu = new ArrayList<LessonMenuItem>();
        WebSession ws = getWebSession(session);
        // Get the categories, these are the main menu items
        Course course = ws.getCourse();
        List<Category> categories = course.getCategories();

        for (Category category : categories) {
            LessonMenuItem categoryItem = new LessonMenuItem();
            categoryItem.setName(category.getName());
            categoryItem.setType(LessonMenuItemType.CATEGORY);
            // check for any lessons for this category
            List<AbstractLesson> lessons = ws.getLessons(category);
            String role = ws.getRole();
            logger.info("Role: " + role);
            for (AbstractLesson lesson : lessons) {
                LessonMenuItem lessonItem = new LessonMenuItem();
                lessonItem.setName(lesson.getTitle());
                lessonItem.setLink(lesson.getLink());
                lessonItem.setType(LessonMenuItemType.LESSON);
                if (lesson.isCompleted(ws)) {
                    lessonItem.setComplete(true);
                }
                /* @TODO - do this in a more efficient way 
                 if (lesson.isAuthorized(ws, role, WebSession.SHOWHINTS)) {
                 lessonItem.setShowHints(true);
                 }

                 if (lesson.isAuthorized(ws, role, WebSession.SHOWSOURCE)) {
                 lessonItem.setShowSource(true);
                 }
                 */
                // special handling for challenge role
                if (Category.CHALLENGE.equals(lesson.getCategory())) {
                    lessonItem.setShowHints(lesson.isAuthorized(ws, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS));
                    lessonItem.setShowSource(lesson.isAuthorized(ws, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS));
                }

                categoryItem.addChild(lessonItem);
                // Does the lesson have stages
                if (lesson instanceof RandomLessonAdapter) {
                    RandomLessonAdapter rla = (RandomLessonAdapter) lesson;
                    String[] stages = rla.getStages();
                    if (stages != null) {
                        String lessonLink = lesson.getLink();
                        int stageIdx = 1;
                        for (String stage : stages) {
                            LessonMenuItem stageItem = new LessonMenuItem();
                            stageItem.setName("Stage " + stageIdx + ": " + stage);
                            // build the link for the stage
                            String stageLink = lessonLink + "&stage=" + stageIdx;
                            stageItem.setLink(stageLink);
                            stageItem.setType(LessonMenuItemType.STAGE);
                            if (rla.isStageComplete(ws, stage)) {
                                stageItem.setComplete(true);
                            }
                            lessonItem.addChild(stageItem);
                            stageIdx++;
                        }
                    }
                }
            }
            menu.add(categoryItem);
        }
        return menu;

    }
}
