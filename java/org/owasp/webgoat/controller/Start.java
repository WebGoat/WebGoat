/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.controller;

import javax.servlet.http.HttpServletRequest;
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

    private static final String WELCOMED = "welcomed";

    @RequestMapping(value = "start.mvc", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView start(HttpServletRequest request,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        //@TODO put stuff here the main page needs to access
        ModelAndView model = new ModelAndView();
        model.setViewName("main_new");

        return model;

    }
}
