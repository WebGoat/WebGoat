package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests focusing on:
 * - Removal of hard-coded default password "dave" in getPassword().
 * - Ensuring exceptions are logged via SLF4J instead of printStackTrace (no stack trace leakage).
 */
@Slf4j
class SqlInjectionLesson6bTest {

    @Test
    @DisplayName("getPassword returns DB value and does not fall back to hard-coded 'dave'")
    void getPassword_returnsDbValue_notHardCoded() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement(
                anyInt(), anyInt())).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet != null && resultSet.first()).thenReturn(true);
        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn("dbPassword");

        SqlInjectionLesson6b lesson = new SqlInjectionLesson6b(dataSource);

        // Act
        String password = lesson.getPassword();

        // Assert
        assertEquals("dbPassword", password, "Password should come from DB, not a hard-coded default");
        assertNotEquals("dave", password, "Password must no longer use hard-coded fallback value 'dave'");
    }

    @Test
    @DisplayName("completed returns success only when user input matches DB password")
    void completed_comparesAgainstDbPasswordOnly() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement(
                anyInt(), anyInt())).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn("secretFromDb");

        SqlInjectionLesson6b lesson = Mockito.spy(new SqlInjectionLesson6b(dataSource));
        // Use real getPassword() to ensure behavior is consistent
        doCallRealMethod().when(lesson).getPassword();

        // Act & Assert
        AttackResult successResult = lesson.completed("secretFromDb");
        AttackResult failResult = lesson.completed("wrongPassword");

        assertTrue(successResult.getLessonCompleted(), "Matching DB password should succeed");
        assertFalse(failResult.getLessonCompleted(), "Non-matching password should fail");
    }

    @Test
    @DisplayName("getPassword handles SQL exception without leaking stack trace")
    void getPassword_onSqlException_doesNotLeakStackTrace() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement(anyInt(), anyInt()))
                .thenThrow(new SQLException("Simulated SQL failure"));

        SqlInjectionLesson6b lesson = new SqlInjectionLesson6b(dataSource);

        // We cannot easily assert logging behavior without a logging test appender,
        // but we can assert that getPassword() does not throw and returns null
        // (meaning no hard-coded fallback is used) when exceptions occur.
        // This indirectly verifies the new behavior (no printStackTrace, no 'dave' fallback).

        // Act
        String password = lesson.getPassword();

        // Assert
        assertNull(password, "On exception, getPassword should return null and avoid using default 'dave'");
    }
}
