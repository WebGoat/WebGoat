// Assumed package based on source location; adjust if needed.
// Source: src/main/java/org/owasp/webgoat/lessons/challenges/challenge5/Assignment5.java
package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.util.StringUtils;

/**
 * Delta tests for Assignment5 focusing on the SQL injection fix:
 * - Ensures PreparedStatement with parameter placeholders is used.
 * - Verifies user input is bound as parameters and not concatenated into SQL.
 */
class Assignment5Test {

  @Test
  void login_shouldUseParameterizedQueryWithUserInputs() throws Exception {
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
    when(flags.getFlag(5)).thenReturn("FLAG-5");

    AttackResult result = assignment5.login("Larry", "password");

    // Verify that prepared statement is used with placeholders, not concatenated SQL
    verify(connection).prepareStatement(
        "select password from challenge_users where userid = ? and password = ?");

    // Verify parameters are bound properly (no string concatenation of user input)
    verify(preparedStatement).setString(1, "Larry");
    verify(preparedStatement).setString(2, "password");

    assertTrue(result.getLessonCompleted(), "Successful login should complete the lesson");
  }

  @Test
  void login_shouldRejectNonLarryUserBeforeQueryExecution() throws Exception {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    Flags flags = mock(Flags.class);
    Assignment5 assignment5 = new Assignment5(dataSource, flags);

    AttackResult result = assignment5.login("Mallory", "any");

    // DataSource should never be touched if username is not Larry
    verifyNoInteractions(dataSource);

    assertFalse(result.getLessonCompleted(), "Non-Larry user must not pass the challenge");
  }

  @Test
  void login_shouldRejectEmptyCredentialsBeforeQueryExecution() throws Exception {
    LessonDataSource dataSource = mock(LessonDataSource.class);
    Flags flags = mock(Flags.class);
    Assignment5 assignment5 = new Assignment5(dataSource, flags);

    AttackResult result = assignment5.login("", "");

    verifyNoInteractions(dataSource);
    assertFalse(result.getLessonCompleted(), "Empty credentials must not pass the challenge");
  }
}
