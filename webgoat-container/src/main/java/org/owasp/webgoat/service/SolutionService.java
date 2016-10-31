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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>SolutionService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class SolutionService {

    /**
     * Returns solution for current attack
     *
     * @return a {@link java.lang.String} object.
     */
    @RequestMapping(path = "/service/solution.mvc", produces = "text/html")
    public
    @ResponseBody
    String showSolution() {
        //// TODO: 11/6/2016 to decide not sure about the role in WebGoat 8
        String source = getSolution();
        return source;
    }

    /**
     * <p>getSolution.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    protected String getSolution() {
        return "Solution  is not available";
    }
}
