package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

/**
 * Delta tests focusing on the SQL injection fix in Assignment5.login:
 * - Verifies parameterized query is used instead of string concatenation.
 * - Ensures valid credentials succeed, invalid fail.
 * - Ensures SQL injection payloads do not bypass authentication.
 */
class Assignment5Test {

    @Test
    @DisplayName("login with valid Larry credentials should succeed and use parameterized SQL")
    void login_withValidLarryCredentials_usesPreparedStatementAndSucceeds() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        Flags flags = mock(Flags.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(flags.getFlag(5)).thenReturn("FLAG-5");

        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        // Act
        AttackResult result = assignment5.login("Larry", "correct-password");

        // Assert
        assertEquals(true, result.getLessonCompleted(), "Expected challenge to be solved for valid Larry credentials");

        // Verify the SQL uses placeholders and parameters are bound correctly
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(connection).prepareStatement(sqlCaptor.capture());
        String usedSql = sqlCaptor.getValue();
        // Parameterized query should contain '?' placeholders, not concatenated values
        // and should not directly contain user input.
        org.junit.jupiter.api.Assertions.assertTrue(
                usedSql.contains("userid = ?") && usedSql.contains("password = ?"),
                "Expected SQL to use parameter placeholders, got: " + usedSql
        );

        // Verify bound parameters
        verify(preparedStatement).setString(1, "Larry");
        verify(preparedStatement).setString(2, "correct-password");
    }

    @Test
    @DisplayName("login with invalid credentials should fail even with injection-like payload in password")
    void login_withSqlInjectionPassword_doesNotBypassAuthentication() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        Flags flags = mock(Flags.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        // Simulate that DB does not find a matching row
        when(resultSet.next()).thenReturn(false);

        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        String injectionPassword = "anything' OR '1'='1";

        // Act
        AttackResult result = assignment5.login("Larry", injectionPassword);

        // Assert
        assertEquals(false, result.getLessonCompleted(), "Injection-like password must not bypass authentication");

        // Ensure parameters are passed as-is to the prepared statement (no concatenation)
        verify(preparedStatement).setString(1, "Larry");
        verify(preparedStatement).setString(2, injectionPassword);
    }

    @Test
    @DisplayName("login with non-Larry username should fail even with injection-like payload")
    void login_withNonLarryUsernameAndInjectionPayload_stillFails() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Flags flags = mock(Flags.class);
        // connection is never used for non-Larry user; but we keep mock for completeness
        LessonDataSource unusedDataSource = dataSource;

        Assignment5 assignment5 = new Assignment5(unusedDataSource, flags);

        String injectionUsername = "Larry' OR '1'='1";
        String password = "somePass";

        // Act
        AttackResult result = assignment5.login(injectionUsername, password);

        // Assert
        assertEquals(false, result.getLessonCompleted(),
                "Non-Larry username, even with injection payload, must not grant access");
        // DB should not be queried when username is not Larry
        verifyNoInteractions(dataSource);
    }
}
