
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

@AssignmentPath("/SqlInjection/attack9")
@AssignmentHints(value = {"SqlStringInjectionHint.9.1", "SqlStringInjectionHint.9.2", "SqlStringInjectionHint.9.3", "SqlStringInjectionHint.9.4", "SqlStringInjectionHint.9.5"})
public class SqlInjectionLesson9 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
        return injectableQueryIntegrity(name, auth_tan);
    }

    protected AttackResult injectableQueryIntegrity(String name, String auth_tan) {
        StringBuffer output = new StringBuffer();
        String query = "SELECT * FROM employees WHERE last_name = '" + name + "' AND auth_tan = '" + auth_tan + "'";

        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                SqlInjectionLesson8.log(connection, query);
                ResultSet results = statement.executeQuery(query);

                if (results.getStatement() != null) {
                    if (results.first()) {
                        output.append(SqlInjectionLesson8.generateTable(results));
                    } else {
                        // no results
                        return trackProgress(failed().feedback("sql-injection.8.no.results").build());
                    }

                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return trackProgress(failed().feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build());
            }

            return checkSalaryRanking(connection, output);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return trackProgress(failed().feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build());
        }
    }

    private AttackResult checkSalaryRanking(Connection connection, StringBuffer output) {
        try {
            String query = "SELECT * FROM employees ORDER BY salary DESC";
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = statement.executeQuery(query);

            results.first();
            // user completes lesson if John Smith is the first in the list
            if ((results.getString(2).equals("John")) && (results.getString(3).equals("Smith"))) {
                output.append(SqlInjectionLesson8.generateTable(results));
                return trackProgress(success().feedback("sql-injection.9.success").output(output.toString()).build());
            } else {
                return trackProgress(failed().feedback("sql-injection.9.one").output(output.toString()).build());
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return trackProgress(failed().feedback("sql-injection.error").output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>").build());
        }
    }

}
