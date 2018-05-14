package org.owasp.webgoat.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.LessonInfoModel;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.LessonTracker;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>LessonProgressService class.</p>
 *
 * @author webgoat
 */
@Controller
@AllArgsConstructor
public class LessonProgressService {

    private UserTrackerRepository userTrackerRepository;
    private WebSession webSession;

    /**
     * <p>LessonProgressService.</p>
     *
     * @return a {@link LessonInfoModel} object.
     */
    @RequestMapping(value = "/service/lessonprogress.mvc", produces = "application/json")
    @ResponseBody
    public Map getLessonInfo() {
        Map json = Maps.newHashMap();
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        if (webSession.getCurrentLesson() != null) {
            LessonTracker lessonTracker = userTracker.getLessonTracker(webSession.getCurrentLesson());
            String successMessage = "";
            boolean lessonCompleted = false;
            if (lessonTracker != null) {
                lessonCompleted = lessonTracker.isLessonSolved();
                successMessage = "LessonCompleted"; //@todo we still use this??
            }
            json.put("lessonCompleted", lessonCompleted);
            json.put("successMessage", successMessage);
        }
        return json;
    }

    /**
     * Endpoint for fetching the complete lesson overview which informs the user about whether all the assignments are solved.
     * Used as the last page of the lesson to generate a lesson overview.
     *
     * @return list of assignments
     */
    @RequestMapping(value = "/service/lessonoverview.mvc", produces = "application/json")
    @ResponseBody
    public List<LessonOverview> lessonOverview() {
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        AbstractLesson currentLesson = webSession.getCurrentLesson();
        List<LessonOverview> result = Lists.newArrayList();
        if ( currentLesson != null ) {
            LessonTracker lessonTracker = userTracker.getLessonTracker(currentLesson);
            result = toJson(lessonTracker.getLessonOverview());
        }
        return result;
    }

    private List<LessonOverview> toJson(Map<Assignment, Boolean> map) {
        ArrayList<LessonOverview> result = Lists.newArrayList();
        for (Map.Entry<Assignment, Boolean> entry : map.entrySet()) {
            result.add(new LessonOverview(entry.getKey(), entry.getValue()));
        }
        return result;
    }


    @AllArgsConstructor
    @Getter
    //Jackson does not really like returning a map of <Assignment, Boolean> directly, see http://stackoverflow.com/questions/11628698/can-we-make-object-as-key-in-map-when-using-json
    //so creating intermediate object is the easiest solution
    private static class LessonOverview {

        private Assignment assignment;
        private Boolean solved;

    }
}
