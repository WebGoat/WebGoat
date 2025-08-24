package org.owasp.webgoat.dp;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UnsafeSql {

    // Intentional SQL Injection vulnerability - change for pr
    public void doQuery(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id"); // unvalidated user input

        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = conn.createStatement();

        // CWE-89: SQL Injection vulnerability
        stmt.execute("SELECT * FROM users WHERE id=" + id);

        stmt.close();
        conn.close();
    }
}
