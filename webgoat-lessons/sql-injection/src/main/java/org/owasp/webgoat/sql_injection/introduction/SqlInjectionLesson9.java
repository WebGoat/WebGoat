
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hsqldb.jdbc.JDBCResultSet.CONCUR_UPDATABLE;
import static org.hsqldb.jdbc.JDBCResultSet.TYPE_SCROLL_SENSITIVE;

@RestController
@AssignmentHints(value = {"SqlStringInjectionHint.9.1", "SqlStringInjectionHint.9.2", "SqlStringInjectionHint.9.3", "SqlStringInjectionHint.9.4", "SqlStringInjectionHint.9.5"})
public class SqlInjectionLesson9 extends AssignmentEndpoint {

    private final DataSource dataSource;

    public SqlInjectionLesson9(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/SqlInjection/attack9")
    @ResponseBody
    public AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
        return injectableQueryIntegrity(name, auth_tan);
    }

    protected AttackResult injectableQueryIntegrity(String name, String auth_tan) {
        StringBuffer output = new StringBuffer();
        String query = "SELECT * FROM employees WHERE last_name = '" + name + "' AND auth_tan = '" + auth_tan + "'";
        try (Connection connection = dataSource.getConnection()) {
            try {
                Statement statement = connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
                SqlInjectionLesson8.log(connection, query);
                ResultSet results = statement.executeQuery(query);
                var test = results.getRow() != 0;
                if (results.getStatement() != null) {
                    if (results.first()) {
                        output.append(SqlInjectionLesson8.generateTable(results));
                    } else {
                        // no results
                        return failed(this).feedback("sql-injection.8.no.results").build();
                    }
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return failed(this).feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build();
            }

            return checkSalaryRanking(connection, output);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return failed(this).feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build();
        }
    }

    private AttackResult checkSalaryRanking(Connection connection, StringBuffer output) {
        try {
            String query = "SELECT * FROM employees ORDER BY salary DESC";
            try (Statement statement = connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
            ) {
                ResultSet results = statement.executeQuery(query);

                results.first();
                // user completes lesson if John Smith is the first in the list
                if ((results.getString(2).equals("John")) && (results.getString(3).equals("Smith"))) {
                    output.append(SqlInjectionLesson8.generateTable(results));
                    return success(this).feedback("sql-injection.9.success").output(output.toString()).build();
                } else {
                    return failed(this).feedback("sql-injection.9.one").output(output.toString()).build();
                }
            }
        } catch (SQLException e) {
            return failed(this).feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build();
        }
    }

}
