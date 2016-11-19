package org.owasp.webgoat.service;

import org.owasp.webgoat.lessons.model.LessonInfoModel;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/**
 * <p>LessonInfoService class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
public class LessonInfoService {

    private final WebSession webSession;

    public LessonInfoService(WebSession webSession) {
        this.webSession = webSession;
    }

    /**
     * <p>getLessonInfo.</p>
     *
     * @return a {@link org.owasp.webgoat.lessons.model.LessonInfoModel} object.
     */
    @RequestMapping(path = "/service/lessoninfo.mvc", produces = "application/json")
    public @ResponseBody
    LessonInfoModel getLessonInfo() {
        return new LessonInfoModel(webSession);
    }

}
