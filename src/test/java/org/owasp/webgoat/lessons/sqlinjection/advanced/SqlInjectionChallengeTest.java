package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for SqlInjectionChallenge focusing on the refactored, parameterized
 * user existence check in registerNewUser().
 *
 * These tests verify:
 * 1) The existence check uses a parameterized PreparedStatement with setString(1, username).
 * 2) Existing user path returns 'user.exists' feedback.
 * 3) Non-existing user path executes the insert PreparedStatement and returns 'user.created'.
 */
class SqlInjectionChallengeTest {

  private static final String CHECK_USER_SQL =
      "select userid from sql_challenge_users where userid = ?";

  @Test
  @DisplayName("When user already exists, parameterized check is used and user.exists is returned")
  void testExistingUserUsesParameterizedQueryAndReturnsUserExists() throws Exception {
    // Arrange
    String username = "alice";
    String email = "alice@example.com";
    String password = "secret";

    LessonDataSource dataSource = mock(LessonDataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement checkUserStmt = mock(PreparedStatement.class);
    ResultSet checkUserRs = mock(ResultSet.class);
    PreparedStatement insertStmt = mock(PreparedStatement.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(CHECK_USER_SQL)).thenReturn(checkUserStmt);
    when(checkUserStmt.executeQuery()).thenReturn(checkUserRs);
    when(checkUserRs.next()).thenReturn(true); // user already exists

    // INSERT statement should not be used in this path, but we mock it defensively
    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertStmt);

    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    // Act
    AttackResult result = challenge.registerNewUser(username, email, password);

    // Assert
    // Verify parameterized query was used, not concatenated SQL
    verify(connection, times(1)).prepareStatement(CHECK_USER_SQL);
    verify(checkUserStmt, times(1)).setString(1, username);
    verify(checkUserStmt, times(1)).executeQuery();

    // Ensure insert is NOT executed when user exists
    verify(insertStmt, never()).execute();

    assertThat(result).isNotNull();
    assertThat(result.getLessonCompleted()).isFalse();
    assertThat(result.getFeedback()).contains("user.exists");
    assertThat(result.getFeedbackArgs()).contains(username);
  }

  @Test
  @DisplayName("When user does not exist, parameterized check and insert are executed and user.created is returned")
  void testNewUserUsesParameterizedCheckThenInsertAndReturnsUserCreated() throws Exception {
    // Arrange
    String username = "bob";
    String email = "bob@example.com";
    String password = "password123";

    LessonDataSource dataSource = mock(LessonDataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement checkUserStmt = mock(PreparedStatement.class);
    ResultSet checkUserRs = mock(ResultSet.class);
    PreparedStatement insertStmt = mock(PreparedStatement.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(CHECK_USER_SQL)).thenReturn(checkUserStmt);
    when(checkUserStmt.executeQuery()).thenReturn(checkUserRs);
    when(checkUserRs.next()).thenReturn(false); // user does not exist

    when(connection.prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)"))
        .thenReturn(insertStmt);

    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    // Act
    AttackResult result = challenge.registerNewUser(username, email, password);

    // Assert
    // Verify parameterized existence check
    verify(connection, times(1)).prepareStatement(CHECK_USER_SQL);
    verify(checkUserStmt, times(1)).setString(1, username);
    verify(checkUserStmt, times(1)).executeQuery();

    // Verify insert PreparedStatement usage
    verify(connection, times(1))
        .prepareStatement("INSERT INTO sql_challenge_users VALUES (?, ?, ?)");
    verify(insertStmt, times(1)).setString(1, username);
    verify(insertStmt, times(1)).setString(2, email);
    verify(insertStmt, times(1)).setString(3, password);
    verify(insertStmt, times(1)).execute();

    assertThat(result).isNotNull();
    // informationMessage typically sets lessonCompleted=false; we assert on feedback content
    assertThat(result.getFeedback()).contains("user.created");
    assertThat(result.getFeedbackArgs()).contains(username);
  }
}
