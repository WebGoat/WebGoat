package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;

/**
 * Delta tests for Assignment5 focusing on the SQL injection fix:
 *  - query must be parameterized (no concatenation of username/password)
 *  - valid Larry credentials still succeed and return the flag
 *  - invalid credentials do not authenticate
 */
class Assignment5Test {

    @Test
    @DisplayName("Valid Larry credentials use parameterized query and return success with flag")
    void testValidLarryCredentialsUseParameterizedQueryAndSucceed() throws Exception {
        // Arrange
        String username = "Larry";
        String password = "correct-password";
        String expectedFlag = "FLAG-5";

        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        Flags flags = mock(Flags.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(
                "select password from challenge_users where userid = ? and password = ?"))
            .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // simulate successful match
        when(flags.getFlag(5)).thenReturn(expectedFlag);

        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        // Act
        AttackResult result = assignment5.login(username, password);

        // Assert
        // 1) Ensure parameterized SQL is used (no concatenation)
        verify(connection, times(1))
            .prepareStatement("select password from challenge_users where userid = ? and password = ?");

        // 2) Ensure user-controlled input is passed via parameters, not concatenated
        verify(preparedStatement, times(1)).setString(1, username);
        verify(preparedStatement, times(1)).setString(2, password);

        // 3) Ensure query is executed
        verify(preparedStatement, times(1)).executeQuery();

        // 4) Functional behavior: success with flag for correct Larry credentials
        assertThat(result).isNotNull();
        assertThat(result.getLessonCompleted()).isTrue();
        assertThat(result.getFeedback()).contains(expectedFlag);
    }

    @Test
    @DisplayName("Invalid Larry password does not authenticate and still uses parameterized query")
    void testInvalidLarryPasswordDoesNotAuthenticate() throws Exception {
        // Arrange
        String username = "Larry";
        String password = "wrong-password";

        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        Flags flags = mock(Flags.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(
                "select password from challenge_users where userid = ? and password = ?"))
            .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // simulate invalid credentials

        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        // Act
        AttackResult result = assignment5.login(username, password);

        // Assert
        // Still must use parameterized query
        verify(connection, times(1))
            .prepareStatement("select password from challenge_users where userid = ? and password = ?");
        verify(preparedStatement, times(1)).setString(1, username);
        verify(preparedStatement, times(1)).setString(2, password);
        verify(preparedStatement, times(1)).executeQuery();

        // Functional behavior: login should fail
        assertThat(result).isNotNull();
        assertThat(result.getLessonCompleted()).isFalse();
    }

    @Test
    @DisplayName("Non-Larry username is rejected before hitting database")
    void testNonLarryUserRejectedBeforeDatabase() throws Exception {
        // Arrange
        String username = "Mallory";
        String password = "anything";

        LessonDataSource dataSource = mock(LessonDataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        Flags flags = mock(Flags.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(
                "select password from challenge_users where userid = ? and password = ?"))
            .thenReturn(preparedStatement);

        Assignment5 assignment5 = new Assignment5(dataSource, flags);

        // Act
        AttackResult result = assignment5.login(username, password);

        // Assert
        // For non-Larry, the method should short-circuit and never touch the database
        verifyNoInteractions(connection);
        verifyNoInteractions(preparedStatement);

        assertThat(result).isNotNull();
        assertThat(result.getLessonCompleted()).isFalse();
    }
}
