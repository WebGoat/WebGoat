/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Assignment5 implements AssignmentEndpoint {

  private final LessonDataSource dataSource;
  private final Flags flags;
    @PostMapping("/challenge/5")
    @ResponseBody
    public AttackResult login(@RequestParam String username_login, @RequestParam String password_login) throws Exception {
        Connection connection = DatabaseUtilities.getConnection(webSession);
        String query = "SELECT * FROM challenge_users WHERE userid = ? and password = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username_login);
            statement.setString(2, password_login);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return success().build();
            } else {
                return failed().feedback("challenge.login.fail").build();
            }
        } catch (SQLException e) {
            log.error("SQL Error", e);
            return failed().output("Database error occurred").build();
        }
    }
}
