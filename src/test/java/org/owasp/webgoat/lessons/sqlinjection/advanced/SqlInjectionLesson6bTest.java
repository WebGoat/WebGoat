package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.LessonDataSource;

/**
 * Delta tests for SqlInjectionLesson6b focusing on changed exception handling:
 * - getPassword() returns non-null even when DB is available
 * - getPassword() swallows/logs exceptions and does not rethrow
 */
class SqlInjectionLesson6bTest {

    private LessonDataSource dataSource;
    private SqlInjectionLesson6b lesson;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = mock(LessonDataSource.class);
        lesson = new SqlInjectionLesson6b(dataSource);

        connection = mock(Connection.class);
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement(anyInt(), anyInt())).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
    }

    @Test
    void getPasswordReturnsNonNullOnSuccessfulQuery() throws Exception {
        // Arrange
        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn("secret");

        // Act
        String password = lesson.getPassword();

        // Assert
        assertNotNull(password, "Password should not be null when query succeeds");
        assertEquals("secret", password);
    }

    @Test
    void getPasswordHandlesSqlExceptionWithoutThrowing() throws Exception {
        // Arrange
        when(connection.createStatement(anyInt(), anyInt())).thenThrow(new SQLException("DB down"));

        // Act
        String password = null;
        try {
            password = lesson.getPassword();
        } catch (Exception e) {
            fail("getPassword() should not propagate exceptions after the logging fix");
        }

        // Assert
        assertNotNull(password, "Password should still be non-null (fallback default) on exception");
        assertEquals("dave", password, "Method should return default value when exception occurs");
        // The fix ensures exceptions are logged via log.error instead of leaking stack traces
    }
}
