
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
@AssignmentHints(value = {"SqlStringInjectionHint9-1", "SqlStringInjectionHint9-2", "SqlStringInjectionHint9-3", "SqlStringInjectionHint9-4", "SqlStringInjectionHint9-5"})
public class SqlInjectionLesson9 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
        return injectableQueryIntegrity(name, auth_tan);
    }

    protected AttackResult injectableQueryIntegrity(String name, String auth_tan) {
        StringBuffer output = new StringBuffer();
        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());

            try {
                String query = "SELECT * FROM employees WHERE last_name = '" + name + "' AND auth_tan = '" + auth_tan + "'";
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                SqlInjectionLesson8.log(connection, query);
                ResultSet results = statement.executeQuery(query);

                if (results != null && results.first()) {
                    output.append(SqlInjectionLesson8.generateTable(results, results.getMetaData()));
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return checkSalaryRanking(connection, output);
            }

            return checkSalaryRanking(connection, output);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return trackProgress(failed().output("<br><span style='color: red;'>" + this.getClass().getName() + " : " + e.getMessage() + "</span>").build());
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
                output.append(SqlInjectionLesson8.generateTable(results, results.getMetaData()));
                return trackProgress(success().feedback("sql-injection.8.success").feedbackArgs(output.toString()).build());
            } else {
                return trackProgress(failed().output(output.toString()).build());
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return trackProgress(failed().output("<br><span style='color: red;'>" + e.getMessage() + "</span>").build());
        }
    }

}
