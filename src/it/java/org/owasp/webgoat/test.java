import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLInjectionExample {
    public static void main(String[] args) {
        String username = "admin"; // Example input (could be attacker-controlled)
        String password = "password123"; // Example input (could be attacker-controlled)

        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "password");

            // Vulnerable SQL query (without input sanitization)
            String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";

            // Execute query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid username or password.");
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

