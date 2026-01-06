package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

/**
 * Delta tests for Assignment5 focusing only on the changed SQL behavior:
 * - ensure prepared statement with parameters is effectively used
 * - ensure SQL injection attempts do not bypass authentication
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
        when(flags.getFlag(5)).thenReturn("FLAG-5");
    }

    @Test
    void loginWithCorrectLarryCredentialsShouldSucceed() throws Exception {
        // Arrange
        when(resultSet.next()).thenReturn(true);

        // Act
        AttackResult result = assignment5.login("Larry", "correct-password");

        // Assert
        assertTrue(result.getLessonCompleted(), "Expected successful login for valid Larry credentials");
        assertEquals("FLAG-5", result.getFeedbackArgs()[0]);
        verify(preparedStatement).setString(1, "Larry");
        verify(preparedStatement).setString(2, "correct-password");
    }

    @Test
    void loginWithIncorrectPasswordShouldFail() throws Exception {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        // Act
        AttackResult result = assignment5.login("Larry", "wrong-password");

        // Assert
        assertTrue(!result.getLessonCompleted(), "Expected failed login for invalid password");
        verify(preparedStatement).setString(1, "Larry");
        verify(preparedStatement).setString(2, "wrong-password");
    }

    @Test
    void sqlInjectionInPasswordShouldNotBypassAuthentication() throws Exception {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        String injection = "' OR '1'='1";

        // Act
        AttackResult result = assignment5.login("Larry", injection);

        // Assert
        assertTrue(!result.getLessonCompleted(), "SQL injection payload must not bypass authentication");
        verify(preparedStatement).setString(1, "Larry");
        verify(preparedStatement).setString(2, injection);
        // The key property of the fix: user input is bound as parameters, not concatenated into SQL
    }
}
