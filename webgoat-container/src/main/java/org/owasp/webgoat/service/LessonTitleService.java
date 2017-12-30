package org.owasp.webgoat.service;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p>LessonTitleService class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
@Controller
public class LessonTitleService {

    private final WebSession webSession;

    public LessonTitleService(final WebSession webSession) {
        this.webSession = webSession;
    }

    /**
     * Returns the title for the current attack
     *
     * @return a {@link java.lang.String} object.
     */
    @RequestMapping(path = "/service/lessontitle.mvc", produces = "application/html")
    public
    @ResponseBody
    String showPlan() {
        AbstractLesson lesson = webSession.getCurrentLesson();
        return lesson != null ? lesson.getTitle() : "";
    }

}
