package org.owasp.webgoat.container.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.session.WebSession;
import org.owasp.webgoat.container.users.UserTrackerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * <p>LessonProgressService class.</p>
 *
 * @author webgoat
 */
@Controller
@RequiredArgsConstructor
public class LessonProgressService {

    private final UserTrackerRepository userTrackerRepository;
    private final WebSession webSession;

    /**
     * Endpoint for fetching the complete lesson overview which informs the user about whether all the assignments are solved.
     * Used as the last page of the lesson to generate a lesson overview.
     *
     * @return list of assignments
     */
    @RequestMapping(value = "/service/lessonoverview.mvc", produces = "application/json")
    @ResponseBody
    public List<LessonOverview> lessonOverview() {
        var userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        var currentLesson = webSession.getCurrentLesson();

        if (currentLesson != null) {
            var lessonTracker = userTracker.getLessonTracker(currentLesson);
            return lessonTracker.getLessonOverview().entrySet().stream()
                    .map(entry -> new LessonOverview(entry.getKey(), entry.getValue()))
                    .toList();
        }
        return List.of();
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
