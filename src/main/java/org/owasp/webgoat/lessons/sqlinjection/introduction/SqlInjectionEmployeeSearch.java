/*
 * SPDX-FileCopyrightText: Copyright © 2024 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.introduction;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.container.LessonDataSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqlInjectionEmployeeSearch {

  private final LessonDataSource dataSource;

  public SqlInjectionEmployeeSearch(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping(path = "/SqlInjection/employeeSearch", produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public String searchByDepartment(@RequestParam String department) {
    String query =
        "SELECT userid, first_name, last_name, department, salary, auth_tan FROM employees WHERE"
            + " department = '"
            + department
            + "'";

    try (Connection connection = dataSource.getConnection();
        Statement statement =
            connection.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY)) {
      ResultSet results = statement.executeQuery(query);
      return SqlInjectionLesson8.generateTable(results);
    } catch (SQLException sqle) {
      return "<br><span class='feedback-negative'>"
          + sqle.getMessage()
          + "</span><br> Your query was: "
          + query;
    }
  }
}
