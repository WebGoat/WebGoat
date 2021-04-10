
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

import org.owasp.webgoat.LessonDataSource;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

@RestController
@AssignmentHints(value = {"SqlStringInjectionHint.8.1", "SqlStringInjectionHint.8.2", "SqlStringInjectionHint.8.3", "SqlStringInjectionHint.8.4", "SqlStringInjectionHint.8.5"})
public class SqlInjectionLesson8 extends AssignmentEndpoint {

    private final LessonDataSource dataSource;

    public SqlInjectionLesson8(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/SqlInjection/attack8")
    @ResponseBody
    public AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
        return injectableQueryConfidentiality(name, auth_tan);
    }

    protected AttackResult injectableQueryConfidentiality(String name, String auth_tan) {
        StringBuffer output = new StringBuffer();
        String query = "SELECT * FROM employees WHERE last_name = '" + name + "' AND auth_tan = '" + auth_tan + "'";

        try (Connection connection = dataSource.getConnection()) {
            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                log(connection, query);
                ResultSet results = statement.executeQuery(query);

                if (results.getStatement() != null) {
                    if (results.first()) {
                        output.append(generateTable(results));
                        results.last();

                        if (results.getRow() > 1) {
                            // more than one record, the user succeeded
                            return success(this).feedback("sql-injection.8.success").output(output.toString()).build();
                        } else {
                            // only one record
                            return failed(this).feedback("sql-injection.8.one").output(output.toString()).build();
                        }

                    } else {
                        // no results
                        return failed(this).feedback("sql-injection.8.no.results").build();
                    }
                } else {
                    return failed(this).build();
                }
            } catch (SQLException e) {
                return failed(this).output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build();
            }

        } catch (Exception e) {
            return failed(this).output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build();
        }
    }

    public static String generateTable(ResultSet results) throws SQLException {
        ResultSetMetaData resultsMetaData = results.getMetaData();
        int numColumns = resultsMetaData.getColumnCount();
        results.beforeFirst();
        StringBuffer table = new StringBuffer();
        table.append("<table>");

        if (results.next()) {
            table.append("<tr>");
            for (int i = 1; i < (numColumns + 1); i++) {
                table.append("<th>" + resultsMetaData.getColumnName(i) + "</th>");
            }
            table.append("</tr>");

            results.beforeFirst();
            while (results.next()) {
                table.append("<tr>");
                for (int i = 1; i < (numColumns + 1); i++) {
                    table.append("<td>" + results.getString(i) + "</td>");
                }
                table.append("</tr>");
            }

        } else {
            table.append("Query Successful; however no data was returned from this query.");
        }

        table.append("</table>");
        return (table.toString());
    }

    public static void log(Connection connection, String action) {
        action = action.replace('\'', '"');
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(cal.getTime());

        String logQuery = "INSERT INTO access_log (time, action) VALUES ('" + time + "', '" + action + "')";

        try {
            Statement statement = connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
            statement.executeUpdate(logQuery);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
