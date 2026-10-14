/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.introduction;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

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
@AssignmentHints(
    value = {"SqlStringInjectionHint4-1", "SqlStringInjectionHint4-2", "SqlStringInjectionHint4-3"})
public class SqlInjectionLesson4 implements AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public SqlInjectionLesson4(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/SqlInjection/attack4")
  @ResponseBody
  public AttackResult completed(@RequestParam String query) {
    return injectableQuery(query);
  }

  private static final Set<String> ACCEPTED =
      Set.of(
          "alter table employees add column phone varchar(20)",
          "alter table employees add phone varchar(20)");
  // Constant DDL - user input is only compared, never executed
  private static final String SOLUTION_DDL = "ALTER TABLE employees ADD COLUMN phone varchar(20)";

  protected AttackResult injectableQuery(String query) {
    if (!SqlAnswers.matches(query, ACCEPTED)) {
      return failed(this).build();
    }
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement ddl = connection.prepareStatement(SOLUTION_DDL);
          PreparedStatement check =
              connection.prepareStatement(
                  "SELECT phone from employees", TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY)) {
        ddl.executeUpdate();
        connection.commit();
        ResultSet results = check.executeQuery();
        StringBuilder output = new StringBuilder();
        // user completes lesson if column phone exists
        if (results.first()) {
          output.append("<span class='feedback-positive'>" + HtmlUtils.htmlEscape(query) + "</span>");
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
