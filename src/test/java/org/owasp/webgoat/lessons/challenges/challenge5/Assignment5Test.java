package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

/**
 * Delta tests focusing on the change from string-concatenated SQL to parameterized query
 * in Assignment5.login.
 */
class Assignment5Test {

  @Test
  void login_usesParameterizedQueryForUsernameAndPassword() throws Exception {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    Flags flags = mock(Flags.class);
    Assignment5 assignment5 = new Assignment5(dataSource, flags);

    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(
            "select password from challenge_users where userid = ? and password = ?"))
        .thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(flags.getFlag(5)).thenReturn("flag-5");

    AttackResult result = assignment5.login("Larry", "secret");

    // Verify that a parameterized query is used with placeholders
    verify(connection, times(1))
        .prepareStatement(
            "select password from challenge_users where userid = ? and password = ?");
    verify(preparedStatement, times(1)).setString(1, "Larry");
    verify(preparedStatement, times(1)).setString(2, "secret");
    verify(preparedStatement, times(1)).executeQuery();
    assertTrue(result.isLessonCompleted());
  }
}
