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

import javax.servlet.http.HttpServletRequest;
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
@AssignmentPath("/SqlInjection/attack5b")
@AssignmentHints(value = {"SqlStringInjectionHint1", "SqlStringInjectionHint2", "SqlStringInjectionHint3", "SqlStringInjectionHint4"})
public class SqlInjectionLesson5b extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String userid, HttpServletRequest request) throws IOException {
        return injectableQuery(userid);

    }

    protected AttackResult injectableQuery(String accountName) {
        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());
            String query = "SELECT * FROM user_data WHERE userid = " + accountName;

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);

                if ((results != null) && (results.first() == true)) {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    StringBuffer output = new StringBuffer();

                    output.append(SqlInjectionLesson5a.writeTable(results, resultsMetaData));
                    results.last();

                    // If they get back more than one user they succeeded
                    if (results.getRow() >= 6) {
                        return trackProgress(success().feedback("sql-injection.5b.success").feedbackArgs(output.toString()).build());
                    } else {
                        return trackProgress(failed().output(output.toString()).build());
                    }

                } else {
                    return trackProgress(failed().feedback("sql-injection.5b.no.results").build());

//                    output.append(getLabelManager().get("NoResultsMatched"));
                }
            } catch (SQLException sqle) {

                return trackProgress(failed().output(sqle.getMessage()).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return trackProgress(failed().output(this.getClass().getName() + " : " + e.getMessage()).build());
        }
    }
}
