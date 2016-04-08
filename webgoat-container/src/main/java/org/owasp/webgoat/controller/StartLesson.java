/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.controller;

import org.owasp.webgoat.lessons.RandomLessonAdapter;
import org.owasp.webgoat.session.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Start class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class StartLesson {

    final Logger logger = LoggerFactory.getLogger(StartLesson.class);

    @Autowired
    private ServletContext servletContext;

    /**
     * <p>start.</p>
     *
     * @param request a {@link HttpServletRequest} object.
     * @return a {@link ModelAndView} object.
     */
    @RequestMapping(path = "startlesson.mvc", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView start(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        WebSession ws = (WebSession) request.getSession().getAttribute(WebSession.SESSION);
        model.addObject("has_stages", ws.getCurrentLesson() instanceof RandomLessonAdapter);
        model.addObject("course", ws.getCourse());
        model.addObject("lesson", ws.getCurrentLesson());
        model.addObject("message", ws.getMessage());
        model.addObject("instructions", ws.getInstructions());
        model.setViewName("lesson_content");
        return model;
    }
}
