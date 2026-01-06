package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.owasp.webgoat.container.LessonDataSource;
import org.slf4j.Logger;

/**
 * Delta unit tests for SqlInjectionLesson6b focusing on:
 *  - Replacing printStackTrace() with structured logging.
 *  - Ensuring getPassword() still retrieves the password correctly on success.
 */
class SqlInjectionLesson6bTest {

    @Test
    @DisplayName("getPassword should return password from DB when query succeeds")
    void getPassword_returnsPasswordFromDatabase() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
                .thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(rs);
        when(rs.first()).thenReturn(true);
        when(rs.getString("password")).thenReturn("db-password");

        SqlInjectionLesson6b lesson = new SqlInjectionLesson6b(dataSource);

        // Act
        String password = lesson.getPassword();

        // Assert
        assertEquals("db-password", password);
    }

    @Test
    @DisplayName("getPassword should log errors instead of using printStackTrace when SQL exception occurs")
    void getPassword_logsErrorsInsteadOfPrintStackTrace_onSQLException() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
                .thenThrow(new java.sql.SQLException("DB error"));

        // We create a spy so we can inject a mock logger if the Lombok-generated log field is used.
        SqlInjectionLesson6b lesson = new SqlInjectionLesson6b(dataSource);

        // Use reflection to replace the Lombok-generated logger with a mock,
        // so we can assert it is used instead of printStackTrace.
        Logger mockLogger = mock(Logger.class);
        try {
            java.lang.reflect.Field logField =
                    SqlInjectionLesson6b.class.getDeclaredField("log");
            logField.setAccessible(true);
            logField.set(null, mockLogger);
        } catch (NoSuchFieldException e) {
            // If field name differs due to Lombok or build configuration, mark this as a TODO.
            // TODO: Adjust reflection to match actual logger field name if needed.
        }

        // Act
        String password = lesson.getPassword();

        // Assert
        // Even on error, getPassword should return the default "dave"
        assertEquals("dave", password);

        // Verify that an error was logged (and not printed directly)
        verify(mockLogger, atLeastOnce()).error(
                startsWith("Database error during password retrieval."),
                any(Throwable.class));
    }

    @Test
    @DisplayName("getPassword should log unexpected exceptions instead of using printStackTrace")
    void getPassword_logsUnexpectedExceptions() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);

        when(dataSource.getConnection()).thenThrow(new RuntimeException("Unexpected"));

        SqlInjectionLesson6b lesson = new SqlInjectionLesson6b(dataSource);

        Logger mockLogger = mock(Logger.class);
        try {
            java.lang.reflect.Field logField =
                    SqlInjectionLesson6b.class.getDeclaredField("log");
            logField.setAccessible(true);
            logField.set(null, mockLogger);
        } catch (NoSuchFieldException e) {
            // TODO: Adjust reflection to match actual logger field name if needed.
        }

        // Act
        String password = lesson.getPassword();

        // Assert
        assertEquals("dave", password);
        verify(mockLogger, atLeastOnce()).error(
                startsWith("Unexpected error during password retrieval."),
                any(Throwable.class));
    }
}
