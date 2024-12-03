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

import static org.hsqldb.jdbc.JDBCResultSet.CONCUR_UPDATABLE;
import static org.hsqldb.jdbc.JDBCResultSet.TYPE_SCROLL_SENSITIVE;
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
      "SqlStringInjectionHint.9.1",
      "SqlStringInjectionHint.9.2",
      "SqlStringInjectionHint.9.3",
      "SqlStringInjectionHint.9.4",
      "SqlStringInjectionHint.9.5"
    })
public class SqlInjectionLesson9 implements AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public SqlInjectionLesson9(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/SqlInjection/attack9")
  @ResponseBody
  public AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
    return injectableQueryIntegrity(name, auth_tan);
  }

  protected AttackResult injectableQueryIntegrity(String name, String auth_tan) {
    StringBuilder output = new StringBuilder();
    String queryInjection =
        "SELECT * FROM employees WHERE last_name = '"
            + name
            + "' AND auth_tan = '"
            + auth_tan
            + "'";
    try (Connection connection = dataSource.getConnection()) {
      // V2019_09_26_7__employees.sql
      int oldMaxSalary = this.getMaxSalary(connection);
      int oldSumSalariesOfOtherEmployees = this.getSumSalariesOfOtherEmployees(connection);
      // begin transaction
      connection.setAutoCommit(false);
      // do injectable query
      Statement statement = connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
      SqlInjectionLesson8.log(connection, queryInjection);
      statement.execute(queryInjection);
      // check new sum of salaries other employees and new salaries of John
      int newJohnSalary = this.getJohnSalary(connection);
      int newSumSalariesOfOtherEmployees = this.getSumSalariesOfOtherEmployees(connection);
      if (newJohnSalary > oldMaxSalary
          && newSumSalariesOfOtherEmployees == oldSumSalariesOfOtherEmployees) {
        // success commit
        connection.commit(); // need execute not executeQuery
        connection.setAutoCommit(true);
        output.append(
            SqlInjectionLesson8.generateTable(this.getEmployeesDataOrderBySalaryDesc(connection)));
        return success(this).feedback("sql-injection.9.success").output(output.toString()).build();
      }
      // failed roolback
      connection.rollback();
      return failed(this)
          .feedback("sql-injection.9.one")
          .output(
              SqlInjectionLesson8.generateTable(this.getEmployeesDataOrderBySalaryDesc(connection)))
          .build();
    } catch (SQLException e) {
      return failed(this)
          .output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>")
          .build();
    }
  }

  private int getSqlInt(Connection connection, String query) throws SQLException {
    Statement statement = connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
    ResultSet results = statement.executeQuery(query);
    results.first();
    return results.getInt(1);
  }

  private int getMaxSalary(Connection connection) throws SQLException {
    String query = "SELECT max(salary) FROM employees";
    return this.getSqlInt(connection, query);
  }

  private int getSumSalariesOfOtherEmployees(Connection connection) throws SQLException {
    String query = "SELECT sum(salary) FROM employees WHERE auth_tan != '3SL99A'";
    return this.getSqlInt(connection, query);
  }

  private int getJohnSalary(Connection connection) throws SQLException {
    String query = "SELECT salary FROM employees WHERE auth_tan = '3SL99A'";
    return this.getSqlInt(connection, query);
  }

  private ResultSet getEmployeesDataOrderBySalaryDesc(Connection connection) throws SQLException {
    String query = "SELECT * FROM employees ORDER BY salary DESC";
    Statement statement = connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
    return statement.executeQuery(query);
  }
}
