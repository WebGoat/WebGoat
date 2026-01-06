package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

class SqlInjectionChallengeTest {

  @Test
  @DisplayName("registerNewUser should use PreparedStatement with parameterized username")
  void registerNewUser_usesParameterizedQueryForUserCheck() throws SQLException {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = mock(Connection.class);
    PreparedStatement checkUserPs = mock(PreparedStatement.class);
    PreparedStatement insertPs = mock(PreparedStatement.class);
    ResultSet checkUserResult = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenReturn(checkUserPs);
    when(checkUserPs.executeQuery()).thenReturn(checkUserResult);
    when(checkUserResult.next()).thenReturn(false);
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertPs);

    challenge.registerNewUser("user1", "user1@example.com", "pwd123");

    verify(checkUserPs).setString(1, "user1");
    verify(checkUserPs).executeQuery();
    verify(insertPs).setString(1, "user1");
    verify(insertPs).setString(2, "user1@example.com");
    verify(insertPs).setString(3, "pwd123");
    verify(insertPs).execute();
  }

  @Test
  @DisplayName("registerNewUser should handle SQLException without exposing SQL details")
  void registerNewUser_handlesSqlException_securely() throws SQLException {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = mock(Connection.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenThrow(new SQLException("Database unavailable"));

    AttackResult result = challenge.registerNewUser("user1", "user1@example.com", "pwd123");

    assertFalse(result.getLessonCompleted(), "On SQLException the lesson must not complete");
  }
}
