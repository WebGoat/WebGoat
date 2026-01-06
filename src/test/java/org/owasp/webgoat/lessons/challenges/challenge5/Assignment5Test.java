// Assumed test source root: src/test/java
// Package inferred from source file: org.owasp.webgoat.lessons.challenges.challenge5
package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.owasp.webgoat.container.assignments.AttackResult.Status.SUCCESS;
import static org.owasp.webgoat.container.assignments.AttackResult.Status.FAIL;

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
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.util.StringUtils;

/**
 * Delta tests for Assignment5 focusing on the change from string-concatenated SQL
 * to a parameterized PreparedStatement preventing SQL injection.
 */
class Assignment5Test {

  @Test
  @DisplayName("login should use parameterized PreparedStatement instead of concatenated SQL")
  void loginUsesParameterizedPreparedStatement() throws Exception {
    LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
    Flags flags = Mockito.mock(Flags.class);
    Assignment5 assignment = new Assignment5(dataSource, flags);

    Connection connection = Mockito.mock(Connection.class);
    PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
    ResultSet resultSet = Mockito.mock(ResultSet.class);

    Mockito.when(dataSource.getConnection()).thenReturn(connection);
    Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
    Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
    Mockito.when(resultSet.next()).thenReturn(true);
    Mockito.when(flags.getFlag(5)).thenReturn("FLAG-5");

    String username = "Larry";
    String password = "safePass' OR '1'='1"; // attempt injection, should be treated as data

    AttackResult result = assignment.login(username, password);

    // Verify that a parameterized query is used
    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(connection).prepareStatement(sqlCaptor.capture());
    String sql = sqlCaptor.getValue();
    assertThat(sql)
        .isEqualTo("select password from challenge_users where userid = ? and password = ?");

    // Verify parameters are bound to the placeholders, not concatenated
    Mockito.verify(preparedStatement).setString(1, username);
    Mockito.verify(preparedStatement).setString(2, password);

    // Verify successful path still works with correct credentials
    assertThat(result.getStatus()).isEqualTo(SUCCESS);
  }

  @Test
  @DisplayName("login should handle SQLException gracefully and not leak details")
  void loginHandlesSqlExceptionGracefully() throws Exception {
    LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
    Flags flags = Mockito.mock(Flags.class);
    Assignment5 assignment = new Assignment5(dataSource, flags);

    Mockito.when(dataSource.getConnection()).thenThrow(new SQLException("DB down"));

    AttackResult result = assignment.login("Larry", "anyPassword");

    assertThat(result.getStatus()).isEqualTo(FAIL);
    assertThat(result.getFeedback()).contains("internal error").doesNotContain("DB down");
  }

  @Test
  @DisplayName("login should still reject non-Larry users (behavior preserved)")
  void loginStillRejectsNonLarryUser() throws Exception {
    LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
    Flags flags = Mockito.mock(Flags.class);
    Assignment5 assignment = new Assignment5(dataSource, flags);

    AttackResult result = assignment.login("Bob", "pwd");

    assertThat(result.getStatus()).isEqualTo(FAIL);
  }
}
