/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.controller;

import java.util.Collection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.application.Application;
import org.owasp.webgoat.session.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Start class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class Start {

    final Logger logger = LoggerFactory.getLogger(Start.class);

    private static final String WELCOMED = "welcomed";

    @Autowired
    private ServletContext servletContext;

    /**
     * <p>start.</p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @param error a {@link java.lang.String} object.
     * @param logout a {@link java.lang.String} object.
     * @return a {@link org.springframework.web.servlet.ModelAndView} object.
     */
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
        String role = getRole();
        String user = request.getUserPrincipal().getName();
        model.addObject("role", role);
        model.addObject("user", user);

        String contactEmail = servletContext.getInitParameter("email");
        model.addObject("contactEmail", contactEmail);
        String emailList = servletContext.getInitParameter("emaillist");
        model.addObject("emailList", emailList);

        Application app = Application.getInstance();
        logger.info("Setting application properties: " + app);
        model.addObject("version", app.getVersion());
        model.addObject("build", app.getBuild());

        // if everything ok then go to webgoat UI
        model.setViewName("main_new");
        return model;
    }

    private String getRole() {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        String role = "N/A";
        for (GrantedAuthority authority : authorities) {
            authority.getAuthority();
            role = authority.getAuthority();
            role = StringUtils.lowerCase(role);
            role = StringUtils.remove(role, "role_");
            break;
        }
        return role;
    }

    /**
     * <p>checkWebSession.</p>
     *
     * @param session a {@link javax.servlet.http.HttpSession} object.
     * @return a boolean.
     */
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
