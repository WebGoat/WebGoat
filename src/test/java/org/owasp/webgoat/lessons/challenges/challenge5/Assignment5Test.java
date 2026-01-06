package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

import static org.mockito.Mockito.*;

/**
 * Delta tests for Assignment5 focusing on the SQL injection fix.
 *
 * These tests specifically verify:
 *  - That user input is bound via PreparedStatement parameters (no concatenation).
 *  - That SQL injection-style password no longer bypasses authentication.
 */
public class Assignment5Test {

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
    void login_shouldUseParameterizedQueryAndBindUserInputs() throws Exception {
        when(resultSet.next()).thenReturn(true);

        String username = "Larry";
        String password = "secret";
        assignment5.login(username, password);

        // Verify the SQL string uses placeholders, not concatenation
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(connection).prepareStatement(sqlCaptor.capture());
        String sql = sqlCaptor.getValue();
        // Assert that the SQL contains parameter placeholders
        assertTrue(sql.contains("userid = ?"), "Expected parameter placeholder for userid");
        assertTrue(sql.contains("password = ?"), "Expected parameter placeholder for password");
        // Ensure user input not concatenated directly into SQL
        assertFalse(sql.contains(username), "SQL must not contain raw username");
        assertFalse(sql.contains(password), "SQL must not contain raw password");

        // Verify that user input is bound using setString
        verify(preparedStatement).setString(1, username);
        verify(preparedStatement).setString(2, password);
    }

    @Test
    void login_shouldNotAllowSqlInjectionViaPassword() throws Exception {
        // Simulate that the injected password does NOT match a row
        when(resultSet.next()).thenReturn(false);

        String username = "Larry";
        // Typical SQL injection payload attempting to bypass password check
        String injectionPassword = "' OR '1'='1";

        AttackResult result = assignment5.login(username, injectionPassword);

        // The attack should fail (no success feedback)
        assertFalse(result.getLessonCompleted(), "SQL injection payload must not succeed");

        // And the injected payload must be bound as a parameter, not concatenated
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(connection).prepareStatement(sqlCaptor.capture());
        String sql = sqlCaptor.getValue();
        assertFalse(sql.contains(injectionPassword), "SQL must not contain injected payload as literal");

        verify(preparedStatement).setString(1, username);
        verify(preparedStatement).setString(2, injectionPassword);
    }
}
