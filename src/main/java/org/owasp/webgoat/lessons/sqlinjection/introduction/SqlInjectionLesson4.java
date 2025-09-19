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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
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
    value = {"SqlStringInjectionHint4-1", "SqlStringInjectionHint4-2", "SqlStringInjectionHint4-3"})
public class SqlInjectionLesson4 implements AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public SqlInjectionLesson4(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/SqlInjection/attack4")
  @ResponseBody
  public AttackResult completed(@RequestParam String phone, @RequestParam int employeeId) {
    return injectableQuery(phone, employeeId);
  }

  protected AttackResult injectableQuery(String phone, int employeeId) {
    String updateSql = "UPDATE employees SET phone = ? WHERE employee_id = ?";
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement preparedStatement =
           connection.prepareStatement(updateSql, TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY)) {
        preparedStatement.setString(1, phone);
        preparedStatement.setInt(2, employeeId);
        preparedStatement.executeUpdate();
        connection.commit();
        // Verify the update
        try (PreparedStatement selectStmt = connection.prepareStatement("SELECT phone FROM employees WHERE employee_id = ?")) {
          selectStmt.setInt(1, employeeId);
          ResultSet results = selectStmt.executeQuery();
          StringBuilder output = new StringBuilder();
          if (results.next()) {
            output.append("<span class='feedback-positive'>Update successful: " + phone + "</span>");
            return success(this).output(output.toString()).build();
          } else {
            return failed(this).output("No employee found with id: " + employeeId).build();
          }
        }
      } catch (SQLException sqle) {
        return failed(this).output(sqle.getMessage()).build();
      }
    } catch (Exception e) {
      return failed(this).output(this.getClass().getName() + " : " + e.getMessage()).build();
    }
  }
}
