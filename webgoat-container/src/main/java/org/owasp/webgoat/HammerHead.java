package org.owasp.webgoat;

import lombok.AllArgsConstructor;
import org.owasp.webgoat.session.Course;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * @author Jeff Williams
 * @author Bruce Mayhew
 * @author Nanne Baars
 * @version $Id: $Id
 * @since October 28, 2003
 */
@Controller
@AllArgsConstructor
public class HammerHead {

    private final Course course;

    /**
     * Entry point for WebGoat, redirects to the first lesson found within the course.
     */
    @RequestMapping(path = "/attack", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView attack(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("redirect:" + "start.mvc" + course.getFirstLesson().getLink());
    }
}
