package org.owasp.webgoat;

import java.sql.*;
import java.io.*;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.Random;

/**
 * SECURITY WARNING: This file contains intentionally vulnerable code for testing purposes.
 * DO NOT use any of these patterns in production code!
 * 
 * This class demonstrates various security vulnerabilities that CodeQL should detect.
 */
public class VulnerableTestCases {
    
    private static final Logger logger = Logger.getLogger(VulnerableTestCases.class.getName());
    private Connection connection;
    
    // 1. SQL Injection - Direct concatenation
    public ResultSet getUserById(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = " + userId;
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }
    
    // 2. SQL Injection - String concatenation in WHERE clause
    public ResultSet searchUsers(String username, String email) throws SQLException {
        String query = "SELECT * FROM users WHERE username = '" + username + "' AND email = '" + email + "'";
        return connection.createStatement().executeQuery(query);
    }
    
    // 3. Hard-coded password
    public boolean authenticateAdmin() {
        String adminPassword = "admin123"; // Hard-coded credential
        String dbPassword = "jdbc:mysql://localhost:3306/webgoat?password=secretpassword";
        return true;
    }
    
    // 4. Sensitive information in logs
    public void loginUser(String username, String password) {
        logger.info("User login attempt: " + username + " with password: " + password);
        logger.warning("Authentication failed for user: " + username + ", password was: " + password);
    }
    
    // 5. Weak cryptographic algorithm
    public String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5"); // Weak algorithm
        byte[] hash = md.digest(password.getBytes());
        return new String(hash);
    }
    
    // 6. Path traversal vulnerability
    public File readUserFile(String filename) {
        String basePath = "/app/uploads/";
        File file = new File(basePath + filename); // No validation
        return file;
    }
    
    // 7. Weak random number generation
    public String generateSessionToken() {
        Random random = new Random(); // Weak random
        return String.valueOf(random.nextInt(999999));
    }
    
    // 8. XSS vulnerability via response writing
    public void displayUserInput(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userInput = request.getParameter("input");
        PrintWriter out = response.getWriter();
        out.println("<div>User input: " + userInput + "</div>"); // No escaping
    }
    
    // 9. Command injection
    public void executeSystemCommand(String userCommand) throws IOException {
        String command = "ls -la " + userCommand; // Command injection
        Runtime.getRuntime().exec(command);
    }
    
    // 10. LDAP injection
    public void searchLDAP(String username) {
        String filter = "(uid=" + username + ")"; // LDAP injection
        // Simulated LDAP search
        logger.info("LDAP search with filter: " + filter);
    }
    
    // 11. Information disclosure through error messages
    public void connectToDatabase(String host, String username, String password) {
        try {
            String url = "jdbc:mysql://" + host + ":3306/webgoat";
            DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // Exposing sensitive information in error message
            throw new RuntimeException("Database connection failed: " + e.getMessage() + 
                                     " for host: " + host + " with credentials: " + username + "/" + password);
        }
    }
    
    // 12. Insecure deserialization
    public Object deserializeUserData(String serializedData) throws Exception {
        byte[] data = java.util.Base64.getDecoder().decode(serializedData);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        return ois.readObject(); // Unsafe deserialization
    }
    
    // 13. API key exposure
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE"; // Exposed API key
    private static final String SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    
    public void configureAWS() {
        System.setProperty("aws.accessKeyId", AWS_ACCESS_KEY);
        System.setProperty("aws.secretKey", SECRET_KEY);
        logger.info("AWS configured with key: " + AWS_ACCESS_KEY);
    }
}
