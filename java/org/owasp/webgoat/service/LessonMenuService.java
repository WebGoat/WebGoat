/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class LessonMenuService extends BaseService {

    /**
     * Returns the lesson menu which is used to build the left nav
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/lessonmenu.mvc", produces = "application/json")
    public @ResponseBody
    List<LessonMenuItem> showLeftNav(HttpSession session) {
        //TODO - need Links, rank, title
        List<LessonMenuItem> menu = new ArrayList<LessonMenuItem>();
        WebSession ws;
        Object o = session.getAttribute(WebSession.SESSION);
        if (o == null || !(o instanceof WebSession)) {
            return null;
        }
        ws = (WebSession) o;
        AbstractLesson l = ws.getCurrentLesson();
        // Get the categories, these are the main menu items
        Course course = ((Course) session.getAttribute("course"));
        List<Category> categories = course.getCategories();

        for (Category category : categories) {
            LessonMenuItem categoryItem = new LessonMenuItem();
            categoryItem.setName(category.getName());
            categoryItem.setType(LessonMenuItemType.CATEGORY);
            // check for any lessons for this category
            List<AbstractLesson> lessons = ws.getLessons(category);
            for (AbstractLesson lesson : lessons) {
                LessonMenuItem lessonItem = new LessonMenuItem();
                lessonItem.setName(lesson.getTitle());
                lessonItem.setLink(lesson.getLink());
                lessonItem.setType(LessonMenuItemType.LESSON);
                if (lesson.isCompleted(ws)) {
                    lessonItem.setComplete(true);
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
