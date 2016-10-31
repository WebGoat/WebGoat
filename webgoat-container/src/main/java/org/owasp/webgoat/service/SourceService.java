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
 */
package org.owasp.webgoat.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * <p>SourceService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
//TODO REMOVE!
public class SourceService {

    /**
     * Description of the Field
     */
    public final static String START_SOURCE_SKIP = "START_OMIT_SOURCE";

    /** Constant <code>END_SOURCE_SKIP="END_OMIT_SOURCE"</code> */
    public final static String END_SOURCE_SKIP = "END_OMIT_SOURCE";

    /**
     * Returns source for current attack
     *
     * @param session a {@link javax.servlet.http.HttpSession} object.
     * @return a {@link java.lang.String} object.
     */
    @RequestMapping(path = "/service/source.mvc", produces = "application/text")
    public
    @ResponseBody
    String showSource(HttpSession session) {
        //// TODO: 11/6/2016 to decide not sure about the role in WebGoat 8
        String source = getSource();
        if (source == null) {
            source = "No source listing found";
        }
        return StringEscapeUtils.escapeHtml4(source);
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    protected String getSource() {
        return "Source code is not available for this lesson.";
    }
}
