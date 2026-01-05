package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.owasp.webgoat.container.LessonDataSource;

public class SqlInjectionChallengeTest {

    @Test
    void registerNewUser_shouldUsePreparedStatementForUserExistenceCheck() throws Exception {
        LessonDataSource dataSource = Mockito.mock(LessonDataSource.class);
        SqlInjectionChallenge challenge = new SqlInjectionChallenge(dataSource);

        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement checkUserPs = Mockito.mock(PreparedStatement.class);
        PreparedStatement insertPs = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.eq("select userid from sql_challenge_users where userid = ?")))
                .thenReturn(checkUserPs);
        Mockito.when(connection.prepareStatement(Mockito.eq("INSERT INTO sql_challenge_users VALUES (?, ?, ?)")))
                .thenReturn(insertPs);

        Mockito.when(checkUserPs.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        String username = "alice";
        String email = "alice@example.com";
        String password = "s3cr3t";

        challenge.registerNewUser(username, email, password);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(connection).prepareStatement(sqlCaptor.capture());
        String usedSql = sqlCaptor.getValue();
        assertEquals(
                "select userid from sql_challenge_users where userid = ?",
                usedSql);

        verify(checkUserPs).setString(1, username);
    }
}
