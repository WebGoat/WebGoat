/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.introduction;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

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
    value = {
      "SqlStringInjectionHint2-1",
      "SqlStringInjectionHint2-2",
      "SqlStringInjectionHint2-3",
      "SqlStringInjectionHint2-4"
    })
public class SqlInjectionLesson2 implements AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public SqlInjectionLesson2(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/SqlInjection/attack2")
  @ResponseBody
  public AttackResult completed(@RequestParam String query) {
    return injectableQuery(query);
  }

  private static final Set<String> ACCEPTED =
      Set.of(
          "select department from employees where last_name='Franco'",
          "select department from employees where first_name='Bob' and last_name='Franco'",
          "select department from employees where userid=96134");
  private static final String SOLUTION_QUERY =
      "SELECT department FROM employees WHERE last_name = ?";

  protected AttackResult injectableQuery(String query) {
    if (!SqlAnswers.matches(query, ACCEPTED)) {
      return failed(this).feedback("sql-injection.2.failed").build();
    }
    try (var connection = dataSource.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(SOLUTION_QUERY, TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY)) {
      statement.setString(1, "Franco");
      ResultSet results = statement.executeQuery();
      StringBuilder output = new StringBuilder();

      if(!results.first()) {
          return failed(this).feedback("sql-injection.2.failed").build();
      }

      if (results.getString("department").equals("Marketing")) {
        output.append("<span class='feedback-positive'>" + HtmlUtils.htmlEscape(query) + "</span>");

        output.append(SqlInjectionLesson8.generateTable(results));
        return success(this).feedback("sql-injection.2.success").output(output.toString()).build();
      } else {
        return failed(this).feedback("sql-injection.2.failed").output(output.toString()).build();
      }
    } catch (SQLException sqle) {
      return failed(this).feedback("sql-injection.2.failed").output(sqle.getMessage()).build();
    }
  }
}
