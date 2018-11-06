
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
@AssignmentHints(value = {"SqlStringInjectionHint8-1", "SqlStringInjectionHint8-2", "SqlStringInjectionHint8-3", "SqlStringInjectionHint8-4", "SqlStringInjectionHint8-5"})
public class SqlInjectionLesson8 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
        return injectableQueryConfidentiality(name, auth_tan);
    }

    protected AttackResult injectableQueryConfidentiality(String name, String auth_tan) {
        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());
            String query = "SELECT * FROM employees WHERE last_name = '" + name + "' AND auth_tan = '" + auth_tan + "'";

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                log(connection, query);
                ResultSet results = statement.executeQuery(query);

                if ((results != null) && (results.first())) {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    StringBuffer output = new StringBuffer();

                    output.append(generateTable(results, resultsMetaData));
                    results.last();

                    // If they get back more than one user they succeeded
                    if (results.getRow() > 1) {
                        return trackProgress(success().feedback("sql-injection.8.success").feedbackArgs(output.toString()).build());
                    } else {
                        return trackProgress(failed().output(output.toString()).build());
                    }
                } else {
                    return trackProgress(failed().feedback("sql-injection.8.no.results").build());
                }
            } catch (SQLException e) {
                return trackProgress(failed().output(e.getMessage()).build());
            }

        } catch (Exception e) {
            return trackProgress(failed().output(this.getClass().getName() + " : " + e.getMessage()).build());
        }
    }

    public static String generateTable(ResultSet results, ResultSetMetaData resultsMetaData) throws SQLException {
        int numColumns = resultsMetaData.getColumnCount();
        results.beforeFirst();
        StringBuffer t = new StringBuffer();
        t.append("<table>");

        if (results.next()) {
            t.append("<tr>");
            for (int i = 1; i < (numColumns + 1); i++) {
                t.append("<th>" + resultsMetaData.getColumnName(i) + "</th>");
            }
            t.append("</tr>");

            results.beforeFirst();
            while (results.next()) {
                t.append("<tr>");
                for (int i = 1; i < (numColumns + 1); i++) {
                    System.out.println(results.getString(i));
                    t.append("<td>" + results.getString(i) + "</td>");
                }
                t.append("</tr>");
            }

        } else {
            t.append("Query Successful; however no data was returned from this query.");
        }

        t.append("</table>");
        return (t.toString());
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
