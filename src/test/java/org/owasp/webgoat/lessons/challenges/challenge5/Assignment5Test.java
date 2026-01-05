package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

class Assignment5Test {

  @Test
  @DisplayName("login: should authenticate valid Larry user using bound parameters")
  void login_authenticatesValidUserWithParameterizedQuery() throws Exception {
    // Arrange
    LessonDataSource dataSource = mock(LessonDataSource.class);
    Flags flags = mock(Flags.class);
    Assignment5 assignment5 = new Assignment5(dataSource, flags);

    Connection connection = mock(Connection.class);
    PreparedStatement ps = mock(PreparedStatement.class);
    ResultSet rs = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(ps);
    when(ps.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(true);
    when(flags.getFlag(5)).thenReturn("FLAG-5");

    // Act
    AttackResult result = assignment5.login("Larry", "goodPassword");

    // Assert
    verify(connection)
        .prepareStatement(
            "select password from challenge_users where userid = ? and password = ?");
    verify(ps).setString(1, "Larry");
    verify(ps).setString(2, "goodPassword");
    verify(ps).executeQuery();

    assertTrue(result.getLessonCompleted(), "Valid credentials for Larry should succeed");
    assertTrue(
        result.getFeedback().contains("challenge.solved"),
        "Feedback should indicate the challenge was solved");
  }

  @Test
  @DisplayName("login: SQL injection attempt in password should not be treated as code")
  void login_sqlInjectionInPasswordDoesNotBypassAuth() throws Exception {
    // Arrange
    LessonDataSource dataSource = mock(LessonDataSource.class);
    Flags flags = mock(Flags.class);
    Assignment5 assignment5 = new Assignment5(dataSource, flags);

    Connection connection = mock(Connection.class);
    PreparedStatement ps = mock(PreparedStatement.class);
    ResultSet rs = mock(ResultSet.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(ps);
    when(ps.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(false);

    String maliciousPassword = "x' OR '1'='1";

    // Act
    AttackResult result = assignment5.login("Larry", maliciousPassword);

    // Assert
    verify(connection)
        .prepareStatement(
            "select password from challenge_users where userid = ? and password = ?");
    verify(ps).setString(1, "Larry");
    verify(ps).setString(2, maliciousPassword);
    verify(ps).executeQuery();

    assertFalse(
        result.getLessonCompleted(), "Injection in password must not bypass authentication");
    assertTrue(
        result.getFeedback().contains("challenge.close"),
        "Feedback should indicate login failure, not success");
  }
}
