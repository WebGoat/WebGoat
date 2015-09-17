/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Welcome class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class Welcome {
    
    final Logger logger = LoggerFactory.getLogger(Welcome.class);
    private static final String WELCOMED = "welcomed";
    
    /**
     * <p>welcome.</p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @param error a {@link java.lang.String} object.
     * @param logout a {@link java.lang.String} object.
     * @return a {@link org.springframework.web.servlet.ModelAndView} object.
     */
    @RequestMapping(value = "welcome.mvc", method = RequestMethod.GET)
    public ModelAndView welcome(HttpServletRequest request,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        // set the welcome attribute
        // this is so the attack servlet does not also 
        // send them to the welcome page
        HttpSession session = request.getSession();
        if (session.getAttribute(WELCOMED) == null) {
            session.setAttribute(WELCOMED, "true");
        }
        
        //go ahead and send them to webgoat (skip the welcome page)
        ModelAndView model = new ModelAndView();
        //model.setViewName("welcome");
        //model.setViewName("main_new");
        model.setViewName("forward:/attack?start=true");
        return model;
    }
    
}
