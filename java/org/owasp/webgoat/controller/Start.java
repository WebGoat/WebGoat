/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rlawson
 */
@Controller
public class Start {

    final Logger logger = LoggerFactory.getLogger(Start.class);

    private static final String WELCOMED = "welcomed";

    @RequestMapping(value = "start.mvc", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView start(HttpServletRequest request,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        // make sure session is set up correctly
        // if not redirect user to login
        if (checkWebSession(request.getSession()) == false) {
            model.setViewName("redirect:/login.mvc");
            return model;
        }

        // if everything ok then go to webgoat UI
        model.setViewName("main_new");
        return model;
    }

    public boolean checkWebSession(HttpSession session) {
        Object o = session.getAttribute(WebSession.SESSION);
        if (o == null) {
            logger.error("No valid WebSession object found, has session timed out? [" + session.getId() + "]");
            return false;
        }
        if (!(o instanceof WebSession)) {
            logger.error("Invalid WebSession object found, this is probably a bug! [" + o.getClass() + " | " + session.getId() + "]");
            return false;
        }
        return true;
    }
}
