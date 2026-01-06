// Assumed test source root: src/test/java
// Package inferred from source file: org.owasp.webgoat.lessons.sqlinjection.advanced
package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.assertj.core.api.Assertions.assertThat;
import static org.owasp.webgoat.container.assignments.AttackResult.Status.FAIL;
import static org.owasp.webgoat.container.assignments.AttackResult.Status.SUCCESS; // for informationMessage status

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for SqlInjectionChallenge focusing on the change from string-concatenated
 * user check query to a parameterized PreparedStatement.
 */
class SqlInjectionChallengeTest {

  @Test
  @DisplayName("registerNewUser should use PreparedStatement with placeholder for username (no concatenation)")
  void registerNewUserUsesPreparedStatementForUserCheck() throws Exception {
    LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Connection connection = Mockito.mock(Connection.class);
    PreparedStatement checkStmt = Mockito.mock(PreparedStatement.class);
    PreparedStatement insertStmt = Mockito.mock(PreparedStatement.class);
    ResultSet checkResult = Mockito.mock(ResultSet.class);

    Mockito.when(dataSource.getConnection()).thenReturn(connection);
    // First prepareStatement call: user existence check
    Mockito.when(connection.prepareStatement(Mockito.startsWith("select userid"))).thenReturn(checkStmt);
    // Second prepareStatement call: insert
    Mockito.when(connection.prepareStatement(Mockito.startsWith("INSERT INTO sql_challenge_users")))
        .thenReturn(insertStmt);

    Mockito.when(checkStmt.executeQuery()).thenReturn(checkResult);
    Mockito.when(checkResult.next()).thenReturn(false);

    String username = "victim' OR '1'='1";
    String email = "user@example.com";
    String password = "pwd";

    AttackResult result = challenge.registerNewUser(username, email, password);

    // Verify that a parameterized query is used for the user check
    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(connection).prepareStatement(sqlCaptor.capture());
    String sql = sqlCaptor.getValue();
    assertThat(sql).isEqualTo("select userid from sql_challenge_users where userid = ?");

    // Verify the potentially malicious username is passed as a bound parameter
    Mockito.verify(checkStmt).setString(1, username);

    assertThat(result.getStatus()).isEqualTo(SUCCESS);
  }

  @Test
  @DisplayName("registerNewUser should still reject empty inputs (behavior preserved)")
  void registerNewUserRejectsEmptyInputs() {
    LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    AttackResult result = challenge.registerNewUser("", "e", "p");

    assertThat(result.getStatus()).isEqualTo(FAIL);
  }

  @Test
  @DisplayName("registerNewUser handles SQLException from DB layer gracefully")
  void registerNewUserHandlesSqlExceptionGracefully() throws Exception {
    LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
    SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

    Mockito.when(dataSource.getConnection()).thenThrow(new SQLException("db failure"));

    AttackResult result = challenge.registerNewUser("user", "e@x.com", "pwd");

    assertThat(result.getStatus()).isEqualTo(FAIL);
    assertThat(result.getOutput()).contains("Something went wrong");
  }
}
