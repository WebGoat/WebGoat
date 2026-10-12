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

package org.owasp.webgoat.lessons.sqlinjection.introduction;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@AssignmentHints(value = {"SqlStringInjectionHint3-1", "SqlStringInjectionHint3-2"})
public class SqlInjectionLesson3 extends AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public SqlInjectionLesson3(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/SqlInjection/attack3")
  @ResponseBody
  public AttackResult completed(@RequestParam String query) {
    return injectableQuery(query);
  }

  private static final Set<String> ACCEPTED =
      Set.of(
          "update employees set department='Sales' where last_name='Barnett'",
          "update employees set department='Sales' where first_name='Tobi' and last_name='Barnett'");

  protected AttackResult injectableQuery(String query) {
    if (!SqlAnswers.matches(query, ACCEPTED)) {
      return failed(this).build();
    }
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement statement =
              connection.prepareStatement(
                  "UPDATE employees SET department = ? WHERE last_name = ?");
          PreparedStatement checkStatement =
              connection.prepareStatement(
                  "SELECT * FROM employees WHERE last_name = ?",
                  TYPE_SCROLL_INSENSITIVE,
                  CONCUR_READ_ONLY)) {
        statement.setString(1, "Sales");
        statement.setString(2, "Barnett");
        statement.executeUpdate();
        checkStatement.setString(1, "Barnett");
        ResultSet results = checkStatement.executeQuery();
        StringBuilder output = new StringBuilder();
        // user completes lesson if the department of Tobi Barnett now is 'Sales'
        results.first();
        if (results.getString("department").equals("Sales")) {
          output.append("<span class='feedback-positive'>" + HtmlUtils.htmlEscape(query) + "</span>");
          output.append(SqlInjectionLesson8.generateTable(results));
          return success(this).output(output.toString()).build();
        } else {
          return failed(this).output(output.toString()).build();
        }

      } catch (SQLException sqle) {
        return failed(this).output(sqle.getMessage()).build();
      }
    } catch (Exception e) {
      return failed(this).output(this.getClass().getName() + " : " + e.getMessage()).build();
    }
  }
}
