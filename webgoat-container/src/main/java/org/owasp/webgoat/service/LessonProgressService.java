package org.owasp.webgoat.service;

import com.google.common.collect.Maps;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.RandomLessonAdapter;
import org.owasp.webgoat.lessons.model.LessonInfoModel;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.LabelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
/**
 * <p>LessonProgressService class.</p>
 *
 * @author webgoat
 */
public class LessonProgressService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(LessonMenuService.class);
    private LabelManager labelManager;

    @Autowired
    public LessonProgressService(final LabelManager labelManager) {
        this.labelManager = labelManager;
    }

    /**
     * <p>LessonProgressService.</p>
     *
     * @param session a {@link HttpSession} object.
     * @return a {@link LessonInfoModel} object.
     */
    @RequestMapping(value = "/lessonprogress.mvc", produces = "application/json")
    @ResponseBody
    public Map getLessonInfo(HttpSession session) {
        WebSession webSession = getWebSession(session);
        AbstractLesson lesson = webSession.getCurrentLesson();
        boolean lessonCompleted = lesson.isCompleted(webSession);
        String successMessage = lesson instanceof RandomLessonAdapter ? "Congratulations, you have completed this lab" : labelManager
                .get("LessonCompleted");
        Map json = Maps.newHashMap();
        json.put("lessonCompleted", lessonCompleted);
        json.put("successMessage", successMessage);
        return json;
    }
}
