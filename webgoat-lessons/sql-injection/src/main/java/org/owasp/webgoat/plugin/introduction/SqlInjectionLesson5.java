
package org.owasp.webgoat.plugin.introduction;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.*;


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
 * For details, please see http://webgoat.github.io
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
@AssignmentPath("/SqlInjection/attack5")
@AssignmentHints(value = {"SqlStringInjectionHint5-a"})
public class SqlInjectionLesson5 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String query) {
        return injectableQuery(query);
    }

    protected AttackResult injectableQuery(String _query) {
        try {
            String query = _query;
            String regex = "(?i)^(grant alter table to unauthorizedUser)(?:[;]?)$";
            Boolean isCorrect = false;
            StringBuffer output = new StringBuffer();

            // user completes lesson if the query is correct
            if (_query.matches(regex)) {
                output.append("<span class='feedback-positive'>" + _query + "</span>");
                return trackProgress(success().output(output.toString()).build());
            } else {
                return trackProgress(failed().output(output.toString()).build());
            }

        } catch (Exception e) {
            return trackProgress(failed().output(this.getClass().getName() + " : " + e.getMessage()).build());
        }
    }
}
