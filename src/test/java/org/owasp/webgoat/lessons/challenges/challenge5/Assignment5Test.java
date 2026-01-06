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
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

/**
 * Delta unit tests focusing on the SQL injection fix in Assignment5.login().
 *
 * These tests assert:
 *  - SQL parameters are now bound via PreparedStatement (no concatenated SQL).
 *  - Functional behavior for valid/invalid credentials is preserved.
 */
class Assignment5Test {

    @Test
    @DisplayName("login should bind username and password as PreparedStatement parameters")
    void login_usesPreparedStatementParameters_insteadOfConcatenation() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Flags flags = mock(Flags.class);
        when(flags.getFlag(5)).thenReturn("FLAG-5");

        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true); // simulate successful login

        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        String username = "Larry";
        String password = "correct-password";

        // Act
        AttackResult result = assignment5.login(username, password);

        // Assert
        // Verify that the query uses parameter placeholders instead of concatenated user input
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(connection).prepareStatement(sqlCaptor.capture());
        String usedSql = sqlCaptor.getValue();
        // The fixed code must use '?' placeholders for the `userid` and `password` values
        // (We don't assert the exact whole query string to keep the test focused on the delta behavior.)
        org.junit.jupiter.api.Assertions.assertTrue(
                usedSql.contains("userid = ?") && usedSql.contains("password = ?"),
                "SQL must use parameter placeholders instead of inlined user input");

        // Verify that user-supplied values are bound as parameters
        verify(ps).setString(1, username);
        verify(ps).setString(2, password);

        // Functional outcome should still be success for a matching row
        org.junit.jupiter.api.Assertions.assertTrue(result.getLessonCompleted());
    }

    @Test
    @DisplayName("login should fail when credentials are invalid while still using prepared parameters")
    void login_invalidCredentials_stillUseParameters_andFail() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Flags flags = mock(Flags.class);

        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // No rows returned = invalid credentials
        when(rs.next()).thenReturn(false);

        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        String username = "Larry";
        String password = "wrong-password";

        // Act
        AttackResult result = assignment5.login(username, password);

        // Assert
        verify(ps).setString(1, username);
        verify(ps).setString(2, password);
        org.junit.jupiter.api.Assertions.assertFalse(result.getLessonCompleted());
    }

    @Test
    @DisplayName("login should reject empty username or password (unchanged behavior but guards injection attempts)")
    void login_emptyInputs_rejectedBeforeQueryExecution() throws Exception {
        // Arrange
        LessonDataSource dataSource = mock(LessonDataSource.class);
        Flags flags = mock(Flags.class);
        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        // Act
        AttackResult result1 = assignment5.login("", "somePwd");
        AttackResult result2 = assignment5.login("Larry", "");

        // Assert
        // No DB interaction should happen when inputs are empty
        verifyNoInteractions(dataSource);
        org.junit.jupiter.api.Assertions.assertFalse(result1.getLessonCompleted());
        org.junit.jupiter.api.Assertions.assertFalse(result2.getLessonCompleted());
    }
}
