package org.owasp.webgoat.service;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.owasp.webgoat.i18n.LabelManager;
import org.owasp.webgoat.lessons.model.LessonInfoModel;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.UserTracker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * <p>LessonProgressService class.</p>
 *
 * @author webgoat
 */
@Controller
@AllArgsConstructor
public class LessonProgressService {

    private LabelManager labelManager;
    private UserTracker userTracker;

    /**
     * <p>LessonProgressService.</p>
     *
     * @return a {@link LessonInfoModel} object.
     */
    @RequestMapping(value = "/service/lessonprogress.mvc", produces = "application/json")
    @ResponseBody
    public Map getLessonInfo() {
        LessonTracker lessonTracker = userTracker.getCurrentLessonTracker();
        boolean lessonCompleted = lessonTracker.getCompleted();
        String successMessage = labelManager.get("LessonCompleted");
        Map json = Maps.newHashMap();
        json.put("lessonCompleted", lessonCompleted);
        json.put("successMessage", successMessage);
        return json;
    }
}
