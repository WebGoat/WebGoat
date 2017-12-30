/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.service;

import com.google.common.collect.Lists;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.Hint;
import org.owasp.webgoat.session.WebSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>HintService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@RestController
public class HintService {

    public static final String URL_HINTS_MVC = "/service/hint.mvc";
    private final WebSession webSession;

    public HintService(WebSession webSession) {
        this.webSession = webSession;
    }

    /**
     * Returns hints for current lesson
     *
     * @return a {@link java.util.List} object.
     */
    @GetMapping(path = URL_HINTS_MVC, produces = "application/json")
    @ResponseBody
    public List<Hint> showHint() {
        AbstractLesson l = webSession.getCurrentLesson();
        List<Hint> hints = createLessonHints(l);
        hints.addAll(createAssignmentHints(l));
        return hints;

    }

    private List<Hint> createLessonHints(AbstractLesson l) {
        if ( l != null ) {
            return l.getHints().stream().map(h -> createHint(h, l.getName(), null)).collect(toList());
        }
        return Lists.newArrayList();
    }

    private List<Hint> createAssignmentHints(AbstractLesson l) {
        List<Hint> hints = Lists.newArrayList();
        if ( l != null) {
            List<Assignment> assignments = l.getAssignments();
            assignments.stream().forEach(a -> { a.getHints(); createHints(a, hints);});
        }
        return hints;
    }

    private void createHints(Assignment a, List<Hint> hints) {
        hints.addAll(a.getHints().stream().map(h -> createHint(h, null, a.getPath())).collect(toList()));
    }

    private Hint createHint(String hintText, String lesson, String assignmentName) {
        Hint hint = new Hint();
        hint.setHint(hintText);
        if (lesson != null) {
            hint.setLesson(lesson);
        } else {
            hint.setAssignmentPath(assignmentName);
        }
        return hint;
    }
}
