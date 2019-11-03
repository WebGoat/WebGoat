/**
 * ************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since October 28, 2003
 */

package org.owasp.webgoat.controller;

import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@Controller
public class StartLesson {

    private final WebSession ws;
    private final Course course;

    public StartLesson(final WebSession ws, final Course course) {
        this.ws = ws;
        this.course = course;
    }

    /**
     * <p>start.</p>
     *
     * @return a {@link ModelAndView} object.
     */
    @RequestMapping(path = "startlesson.mvc", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView start() {
        ModelAndView model = new ModelAndView();

        model.addObject("course", course);
        model.addObject("lesson", ws.getCurrentLesson());
        model.setViewName("lesson_content");
        return model;
    }

    @RequestMapping(value = {"*.lesson"}, produces = "text/html")
    public ModelAndView lessonPage(HttpServletRequest request) {
        // I will set here the thymeleaf fragment location based on the resource requested.
        ModelAndView model = new ModelAndView();
        SecurityContext context = SecurityContextHolder.getContext(); //TODO this should work with the security roles of Spring
        //GrantedAuthority authority = context.getAuthentication().getAuthorities().iterator().next();
        String path = request.getRequestURL().toString(); // we now got /a/b/c/AccessControlMatrix.lesson
        String lessonName = path.substring(path.lastIndexOf('/') + 1, path.indexOf(".lesson"));
        List<? extends Lesson> lessons = course.getLessons();
        Optional<? extends Lesson> lesson = lessons.stream()
                .filter(l -> l.getId().equals(lessonName))
                .findFirst();
        ws.setCurrentLesson(lesson.get());
        model.setViewName("lesson_content");
        model.addObject("lesson", lesson.get());
        return model;
    }

}
