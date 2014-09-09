package org.owasp.webgoat.service;

import javax.servlet.http.HttpSession;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LessonTitleService extends BaseService {
	
	 /**
     * Returns the title for the current attack
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/lessontitle.mvc", produces = "application/html")
    public @ResponseBody
    String showPlan(HttpSession session) {
        WebSession ws = getWebSession(session);
        return getLessonTitle(ws);
    }

    private String getLessonTitle(WebSession s) {
    	String title = "";
        int scr = s.getCurrentScreen();
        Course course = s.getCourse();

        if (s.isUser() || s.isChallenge()) {
            AbstractLesson lesson = course.getLesson(s, scr, AbstractLesson.USER_ROLE);
            title = lesson != null ? lesson.getTitle() : "";
        }
        return title;
    }

}
