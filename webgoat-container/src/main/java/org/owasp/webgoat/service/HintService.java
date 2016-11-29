/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.service;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Hint;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>HintService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class HintService {

    private final WebSession webSession;

    public HintService(WebSession webSession) {
        this.webSession = webSession;
    }

    /**
     * Returns hints for current lesson
     *
     * @return a {@link java.util.List} object.
     */
    @RequestMapping(path = "/service/hint.mvc", produces = "application/json")
    public
    @ResponseBody
    List<Hint> showHint() {
        List<Hint> listHints = new ArrayList<Hint>();
        AbstractLesson l = webSession.getCurrentLesson();
        if (l == null) {
            return listHints;
        }
        List<String> hints = l.getHints();

        if (hints == null) {
            return listHints;
        }

        int idx = 0;
        return hints.stream().map(h -> createHint(h, l.getName(), idx)).collect(toList());
    }

    private Hint createHint(String hintText, String lesson, int idx) {
        Hint hint = new Hint();
        hint.setHint(hintText);
        hint.setLesson(lesson);
        hint.setNumber(idx);
        return hint;
    }
}
