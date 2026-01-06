package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Delta tests for Assignment5 focusing on the SQL injection fix:
 * - PreparedStatement with parameters is used instead of string concatenation.
 * - Valid credentials succeed.
 * - Injection payload that previously could bypass auth does not succeed.
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

        // Some parts of AttackResultBuilder may require a request context; provide minimal one.
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    void login_withValidCredentials_shouldSucceedAndUseParameterizedQuery() throws Exception {
        // Arrange
        String username = "Larry";
        String password = "correct-password";

        when(resultSet.next()).thenReturn(true);
        when(flags.getFlag(5)).thenReturn("flag-5");

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        AttackResult result = assignment5.login(username, password);

        // Assert: business behavior
        // Success feedback key is "challenge.solved" when credentials are correct
        assertEquals(true, result.getLessonCompleted(), "Expected lesson to be marked as completed");

        // Assert: PreparedStatement with placeholders is used
        verify(connection).prepareStatement(sqlCaptor.capture());
        String usedSql = sqlCaptor.getValue();
        // Should no longer contain unescaped username/password concatenation
        // and must contain parameter placeholders.
        // This is a structural assertion; actual parameter count is 2.
        org.junit.jupiter.api.Assertions.assertTrue(
                usedSql.contains("where userid = ? and password = ?"),
                "Expected parameterized query with placeholders");

        // Assert: bound parameters are set correctly in order and no concatenation is used
        verify(preparedStatement).setString(1, username);
        verify(preparedStatement).setString(2, password);
    }

    @Test
    void login_withSqlInjectionPayload_shouldNotAuthenticate() throws Exception {
        // Arrange
        String username = "Larry";
        // Classic injection payload that would bypass string-concatenated SQL
        String password = "' OR '1'='1";

        // With parameterized query, DB will not match this as a valid password.
        when(resultSet.next()).thenReturn(false);

        // Act
        AttackResult result = assignment5.login(username, password);

        // Assert: attack no longer succeeds with injection payload
        assertEquals(false, result.getLessonCompleted(), "SQL injection payload must not bypass authentication");

        // Also verify parameters are bound literally (no concatenation)
        verify(preparedStatement).setString(1, username);
        verify(preparedStatement).setString(2, password);
    }
}
