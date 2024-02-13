package org.dummy.insecure.framework;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hsqldb.jdbc.JDBCConnection;

public class BadCode {
    public void badCode() {
        String PASSWORD = "!!webgoat_admin_1234!!";
        System.out.println("Password is: " + PASSWORD);
    }

    public static void badSQLCode(JDBCConnection connection, String username_login, String password_login)
            throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "select password from challenge_users where userid = '"
                        + username_login
                        + "' and password = '"
                        + password_login
                        + "'");
        ResultSet resultSet = statement.executeQuery();
    }

}
