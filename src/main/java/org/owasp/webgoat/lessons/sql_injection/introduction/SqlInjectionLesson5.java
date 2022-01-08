
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@RestController
@AssignmentHints(value = {"SqlStringInjectionHint5-1", "SqlStringInjectionHint5-2", "SqlStringInjectionHint5-3", "SqlStringInjectionHint5-4"})
public class SqlInjectionLesson5 extends AssignmentEndpoint {

    private final LessonDataSource dataSource;

    public SqlInjectionLesson5(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void createUser() {
        // HSQLDB does not support CREATE USER with IF NOT EXISTS so we need to do it in code (using DROP first will throw error if user does not exists)
        try (Connection connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("CREATE USER unauthorized_user PASSWORD test")) {
                statement.execute();
            }
        } catch (Exception e) {
            //user already exists continue
        }
    }

    @PostMapping("/SqlInjection/attack5")
    @ResponseBody
    public AttackResult completed(String query) {
        createUser();
        return injectableQuery(query);
    }

    protected AttackResult injectableQuery(String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                statement.executeQuery(query);
                if (checkSolution(connection)) {
                    return success(this).build();
                }
                return failed(this).output("Your query was: " + query).build();
            }
        } catch (Exception e) {
            return failed(this).output(this.getClass().getName() + " : " + e.getMessage() + "<br> Your query was: " + query).build();
        }
    }

    private boolean checkSolution(Connection connection) {
        try {
            var stmt = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES WHERE TABLE_NAME = ? AND GRANTEE = ?");
            stmt.setString(1, "GRANT_RIGHTS");
            stmt.setString(2, "UNAUTHORIZED_USER");
            var resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            return false;
        }

    }
}
