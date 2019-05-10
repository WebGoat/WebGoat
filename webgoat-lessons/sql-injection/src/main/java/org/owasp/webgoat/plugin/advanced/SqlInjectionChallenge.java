package org.owasp.webgoat.plugin.advanced;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@AssignmentPath("SqlInjection/challenge")
@AssignmentHints(value = {"SqlInjectionChallenge1", "SqlInjectionChallenge2", "SqlInjectionChallenge3"})
@Slf4j
public class SqlInjectionChallenge extends AssignmentEndpoint {

    private static final String PASSWORD_TOM = "thisisasecretfortomonly";
    //Make it more random at runtime (good luck guessing)
    static final String USERS_TABLE_NAME = "challenge_users_6" + RandomStringUtils.randomAlphabetic(16);

    @Autowired
    private WebSession webSession;

    public SqlInjectionChallenge() {
        log.info("Challenge 6 tablename is: {}", USERS_TABLE_NAME);
    }

    @PutMapping  //assignment path is bounded to class so we use different http method :-)
    @ResponseBody
    public AttackResult registerNewUser(@RequestParam String username_reg, @RequestParam String email_reg, @RequestParam String password_reg) throws Exception {
        AttackResult attackResult = checkArguments(username_reg, email_reg, password_reg);

        if (attackResult == null) {
            Connection connection = DatabaseUtilities.getConnection(webSession);
            checkDatabase(connection);

            try {
                String checkUserQuery = "select userid from " + USERS_TABLE_NAME + " where userid = '" + username_reg + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(checkUserQuery);

                if (resultSet.next()) {
                    attackResult = failed().feedback("user.exists").feedbackArgs(username_reg).build();
                } else {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + USERS_TABLE_NAME + " VALUES (?, ?, ?)");
                    preparedStatement.setString(1, username_reg);
                    preparedStatement.setString(2, email_reg);
                    preparedStatement.setString(3, password_reg);
                    preparedStatement.execute();
                    attackResult = success().feedback("user.created").feedbackArgs(username_reg).build();
                }
            } catch(SQLException e) {
                attackResult = failed().output("Something went wrong").build();
            }
            }
            return attackResult;
    }

    private AttackResult checkArguments(String username_reg, String email_reg, String password_reg) {
        if (StringUtils.isEmpty(username_reg) || StringUtils.isEmpty(email_reg) || StringUtils.isEmpty(password_reg)) {
            return failed().feedback("input.invalid").build();
        }
        if (username_reg.length() > 250 || email_reg.length() > 30 || password_reg.length() > 30) {
            return failed().feedback("input.invalid").build();
        }
        return null;
    }

    static void checkDatabase(Connection connection) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            System.out.println(USERS_TABLE_NAME);
            statement.execute("select 1 from " + USERS_TABLE_NAME);
        } catch (SQLException e) {
            createChallengeTable(connection);
        }
    }

    static void createChallengeTable(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String dropTable = "DROP TABLE " + USERS_TABLE_NAME;
            statement.executeUpdate(dropTable);
        } catch (SQLException e) {
            log.info("Delete failed, this does not point to an error table might not have been present...");
        }
        log.debug("Challenge 6 - Creating tables for users {}", USERS_TABLE_NAME);
        try {
            String createTableStatement = "CREATE TABLE " + USERS_TABLE_NAME
                    + " (" + "userid varchar(250),"
                    + "email varchar(30),"
                    + "password varchar(30)"
                    + ")";
            statement.executeUpdate(createTableStatement);

            String insertData1 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('larry', 'larry@webgoat.org', 'larryknows')";
            String insertData2 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('tom', 'tom@webgoat.org', '" + PASSWORD_TOM + "')";
            String insertData3 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('alice', 'alice@webgoat.org', 'rt*(KJ()LP())$#**')";
            String insertData4 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('eve', 'eve@webgoat.org', '**********')";
            statement.executeUpdate(insertData1);
            statement.executeUpdate(insertData2);
            statement.executeUpdate(insertData3);
            statement.executeUpdate(insertData4);
        } catch (SQLException e) {
            log.error("Unable create table", e);
        }
    }
}

