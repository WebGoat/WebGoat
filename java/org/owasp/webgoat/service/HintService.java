/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.service;

import org.owasp.webgoat.lessons.model.Hint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class HintService extends BaseService {
    
    @RequestMapping(value = "/hint.do", produces = "application/json")
    public @ResponseBody
    Hint showHint() {
        Hint h = new Hint();
        h.setHint("This is a test hint");
        h.setLesson("Some lesson");
        h.setNumber(1);
        return h;
    }
}
