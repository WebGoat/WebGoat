/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement; // Added for parameterized query
import java.sql.ResultSet;
import java.sql.SQLException;
// Removed java.sql.Statement as PreparedStatement is used
import lombok.extern.slf4j.Slf4j; // Added for logging
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j // Added for logging
public class SqlInjectionLesson6b implements AssignmentEndpoint {
  private final LessonDataSource dataSource;

  public SqlInjectionLesson6b(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/SqlInjectionAdvanced/attack6b")
  @ResponseBody
  public AttackResult completed(@RequestParam String userid_6b) throws IOException {
    // Remediation: Changed to call a validation method instead of retrieving and comparing a password string
    if (checkPassword("dave", userid_6b)) { // Assuming 'dave' is the fixed username for this check
      return success(this).build();
    } else {
      return failed(this).build();
    }
  }

  // Remediation: Refactored to validate a provided password against the stored one,
  // instead of returning the stored password.
  // Removed hardcoded default password and printStackTrace for information exposure.
  protected boolean checkPassword(String username, String providedPassword) {
    try (Connection connection = dataSource.getConnection()) {
      // Using PreparedStatement to prevent potential SQL injection in the username parameter
      String query = "SELECT password FROM user_system_data WHERE user_name = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, username);
        ResultSet results = statement.executeQuery();

        if (results != null && results.first()) {
          String storedPassword = results.getString("password");
          return providedPassword.equals(storedPassword); // Compare provided password with stored one
        }
      } catch (SQLException sqle) {
        log.error("Database error during password check for user {}: {}", username, sqle.getMessage());
        // Do not rethrow or expose internal details
      }
    } catch (Exception e) {
      log.error("Error checking password for user {}: {}", username, e.getMessage());
      // Do not rethrow or expose internal details
    }
    return false; // Default to false on any error or if user not found
  }
}
