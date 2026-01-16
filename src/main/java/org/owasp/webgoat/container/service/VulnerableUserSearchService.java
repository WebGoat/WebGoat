/*
 * SPDX-FileCopyrightText: Copyright © 2024 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.LessonDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * VULNERABILITY #1: SQL Injection
 * This service demonstrates SQL injection vulnerability by concatenating user input
 * directly into SQL queries without parameterization.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class VulnerableUserSearchService {

  private final LessonDataSource dataSource;

  @GetMapping("/api/vulnerable/search")
  public ResponseEntity<String> searchUsers(@RequestParam("query") String userQuery) {
    try (Connection connection = dataSource.getConnection()) {
      // VULNERABILITY: Direct string concatenation of user input into SQL query
      String sql = "SELECT username, email FROM users WHERE username LIKE '%" + userQuery + "%'";
      
      try (Statement statement = connection.createStatement()) {
        ResultSet rs = statement.executeQuery(sql);
        StringBuilder result = new StringBuilder();
        result.append("Found users: ");
        
        while (rs.next()) {
          result.append(rs.getString("username")).append(" (").append(rs.getString("email")).append("), ");
        }
        
        return ResponseEntity.ok(result.toString());
      }
    } catch (Exception e) {
      log.error("Error searching users", e);
      return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
  }
}

