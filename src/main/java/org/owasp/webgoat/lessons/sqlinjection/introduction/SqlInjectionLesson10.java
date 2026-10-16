/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.introduction;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints(
    value = {
      "SqlStringInjectionHint.10.1",
      "SqlStringInjectionHint.10.2",
      "SqlStringInjectionHint.10.3",
      "SqlStringInjectionHint.10.4",
      "SqlStringInjectionHint.10.5",
      "SqlStringInjectionHint.10.6"
    })
public class SqlInjectionLesson10 implements AssignmentEndpoint {

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
    StringBuilder output = new StringBuilder();
    String query = "SELECT * FROM access_log WHERE action LIKE '%" + action + "%'";

    try (Connection connection = dataSource.getConnection()) {
      try {
        Statement statement =
            connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet results = statement.executeQuery(query);

        if (results.getStatement() != null) {
          results.first();
          output.append(SqlInjectionLesson8.generateTable(results));
          return failed(this)
              .feedback("sql-injection.10.entries")
              .output(output.toString())
              .build();
        } else {
          if (tableExists(connection)) {
            return failed(this)
                .feedback("sql-injection.10.entries")
                .output(output.toString())
                .build();
          } else {
            return success(this).feedback("sql-injection.10.success").build();
          }
        }
      } catch (SQLException e) {
        if (tableExists(connection)) {
          return failed(this)
              .output(
                  "<span class='feedback-negative'>"
                      + e.getMessage()
                      + "</span><br>"
                      + output.toString())
              .build();
        } else {
          return success(this).feedback("sql-injection.10.success").build();
        }
      }

    } catch (Exception e) {
      return failed(this)
          .output("<span class='feedback-negative'>" + e.getMessage() + "</span>")
          .build();
    }
  }

  private boolean tableExists(Connection connection) {
    try {
      Statement stmt =
          connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet results = stmt.executeQuery("SELECT * FROM access_log");
      int cols = results.getMetaData().getColumnCount();
      return (cols > 0);
    } catch (SQLException e) {
      String errorMsg = e.getMessage();
      if (errorMsg.contains("object not found: ACCESS_LOG")) {
        return false;
      } else {
        return true;
      }
    }
  }
}
