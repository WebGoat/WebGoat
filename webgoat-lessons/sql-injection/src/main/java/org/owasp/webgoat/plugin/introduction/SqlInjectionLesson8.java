
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
import java.util.Calendar;
import java.text.SimpleDateFormat;

import java.sql.*;

@AssignmentPath("/SqlInjection/attack8")
@AssignmentHints(value = {"SqlStringInjectionHint.8.1", "SqlStringInjectionHint.8.2", "SqlStringInjectionHint.8.3", "SqlStringInjectionHint.8.4", "SqlStringInjectionHint.8.5"})
public class SqlInjectionLesson8 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
        return injectableQueryConfidentiality(name, auth_tan);
    }

    protected AttackResult injectableQueryConfidentiality(String name, String auth_tan) {
        StringBuffer output = new StringBuffer();
        String query = "SELECT * FROM employees WHERE last_name = '" + name + "' AND auth_tan = '" + auth_tan + "'";

        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                log(connection, query);
                ResultSet results = statement.executeQuery(query);

                if (results.getStatement() != null) {
                    if (results.first()) {
                        output.append(generateTable(results));
                        results.last();

                        if (results.getRow() > 1) {
                            // more than one record, the user succeeded
                            return trackProgress(success().feedback("sql-injection.8.success").output(output.toString()).build());
                        } else {
                            // only one record
                            return trackProgress(failed().feedback("sql-injection.8.one").output(output.toString()).build());
                        }

                    } else {
                        // no results
                        return trackProgress(failed().feedback("sql-injection.8.no.results").build());
                    }
                } else {
                    return trackProgress(failed().feedback("sql-injection.error").build());
                }
            } catch (SQLException e) {
                return trackProgress(failed().feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build());
            }

        } catch (Exception e) {
            return trackProgress(failed().feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build());
        }
    }

    public static String generateTable(ResultSet results) throws SQLException {
        ResultSetMetaData resultsMetaData = results.getMetaData();
        int numColumns = resultsMetaData.getColumnCount();
        results.beforeFirst();
        StringBuffer table = new StringBuffer();
        table.append("<table>");

        if (results.next()) {
            table.append("<tr>");
            for (int i = 1; i < (numColumns + 1); i++) {
                table.append("<th>" + resultsMetaData.getColumnName(i) + "</th>");
            }
            table.append("</tr>");

            results.beforeFirst();
            while (results.next()) {
                table.append("<tr>");
                for (int i = 1; i < (numColumns + 1); i++) {
                    table.append("<td>" + results.getString(i) + "</td>");
                }
                table.append("</tr>");
            }

        } else {
            table.append("Query Successful; however no data was returned from this query.");
        }

        table.append("</table>");
        return (table.toString());
    }

    public static void log(Connection connection, String action) {
        action = action.replace('\'', '"');
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(cal.getTime());

        String log_query = "INSERT INTO access_log (time, action) VALUES ('" + time + "', '" + action + "')";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(log_query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
