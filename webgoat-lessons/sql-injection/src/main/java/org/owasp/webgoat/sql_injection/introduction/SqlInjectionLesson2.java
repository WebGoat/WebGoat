
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;


@RestController
@AssignmentHints(value = {"SqlStringInjectionHint2-1", "SqlStringInjectionHint2-2", "SqlStringInjectionHint2-3", "SqlStringInjectionHint2-4"})
public class SqlInjectionLesson2 extends AssignmentEndpoint {

    private final DataSource dataSource;

    public SqlInjectionLesson2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/SqlInjection/attack2")
    @ResponseBody
    public AttackResult completed(@RequestParam String query) {
        return injectableQuery(query);
    }

    protected AttackResult injectableQuery(String query) {
        try (var connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
            ResultSet results = statement.executeQuery(query);
            StringBuffer output = new StringBuffer();

            results.first();

            if (results.getString("department").equals("Marketing")) {
                output.append("<span class='feedback-positive'>" + query + "</span>");
                output.append(SqlInjectionLesson8.generateTable(results));
                return trackProgress(success().feedback("sql-injection.2.success").output(output.toString()).build());
            } else {
                return trackProgress(failed().feedback("sql-injection.2.failed").output(output.toString()).build());
            }
        } catch (SQLException sqle) {
            return trackProgress(failed().feedback("sql-injection.2.failed").output(sqle.getMessage()).build());
        }
    }
}
