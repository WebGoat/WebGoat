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

package org.owasp.webgoat.sql_injection.advanced;

import org.owasp.webgoat.LessonDataSource;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints(value = {"SqlInjectionChallengeHint1", "SqlInjectionChallengeHint2", "SqlInjectionChallengeHint3", "SqlInjectionChallengeHint4"})
public class SqlInjectionChallengeLogin extends AssignmentEndpoint {

    private final LessonDataSource dataSource;

    public SqlInjectionChallengeLogin(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/SqlInjectionAdvanced/challenge_Login")
    @ResponseBody
    public AttackResult login(@RequestParam String username_login, @RequestParam String password_login) throws Exception {
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement("select password from sql_challenge_users where userid = ? and password = ?");
            statement.setString(1, username_login);
            statement.setString(2, password_login);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return ("tom".equals(username_login)) ? success(this).build()
                        : failed(this).feedback("ResultsButNotTom").build();
            } else {
                return failed(this).feedback("NoResultsMatched").build();
            }
        }
    }
}
