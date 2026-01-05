// Assumed package based on source location; adjust if needed.
// Source: src/main/java/org/owasp/webgoat/lessons/sqlinjection/advanced/SqlInjectionChallenge.java
package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for SqlInjectionChallenge focusing on the parameterized checkUserQuery:
 * - Ensures PreparedStatement with placeholder is used.
 * - Verifies that the username is bound as a parameter.
 */
class SqlInjectionChallengeTest {

  @Test
  void registerNewUser_shouldUsePreparedStatementForUserExistenceCheck() throws SQLException {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = mock(Connection.class);
    PreparedStatement selectStatement = mock(PreparedStatement.class);
    PreparedStatement insertStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenReturn(selectStatement);
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)")).thenReturn(insertStatement);
    when(selectStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false); // user does not exist

    AttackResult result = challenge.registerNewUser("newuser", "user@example.com", "pass123");

    verify(connection).prepareStatement("select userid from sql_challenge_users where userid = ?");
    verify(selectStatement).setString(1, "newuser");

    verify(connection).prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)");
    verify(insertStatement).setString(1, "newuser");
    verify(insertStatement).setString(2, "user@example.com");
    verify(insertStatement).setString(3, "pass123");

    assertNotNull(result, "AttackResult should not be null");
  }

  @Test
  void registerNewUser_shouldFailFastOnInvalidInputWithoutQueryExecution() {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    AttackResult result = challenge.registerNewUser("", "too-long-email-address@example.com", "");

    assertFalse(result.getLessonCompleted(), "Invalid input should not complete the lesson");
    // Ensure no DB call is made when validation fails
    verifyNoInteractions(dataSource);
  }
}
