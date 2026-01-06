package org.owasp.webgoat.lessons.challenges.challenge5;

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
import org.owasp.webgoat.lessons.challenges.Flags;

class Assignment5Test {

  @Test
  @DisplayName("login should use PreparedStatement parameters and succeed when credentials are valid")
  void login_usesParameterizedQuery_andSucceeds() throws Exception {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    Flags flags = mock(Flags.class);
    Assignment5 assignment = new Assignment5(dataSource, flags);

    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(
            "select password from challenge_users where userid = ? and password = ?"))
        .thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(flags.getFlag(5)).thenReturn("FLAG-5");

    AttackResult result = assignment.login("Larry", "secret");

    verify(preparedStatement).setString(1, "Larry");
    verify(preparedStatement).setString(2, "secret");
    verify(preparedStatement).executeQuery();

    assertTrue(result.getLessonCompleted(), "Valid credentials should still complete the lesson");
  }

  @Test
  @DisplayName("login should return failure AttackResult when SQLException occurs")
  void login_handlesSqlException_securely() throws Exception {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    Flags flags = mock(Flags.class);
    Assignment5 assignment = new Assignment5(dataSource, flags);

    Connection connection = mock(Connection.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(
            "select password from challenge_users where userid = ? and password = ?"))
        .thenThrow(new SQLException("DB down"));

    AttackResult result = assignment.login("Larry", "secret");

    assertFalse(result.getLessonCompleted(), "On SQLException the lesson must not be completed");
  }
}
