/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.service;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.model.Hint;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class HintService extends BaseService {

    /**
     * Returns hints for current lesson
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/hint.mvc", produces = "application/json")
    public @ResponseBody
    List<Hint> showHint(HttpSession session) {
        List<Hint> listHints = new ArrayList<Hint>();
        WebSession ws = getWebSesion(session);
        AbstractLesson l = ws.getCurrentLesson();
        if (l == null) {
            return listHints;
        }
        List<String> hints;
        hints = l.getHintsPublic(ws);
        if (hints == null) {
            return listHints;
        }
        int idx = 0;
        for (String h : hints) {
            Hint hint = new Hint();
            hint.setHint(h);
            hint.setLesson(l.getName());
            hint.setNumber(idx);
            listHints.add(hint);
            idx++;
        }
        return listHints;
    }
}
