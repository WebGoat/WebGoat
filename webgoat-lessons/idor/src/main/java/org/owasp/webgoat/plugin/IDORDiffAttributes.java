package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * ************************************************************************************************
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
 * <p>
 *
 * @author misfir3
 * @version $Id: $Id
 * @since January 3, 2017
 */

@AssignmentPath("IDOR/diff-attributes")
@AssignmentHints({"idor.hints.idorDiffAttributes1","idor.hints.idorDiffAttributes2","idor.hints.idorDiffAttributes3"})
public class IDORDiffAttributes extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam String attributes, HttpServletRequest request) throws IOException {
        attributes = attributes.trim();
        String[] diffAttribs = attributes.split(",");
        if (diffAttribs.length < 2) {
            return trackProgress(failed().feedback("idor.diff.attributes.missing").build());
        }
        if (diffAttribs[0].toLowerCase().trim().equals("userid") && diffAttribs[1].toLowerCase().trim().equals("role") ||
                diffAttribs[1].toLowerCase().trim().equals("userid") && diffAttribs[0].toLowerCase().trim().equals("role")) {
            return trackProgress(success().feedback("idor.diff.success").build());
        } else {
            return trackProgress(failed().feedback("idor.diff.failure").build());
        }
    }
}
