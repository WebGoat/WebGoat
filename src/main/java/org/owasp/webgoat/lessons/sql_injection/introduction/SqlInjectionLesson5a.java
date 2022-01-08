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

package org.owasp.webgoat.lessons.sql_injection.introduction;

import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;


@RestController
@AssignmentHints(value = {"SqlStringInjectionHint5a1"})
public class SqlInjectionLesson5a extends AssignmentEndpoint {

    private static final String EXPLANATION = "<br> Explanation: This injection works, because <span style=\"font-style: italic\">or '1' = '1'</span> "
            + "always evaluates to true (The string ending literal for '1 is closed by the query itself, so you should not inject it). "
            + "So the injected query basically looks like this: <span style=\"font-style: italic\">SELECT * FROM user_data WHERE first_name = 'John' and last_name = '' or TRUE</span>, "
            + "which will always evaluate to true, no matter what came before it.";
    private final LessonDataSource dataSource;

    public SqlInjectionLesson5a(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/SqlInjection/assignment5a")
    @ResponseBody
    public AttackResult completed(@RequestParam String account, @RequestParam String operator, @RequestParam String injection) {
        return injectableQuery(account + " " + operator + " " + injection);
    }

    protected AttackResult injectableQuery(String accountName) {
        String query = "";
        try (Connection connection = dataSource.getConnection()) {
            query = "SELECT * FROM user_data WHERE first_name = 'John' and last_name = '" + accountName + "'";
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ResultSet results = statement.executeQuery(query);

                if ((results != null) && (results.first())) {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    StringBuilder output = new StringBuilder();

                    output.append(writeTable(results, resultsMetaData));
                    results.last();

                    // If they get back more than one user they succeeded
                    if (results.getRow() >= 6) {
                        return success(this).feedback("sql-injection.5a.success").output("Your query was: " + query + EXPLANATION).feedbackArgs(output.toString()).build();
                    } else {
                        return failed(this).output(output.toString() + "<br> Your query was: " + query).build();
                    }
                } else {
                    return failed(this).feedback("sql-injection.5a.no.results").output("Your query was: " + query).build();
                }
            } catch (SQLException sqle) {
                return failed(this).output(sqle.getMessage() + "<br> Your query was: " + query).build();
            }
        } catch (Exception e) {
            return failed(this).output(this.getClass().getName() + " : " + e.getMessage() + "<br> Your query was: " + query).build();
        }
    }

    public static String writeTable(ResultSet results, ResultSetMetaData resultsMetaData) throws SQLException {
        int numColumns = resultsMetaData.getColumnCount();
        results.beforeFirst();
        StringBuilder t = new StringBuilder();
        t.append("<p>");

        if (results.next()) {
            for (int i = 1; i < (numColumns + 1); i++) {
                t.append(resultsMetaData.getColumnName(i));
                t.append(", ");
            }

            t.append("<br />");
            results.beforeFirst();

            while (results.next()) {

                for (int i = 1; i < (numColumns + 1); i++) {
                    t.append(results.getString(i));
                    t.append(", ");
                }

                t.append("<br />");
            }

        } else {
            t.append("Query Successful; however no data was returned from this query.");
        }

        t.append("</p>");
        return (t.toString());
    }
}
