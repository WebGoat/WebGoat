// Derived from: src/main/java/org/owasp/webgoat/lessons/sqlinjection/advanced/SqlInjectionChallenge.java
// Test path assumption: src/test/java/org/owasp/webgoat/lessons/sqlinjection/advanced/SqlInjectionChallengeTest.java
package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta unit tests for SqlInjectionChallenge focusing on the change from
 * a concatenated SQL Statement to a parameterized PreparedStatement.
 */
public class SqlInjectionChallengeTest {

  @Test
  @DisplayName("registerNewUser should use PreparedStatement with placeholder for username in existence check")
  void registerNewUser_usesPreparedStatementForUserCheck() {
    // Arrange
    LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = Mockito.mock(Connection.class);
    PreparedStatement checkUserPs = Mockito.mock(PreparedStatement.class);
    PreparedStatement insertPs = Mockito.mock(PreparedStatement.class);
    ResultSet resultSet = Mockito.mock(ResultSet.class);

    Mockito.when(dataSource.getConnection()).thenReturn(connection);
    // First prepareStatement is the checkUserQuery, second is the insert
    Mockito
        .when(connection.prepareStatement(anyString()))
        .thenReturn(checkUserPs)
        .thenReturn(insertPs);
    Mockito.when(checkUserPs.executeQuery()).thenReturn(resultSet);
    Mockito.when(resultSet.next()).thenReturn(false); // user does not exist yet

    String username = "newuser' OR '1'='1";
    String email = "test@example.com";
    String password = "secret";

    // Act
    AttackResult result = challenge.registerNewUser(username, email, password);

    // Assert
    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(connection).prepareStatement(sqlCaptor.capture());
    String usedSql = sqlCaptor.getValue();

    // The query must now use a parameter placeholder, not inline username
    assertEquals(
        "select userid from sql_challenge_users where userid = ?",
        usedSql
    );

    // Verify that username was bound as a parameter and not concatenated
    Mockito.verify(checkUserPs).setString(1, username);
    Mockito.verify(checkUserPs).executeQuery();

    // Ensure that createStatement with raw SQL is not used
    Mockito.verify(connection, never()).createStatement();

    // Behavior check: user should be created successfully, as before
    // TODO: If AttackResult exposes success indicator or message, assert the creation outcome here.
    Mockito.verify(insertPs).setString(1, username);
    Mockito.verify(insertPs).setString(2, email);
    Mockito.verify(insertPs).setString(3, password);
  }
}
