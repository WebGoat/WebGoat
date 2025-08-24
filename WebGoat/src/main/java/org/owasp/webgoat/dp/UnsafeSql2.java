package org.owasp.webgoat.dp;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UnsafeSql2 {

    // Intentional SQL Injection (CWE-89) for testing DP new findings on PRs
    public void vulnerableQuery(HttpServletRequest request) throws Exception {
        String username = request.getParameter("username"); // tainted input

        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = conn.createStatement();

        // CWE-89: concatenation of user input into SQL
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        stmt.execute(sql);

        stmt.close();
        conn.close();
    }
}

