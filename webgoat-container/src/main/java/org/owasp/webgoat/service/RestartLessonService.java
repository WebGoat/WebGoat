/***************************************************************************************************
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
 */
package org.owasp.webgoat.service;

import org.owasp.webgoat.session.WebSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;

/**
 * <p>RestartLessonService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class RestartLessonService extends BaseService {

    /**
     * Returns current lesson
     *
     * @param session a {@link javax.servlet.http.HttpSession} object.
     */
    @RequestMapping(value = "/restartlesson.mvc")
    @ResponseStatus(value = HttpStatus.OK)
    public void restartLesson(HttpSession session) {
        WebSession ws = getWebSession(session);
        int currentScreen = ws.getCurrentScreen();
        if(currentScreen > 0){
            ws.restartLesson(currentScreen);
        }
    }
}
