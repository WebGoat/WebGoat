package org.owasp.webgoat.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.owasp.webgoat.lessons.Lesson;
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
import java.util.HashMap;
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
     * Endpoint for fetching the complete lesson overview which informs the user about whether all the assignments are solved.
     * Used as the last page of the lesson to generate a lesson overview.
     *
     * @return list of assignments
     */
    @RequestMapping(value = "/service/lessonoverview.mvc", produces = "application/json")
    @ResponseBody
    public List<LessonOverview> lessonOverview() {
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        Lesson currentLesson = webSession.getCurrentLesson();
        List<LessonOverview> result = new ArrayList<>();
        if (currentLesson != null) {
            LessonTracker lessonTracker = userTracker.getLessonTracker(currentLesson);
            result = toJson(lessonTracker.getLessonOverview(), currentLesson);
        }
        return result;
    }

    private List<LessonOverview> toJson(Map<Assignment, Boolean> map, Lesson currentLesson) {
        List<LessonOverview> result = new ArrayList();
        for (Map.Entry<Assignment, Boolean> entry : map.entrySet()) {
            Assignment storedAssignment = entry.getKey();
            for (Assignment lessonAssignment : currentLesson.getAssignments()) {
                if (lessonAssignment.getName().equals(storedAssignment.getName())
                        && !lessonAssignment.getPath().equals(storedAssignment.getPath())) {
                    //here a stored path in the assignments table will be corrected for the JSON output
                    //with the value of the actual expected path
                    storedAssignment.setPath(lessonAssignment.getPath());
                    result.add(new LessonOverview(storedAssignment, entry.getValue()));
                    break;

                } else if (lessonAssignment.getName().equals(storedAssignment.getName())) {
                    result.add(new LessonOverview(storedAssignment, entry.getValue()));
                    break;
                }
            }
            //assignments not in the list will not be put in the lesson progress JSON output

        }
        return result;
    }

    private boolean isLessonComplete(Map<Assignment, Boolean> map, Lesson currentLesson) {
        boolean result = true;
        for (Map.Entry<Assignment, Boolean> entry : map.entrySet()) {
            Assignment storedAssignment = entry.getKey();
            for (Assignment lessonAssignment : currentLesson.getAssignments()) {
                if (lessonAssignment.getName().equals(storedAssignment.getName())) {
                    result = result && entry.getValue();
                    break;
                }
            }

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
