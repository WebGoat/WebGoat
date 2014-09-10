/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * For details, please see http://webgoat.github.io
 */
package org.owasp.webgoat.service;

import javax.servlet.http.HttpSession;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class SolutionService extends BaseService {

    /**
     * Returns solution for current attack
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/solution.mvc", produces = "text/html")
    public @ResponseBody
    String showSolution(HttpSession session) {
        WebSession ws = getWebSession(session);
        String source = getSolution(ws);
        return source;
    }

    protected String getSolution(WebSession s) {

        String source = null;
        int scr = s.getCurrentScreen();
        Course course = s.getCourse();

        if (s.isUser() || s.isChallenge()) {

            AbstractLesson lesson = course.getLesson(s, scr, AbstractLesson.USER_ROLE);

            if (lesson != null) {
                source = lesson.getSolution(s);
            }
        }
        if (source == null) {
            return "Solution  is not available. Contact "
                    + s.getWebgoatContext().getFeedbackAddressHTML();
        }
        return (source);
    }
}
