
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@AssignmentHints(value = {"SqlStringInjectionHint.10.1", "SqlStringInjectionHint.10.2", "SqlStringInjectionHint.10.3", "SqlStringInjectionHint.10.4", "SqlStringInjectionHint.10.5", "SqlStringInjectionHint.10.6"})
public class SqlInjectionLesson10 extends AssignmentEndpoint {

    private final LessonDataSource dataSource;

    public SqlInjectionLesson10(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/SqlInjection/attack10")
    @ResponseBody
    public AttackResult completed(@RequestParam String action_string) {
        return injectableQueryAvailability(action_string);
    }

    protected AttackResult injectableQueryAvailability(String action) {
        StringBuffer output = new StringBuffer();
        String query = "SELECT * FROM access_log WHERE action LIKE '%" + action + "%'";

        try (Connection connection = dataSource.getConnection()) {
            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);

                if (results.getStatement() != null) {
                    results.first();
                    output.append(SqlInjectionLesson8.generateTable(results));
                    return failed(this).feedback("sql-injection.10.entries").output(output.toString()).build();
                } else {
                    if (tableExists(connection)) {
                        return failed(this).feedback("sql-injection.10.entries").output(output.toString()).build();
                    } else {
                        return success(this).feedback("sql-injection.10.success").build();
                    }
                }
            } catch (SQLException e) {
                if (tableExists(connection)) {
                    return failed(this).output("<span class='feedback-negative'>" + e.getMessage() + "</span><br>" + output.toString()).build();
                } else {
                    return success(this).feedback("sql-injection.10.success").build();
                }
            }

        } catch (Exception e) {
            return failed(this).output("<span class='feedback-negative'>" + e.getMessage() + "</span>").build();
        }
    }

    private boolean tableExists(Connection connection) {
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = stmt.executeQuery("SELECT * FROM access_log");
            int cols = results.getMetaData().getColumnCount();
            return (cols > 0);
        } catch (SQLException e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("object not found: ACCESS_LOG")) {
                return false;
            } else {
                System.err.println(e.getMessage());
                return false;
            }
        }
    }

}
