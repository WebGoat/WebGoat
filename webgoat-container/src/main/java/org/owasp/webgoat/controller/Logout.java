/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Logout class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class Logout {

    final Logger logger = LoggerFactory.getLogger(Logout.class);

    /**
     * <p>logout.</p>
     *
     * @param error a {@link java.lang.String} object.
     * @param logout a {@link java.lang.String} object.
     * @return a {@link org.springframework.web.servlet.ModelAndView} object.
     */
    @RequestMapping(value = "logout.mvc", method = RequestMethod.GET)
    public ModelAndView logout(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        
        logger.info("Logging user out");

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("logout");

        return model;

    }
}
