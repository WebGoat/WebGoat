/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.owasp.webgoat.service;

import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.Hint;
import org.owasp.webgoat.lessons.Lesson;
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
    public List<Hint> getHints() {
        Lesson l = webSession.getCurrentLesson();
        return createAssignmentHints(l);
    }

    private List<Hint> createAssignmentHints(Lesson l) {
        if (l != null) {
            return l.getAssignments().stream()
                    .map(a -> createHint(a))
                    .flatMap(hints -> hints.stream())
                    .collect(toList());
        }
        return List.of();
    }

    private List<Hint> createHint(Assignment a) {
        return a.getHints().stream().map(h -> new Hint(h, a.getPath())).collect(toList());
    }
}
