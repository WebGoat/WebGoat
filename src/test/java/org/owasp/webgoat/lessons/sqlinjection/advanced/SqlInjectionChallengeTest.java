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
  @DisplayName("registerNewUser: existing user triggers user.exists using prepared SELECT")
  void registerNewUser_existingUserUsesPreparedSelect() throws SQLException {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = mock(Connection.class);
    PreparedStatement selectPs = mock(PreparedStatement.class);
    PreparedStatement insertPs = mock(PreparedStatement.class);
    ResultSet rs = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenReturn(selectPs);
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertPs);
    when(selectPs.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(true);

    AttackResult result =
        challenge.registerNewUser("john", "john@example.com", "secretPassword");

    verify(connection)
        .prepareStatement("select userid from sql_challenge_users where userid = ?");
    verify(selectPs).setString(1, "john");
    verify(selectPs).executeQuery();
    verify(insertPs, never()).execute();

    assertFalse(result.getLessonCompleted());
    assertTrue(result.getFeedback().contains("user.exists"));
  }

  @Test
  @DisplayName("registerNewUser: SQL injection in username does not bypass existence check")
  void registerNewUser_injectionInUsernameDoesNotBypassCheck() throws SQLException {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = mock(Connection.class);
    PreparedStatement selectPs = mock(PreparedStatement.class);
    PreparedStatement insertPs = mock(PreparedStatement.class);
    ResultSet rs = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenReturn(selectPs);
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertPs);
    when(selectPs.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(true);

    String maliciousUsername = "john' OR '1'='1";

    AttackResult result =
        challenge.registerNewUser(maliciousUsername, "attacker@example.com", "pw");

    verify(connection)
        .prepareStatement("select userid from sql_challenge_users where userid = ?");
    verify(selectPs).setString(1, maliciousUsername);
    verify(selectPs).executeQuery();
    verify(insertPs, never()).execute();

    assertFalse(result.getLessonCompleted());
  }

  @Test
  @DisplayName("registerNewUser: new user is inserted using prepared INSERT")
  void registerNewUser_newUserIsInsertedSecurely() throws SQLException {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = mock(Connection.class);
    PreparedStatement selectPs = mock(PreparedStatement.class);
    PreparedStatement insertPs = mock(PreparedStatement.class);
    ResultSet rs = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenReturn(selectPs);
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertPs);
    when(selectPs.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(false);

    AttackResult result =
        challenge.registerNewUser("alice", "alice@example.com", "pw123");

    verify(selectPs).setString(1, "alice");
    verify(selectPs).executeQuery();

    verify(insertPs).setString(1, "alice");
    verify(insertPs).setString(2, "alice@example.com");
    verify(insertPs).setString(3, "pw123");
    verify(insertPs).execute();

    assertTrue(result.getFeedback().contains("user.created"));
  }
}
