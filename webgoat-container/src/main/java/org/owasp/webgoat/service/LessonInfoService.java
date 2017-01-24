package org.owasp.webgoat.service;

import org.owasp.webgoat.i18n.LabelManager;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.LessonInfoModel;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>LessonInfoService class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
@RestController
public class LessonInfoService {

    private final WebSession webSession;
    private final LabelManager labelManager;

    public LessonInfoService(WebSession webSession, LabelManager labelManager) {
        this.webSession = webSession;
        this.labelManager = labelManager;
    }

    /**
     * <p>getLessonInfo.</p>
     *
     * @return a {@link LessonInfoModel} object.
     */
    @RequestMapping(path = "/service/lessoninfo.mvc", produces = "application/json")
    public @ResponseBody
    LessonInfoModel getLessonInfo() {
        AbstractLesson lesson = webSession.getCurrentLesson();
        return new LessonInfoModel(labelManager.get(lesson.getTitle()), false, false, false);
    }

}
