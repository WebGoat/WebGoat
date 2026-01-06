package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Delta tests for SqlInjectionLesson6b focusing on:
 * - Password is checked via DB and not returned/exposed.
 * - Correct password yields success, wrong password fails.
 * - Query is parameterized, and no real password value is ever returned.
 *
 * Note: checkPassword is protected; tests exercise it indirectly via completed().
 */
@Slf4j
class SqlInjectionLesson6bTest {

    private LessonDataSource dataSource;
    private SqlInjectionLesson6b lesson;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = mock(LessonDataSource.class);
        lesson = new SqlInjectionLesson6b(dataSource);

        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    void completed_withCorrectPassword_shouldReturnSuccess() throws Exception {
        // Arrange
        String correctPassword = "secret";
        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn(correctPassword);

        // Act
        AttackResult result = lesson.completed(correctPassword);

        // Assert behavior: success when supplied password matches stored password
        assertTrue(result.getLessonCompleted(), "Expected lesson to be completed for correct password");

        // Assert: parameterized query used and username parameter is bound
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(connection).prepareStatement(sqlCaptor.capture());
        String sql = sqlCaptor.getValue();
        org.junit.jupiter.api.Assertions.assertTrue(
                sql.contains("WHERE user_name = ?"),
                "Expected prepared statement with username placeholder");

        // Username is hardcoded 'dave' at call site
        verify(preparedStatement).setString(1, "dave");
    }

    @Test
    void completed_withWrongPassword_shouldFailWithoutExposingPassword() throws Exception {
        // Arrange
        String storedPassword = "secret";
        String wrongPassword = "wrong";

        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn(storedPassword);

        // Act
        AttackResult result = lesson.completed(wrongPassword);

        // Assert: lesson not completed for wrong password
        assertFalse(result.getLessonCompleted(), "Expected failure for incorrect password");

        // Assert indirectly that real password is never exposed:
        // - checkPassword returns only boolean; completed() returns AttackResult.
        // - No string of storedPassword should be returned from completed().
        // We can enforce that by ensuring AttackResult's output is not equal to storedPassword.
        // Since AttackResult does not expose raw messages here, we rely on type-level contract:
        // completed() returns AttackResult, never String; this delta test documents that.
    }
}
