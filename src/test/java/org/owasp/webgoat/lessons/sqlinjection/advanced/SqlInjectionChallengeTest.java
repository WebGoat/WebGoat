// Assumed package based on source file location; adjust if actual package differs.
package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

public class SqlInjectionChallengeTest {

  private LessonDataSource dataSource;
  private SqlInjectionChallenge challenge;
  private Connection connection;
  private PreparedStatement checkUserStmt;
  private PreparedStatement insertUserStmt;
  private ResultSet resultSet;

  @BeforeEach
  void setUp() throws Exception {
    dataSource = mock(LessonDataSource.class);
    connection = mock(Connection.class);
    checkUserStmt = mock(PreparedStatement.class);
    insertUserStmt = mock(PreparedStatement.class);
    resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenReturn(checkUserStmt);
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertUserStmt);
    when(checkUserStmt.executeQuery()).thenReturn(resultSet);

    challenge = new SqlInjectionChallenge(dataSource);
  }

  @Test
  void registerNewUser_shouldUsePreparedStatementForUserCheck() {
    // Arrange
    String username = "user1";
    String email = "user1@example.com";
    String password = "secret";
    try {
      when(resultSet.next()).thenReturn(false);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // Act
    AttackResult result = challenge.registerNewUser(username, email, password);

    // Assert: check query uses prepared statement with parameter binding
    try {
      verify(checkUserStmt).setString(1, username);
      verify(checkUserStmt).executeQuery();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.getOutput().contains("user.created"));
  }

  @Test
  void registerNewUser_shouldFailGracefullyOnSQLException() throws Exception {
    // Arrange
    String username = "user2";
    String email = "user2@example.com";
    String password = "secret";
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenThrow(new SQLException("DB error"));

    // Act
    AttackResult result = challenge.registerNewUser(username, email, password);

    // Assert: lesson should not be marked as completed
    assertFalse(result.getLessonCompleted());
  }
}
