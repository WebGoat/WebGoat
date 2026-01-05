package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests focusing on the change from Statement with concatenated SQL
 * to PreparedStatement with a parameter for username in SqlInjectionChallenge.
 */
class SqlInjectionChallengeTest {

  @Test
  void registerNewUser_usesPreparedStatementForUserCheck() throws Exception {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = mock(Connection.class);
    PreparedStatement checkStatement = mock(PreparedStatement.class);
    PreparedStatement insertStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select userid from sql_challenge_users where userid = ?"))
        .thenReturn(checkStatement);
    when(checkStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertStatement);

    AttackResult result =
        challenge.registerNewUser("newuser", "user@example.com", "password123");

    // Verify parameterized query usage for username check
    verify(connection, times(1))
        .prepareStatement("select userid from sql_challenge_users where userid = ?");
    verify(checkStatement, times(1)).setString(1, "newuser");
    verify(checkStatement, times(1)).executeQuery();
    assertNotNull(result);
  }
}
