/**
 *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author WebGoat
 * @since October 28, 2003
 * @version $Id: $Id
 */
package org.owasp.webgoat.controller;

import org.owasp.webgoat.lessons.RandomLessonAdapter;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Controller
public class StartLesson {

    //simple filter can be removed after ECS removal
    private static final String refactored = "ClientSideFiltering AccessControlMatrix";


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
        model.addObject("migrated", refactored.contains(ws.getCurrentLesson().getClass().getSimpleName())); //remove after ECS removal otherwise you will see the lesson twice
        model.setViewName("lesson_content");
        return model;
    }
}
