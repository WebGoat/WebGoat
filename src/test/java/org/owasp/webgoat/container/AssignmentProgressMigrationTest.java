/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

class AssignmentProgressMigrationTest {

  @Test
  void existingAssignmentsCanBeUsedByMultipleProgressRows() throws Exception {
    var dataSource =
        new DriverManagerDataSource("jdbc:hsqldb:mem:" + UUID.randomUUID(), "sa", "");
    dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");

    Flyway.configure()
        .dataSource(dataSource)
        .schemas("container")
        .locations("db/container")
        .target(MigrationVersion.fromVersion("2"))
        .load()
        .migrate();

    long assignmentId;
    try (var connection = dataSource.getConnection();
        var statement =
            connection.prepareStatement(
                "INSERT INTO CONTAINER.assignment (name, path) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, "CSRFLogin");
      statement.setString(2, "/csrf/login");
      statement.executeUpdate();
      try (var keys = statement.getGeneratedKeys()) {
        assertThat(keys.next()).isTrue();
        assignmentId = keys.getLong(1);
      }
      try (var progress =
          connection.prepareStatement(
              "INSERT INTO CONTAINER.assignment_progress (assignment_id, solved) VALUES (?, ?)")) {
        progress.setLong(1, assignmentId);
        progress.setBoolean(2, true);
        progress.executeUpdate();
      }
    }

    Flyway.configure()
        .dataSource(dataSource)
        .schemas("container")
        .locations("db/container")
        .load()
        .migrate();

    try (var connection = dataSource.getConnection();
        var progress =
            connection.prepareStatement(
                "INSERT INTO CONTAINER.assignment_progress (assignment_id, solved) VALUES (?, ?)")) {
      progress.setLong(1, assignmentId);
      progress.setBoolean(2, true);
      progress.executeUpdate();
      try (var count =
          connection.prepareStatement(
              "SELECT COUNT(*) FROM CONTAINER.assignment_progress WHERE assignment_id = ?")) {
        count.setLong(1, assignmentId);
        try (var rows = count.executeQuery()) {
          assertThat(rows.next()).isTrue();
          assertThat(rows.getInt(1)).isEqualTo(2);
        }
      }
      progress.setLong(1, assignmentId + 1);
      assertThatThrownBy(progress::executeUpdate).isInstanceOf(SQLException.class);
    }
  }
}
