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

package org.owasp.webgoat.lessons.sqlinjection.advanced;

import java.sql.*;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@RestController
@AssignmentHints(
    value = {"SqlInjectionChallenge1", "SqlInjectionChallenge2", "SqlInjectionChallenge3"})
@Slf4j
public class SqlInjectionChallenge extends AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public SqlInjectionChallenge(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PutMapping("/SqlInjectionAdvanced/challenge")
  // assignment path is bounded to class so we use different http method :-)
  @ResponseBody
  public AttackResult registerNewUser(
      @RequestParam String username_reg,
      @RequestParam String email_reg,
      @RequestParam String password_reg)
      throws Exception {
    AttackResult attackResult = checkArguments(username_reg, email_reg, password_reg);

    if (attackResult == null) {

      try (Connection connection = dataSource.getConnection()) {
        String checkUserQuery =
            "select userid from sql_challenge_users where userid = '" + username_reg + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(checkUserQuery);

        if (resultSet.next()) {
          if (username_reg.contains("tom'")) {
            attackResult = success(this).feedback("user.exists").build();
          } else {
            attackResult = failed(this).feedback("user.exists").feedbackArgs(username_reg).build();
          }
        } else {
          PreparedStatement preparedStatement =
              connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)");
          preparedStatement.setString(1, username_reg);
          preparedStatement.setString(2, email_reg);
          preparedStatement.setString(3, password_reg);
          preparedStatement.execute();
          attackResult = success(this).feedback("user.created").feedbackArgs(username_reg).build();
        }
      } catch (SQLException e) {
        attackResult = failed(this).output("Something went wrong").build();
      }
    }
    return attackResult;
  }

  private AttackResult checkArguments(String username_reg, String email_reg, String password_reg) {
    if (StringUtils.isEmpty(username_reg)
        || StringUtils.isEmpty(email_reg)
        || StringUtils.isEmpty(password_reg)) {
      return failed(this).feedback("input.invalid").build();
    }
    if (username_reg.length() > 250 || email_reg.length() > 30 || password_reg.length() > 30) {
      return failed(this).feedback("input.invalid").build();
    }
    return null;
  }
}
