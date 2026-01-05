package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

/**
 * Delta tests for Assignment5 focusing on the changed behavior:
 * - SQL is now constructed via parameterized PreparedStatement (no concatenation).
 * - Behaviour of login() in key branches is preserved.
 *
 * These tests specifically exercise:
 *  1) Missing credentials  'required4' feedback.
 *  2) Non-'Larry' username  'user.not.larry' feedback.
 *  3) Successful login when DB returns a row (resultSet.next() == true)  'challenge.solved'.
 *  4) Failed login when DB does not return a row  'challenge.close'.
 *
 * JDBC collaborators are mocked to assert query and parameter binding rather than executing SQL.
 */
class Assignment5Test {

  private LessonDataSource dataSource;
  private Flags flags;
  private Assignment5 assignment5;

  private Connection connection;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;

  @BeforeEach
  void setUp() throws Exception {
    dataSource = mock(LessonDataSource.class);
    flags = mock(Flags.class);
    assignment5 = new Assignment5(dataSource, flags);

    connection = mock(Connection.class);
    preparedStatement = mock(PreparedStatement.class);
    resultSet = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
  }

  @Test
  void login_shouldFailWhenCredentialsMissing() throws Exception {
    // Arrange
    String username = "";
    String password = "somePassword";

    // Act
    AttackResult result = assignment5.login(username, password);

    // Assert
    assertFalse(result.equals(success(this).build()), "Result should not be a generic success");
    assertTrue(
        result.getOutput().contains("required4"),
        "Feedback key 'required4' should be used when credentials are missing"
    );

    // Verify that DB is never hit when basic validation fails
    verifyNoInteractions(dataSource);
  }

  @Test
  void login_shouldFailWhenUsernameIsNotLarry() throws Exception {
    // Arrange
    String username = "Alice";
    String password = "somePassword";

    // Act
    AttackResult result = assignment5.login(username, password);

    // Assert
    assertTrue(
        result.getOutput().contains("user.not.larry"),
        "Feedback key 'user.not.larry' should be used for non-Larry usernames"
    );

    // DB should not be queried in this branch
    verifyNoInteractions(dataSource);
  }

  @Test
  void login_shouldSucceedWhenResultSetReturnsRow_andUsesParameterizedQuery() throws Exception {
    // Arrange
    String username = "Larry";
    String password = "correctPassword";

    // Simulate DB having a matching row
    when(resultSet.next()).thenReturn(true);
    when(flags.getFlag(5)).thenReturn("FLAG-5");

    // Capture SQL used in preparedStatement
    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    when(connection.prepareStatement(sqlCaptor.capture())).thenReturn(preparedStatement);

    // Act
    AttackResult result = assignment5.login(username, password);

    // Assert: functional behaviour
    assertTrue(
        result.getOutput().contains("challenge.solved"),
        "Feedback 'challenge.solved' should be used on successful login"
    );
    assertTrue(
        result.getOutput().contains("FLAG-5"),
        "The flag from Flags.getFlag(5) should be part of the success output"
    );

    // Assert: secure SQL construction via parameterized PreparedStatement
    String usedSql = sqlCaptor.getValue();
    assertNotNull(usedSql, "SQL passed to prepareStatement should not be null");
    assertTrue(
        usedSql.contains("userid = ?") && usedSql.contains("password = ?"),
        "SQL must use parameter placeholders instead of concatenating user input"
    );
    assertFalse(
        usedSql.contains("Larry") || usedSql.contains("correctPassword"),
        "SQL must not directly embed user-controlled values"
    );

    // Assert: parameters bound correctly
    verify(preparedStatement).setString(1, username);
    verify(preparedStatement).setString(2, password);
    verify(preparedStatement).executeQuery();
  }

  @Test
  void login_shouldFailWhenResultSetHasNoRows() throws Exception {
    // Arrange
    String username = "Larry";
    String password = "wrongPassword";

    // Simulate DB returning no rows
    when(resultSet.next()).thenReturn(false);

    // Act
    AttackResult result = assignment5.login(username, password);

    // Assert
    assertTrue(
        result.getOutput().contains("challenge.close"),
        "Feedback 'challenge.close' should be used when credentials are incorrect"
    );

    // Still expect a parameterized query to be used
    verify(connection).prepareStatement(
        argThat(sql ->
            sql.contains("userid = ?") &&
            sql.contains("password = ?") &&
            !sql.contains(username) &&
            !sql.contains(password)
        )
    );
    verify(preparedStatement).setString(1, username);
    verify(preparedStatement).setString(2, password);
    verify(preparedStatement).executeQuery();
  }
}
