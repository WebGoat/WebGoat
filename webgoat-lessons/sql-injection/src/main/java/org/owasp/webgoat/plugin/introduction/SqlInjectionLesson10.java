
package org.owasp.webgoat.plugin.introduction;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;

@AssignmentPath("/SqlInjection/attack10")
@AssignmentHints(value = {"SqlStringInjectionHint.10.1", "SqlStringInjectionHint.10.2", "SqlStringInjectionHint.10.3", "SqlStringInjectionHint.10.4", "SqlStringInjectionHint.10.5", "SqlStringInjectionHint.10.6"})
public class SqlInjectionLesson10 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String action_string) {
        return injectableQueryAvailability(action_string);
    }

    protected AttackResult injectableQueryAvailability(String action) {
        StringBuffer output = new StringBuffer();
        String query = "SELECT * FROM access_log WHERE action LIKE '%" + action + "%'";

        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);

                if (results.getStatement() != null) {
                    results.first();
                    output.append(SqlInjectionLesson8.generateTable(results));
                    return trackProgress(failed().feedback("sql-injection.10.entries").output(output.toString()).build());
                } else {
                    if (tableExists(connection)) {
                        return trackProgress(failed().feedback("sql-injection.10.entries").output(output.toString()).build());
                    }
                    else {
                        return trackProgress(success().feedback("sql-injection.10.success").build());
                    }
                }
            } catch (SQLException e) {
                if (tableExists(connection)) {
                    return trackProgress(failed().feedback("sql-injection.error").output("<span class='feedback-negative'>" + e.getMessage() + "</span><br>" + output.toString()).build());
                }
                else {
                    return trackProgress(success().feedback("sql-injection.10.success").build());
                }
            }

        } catch (Exception e) {
            return trackProgress(failed().output("<span class='feedback-negative'>" + e.getMessage() + "</span>").build());
        }
    }

    private boolean tableExists(Connection connection) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM access_log");
            int cols = results.getMetaData().getColumnCount();
            return (cols > 0);
        } catch (SQLException e) {
            String error_msg = e.getMessage();
            if (error_msg.contains("object not found: ACCESS_LOG")) {
                return false;
            } else {
                System.err.println(e.getMessage());
                return false;
            }
        }
    }

}
