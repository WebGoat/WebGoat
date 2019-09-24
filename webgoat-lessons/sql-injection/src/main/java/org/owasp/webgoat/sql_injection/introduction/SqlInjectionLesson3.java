
/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.sql_injection.introduction;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.springframework.web.bind.annotation.*;

import java.sql.*;


@RestController
@AssignmentHints(value = {"SqlStringInjectionHint3-1", "SqlStringInjectionHint3-2"})
public class SqlInjectionLesson3 extends AssignmentEndpoint {

    @PostMapping("/SqlInjection/attack3")
    @ResponseBody
    public AttackResult completed(@RequestParam String query) {
        return injectableQuery(query);
    }

    protected AttackResult injectableQuery(String _query) {
        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());
            String query = _query;

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                Statement check_statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                statement.executeUpdate(_query);
                ResultSet _results = check_statement.executeQuery("SELECT * FROM employees WHERE last_name='Barnett';");
                StringBuffer output = new StringBuffer();
                // user completes lesson if the department of Tobi Barnett now is 'Sales'
                _results.first();
                if (_results.getString("department").equals("Sales")) {
                    output.append("<span class='feedback-positive'>" + _query + "</span>");
                    output.append(SqlInjectionLesson8.generateTable(_results));
                    return trackProgress(success().output(output.toString()).build());
                } else {
                    return trackProgress(failed().output(output.toString()).build());
                }

            } catch (SQLException sqle) {

                return trackProgress(failed().output(sqle.getMessage()).build());
            }
        } catch (Exception e) {
            return trackProgress(failed().output(this.getClass().getName() + " : " + e.getMessage()).build());
        }
    }
}
