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

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.owasp.webgoat.session.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author rlawson
 */
@RequestMapping("/service")
public abstract class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    public @ResponseBody
    ExceptionInfo handleException(HttpServletRequest request, Exception ex) {
        String url = request.getRequestURL().toString();
        logger.error("Exception handler for service caught exception when processing: " + url, ex);
        ExceptionInfo response = new ExceptionInfo();
        response.setUrl(url);
        
        response.setMessage(getStringStackTrace(ex));

        return response;
    }

    public WebSession getWebSession(HttpSession session) {
        WebSession ws;
        Object o = session.getAttribute(WebSession.SESSION);
        if (o == null) {
            throw new IllegalArgumentException("No valid WebSession object found, has session timed out? [" + session.getId() + "]");
        }
        if (!(o instanceof WebSession)) {
            throw new IllegalArgumentException("Invalid WebSession object found, this is probably a bug! [" + o.getClass() + " | " + session.getId() + "]");
        }
        ws = (WebSession) o;
        return ws;
    }

    public String getStringStackTrace(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
