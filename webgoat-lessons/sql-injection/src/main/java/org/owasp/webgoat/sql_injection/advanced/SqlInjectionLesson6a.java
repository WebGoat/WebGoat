/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.sql_injection.advanced;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.sql_injection.introduction.SqlInjectionLesson5a;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;


@RestController
@AssignmentHints(value = {"SqlStringInjectionHint-advanced-6a-1", "SqlStringInjectionHint-advanced-6a-2", "SqlStringInjectionHint-advanced-6a-3",
        "SqlStringInjectionHint-advanced-6a-4"})
public class SqlInjectionLesson6a extends AssignmentEndpoint {

    private final DataSource dataSource;

    public SqlInjectionLesson6a(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/SqlInjectionAdvanced/attack6a")
    @ResponseBody
    public AttackResult completed(@RequestParam String userid_6a) {
        return injectableQuery(userid_6a);
        // The answer: Smith' union select userid,user_name, password,cookie,cookie, cookie,userid from user_system_data --
    }

    protected AttackResult injectableQuery(String accountName) {
        String query = "";
        try (Connection connection = dataSource.getConnection()) {
            boolean usedUnion = true;
            query = "SELECT * FROM user_data WHERE last_name = '" + accountName + "'";
            //Check if Union is used
            if (!accountName.matches("(?i)(^[^-/*;)]*)(\\s*)UNION(.*$)")) {
                usedUnion = false;
            }
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                ResultSet results = statement.executeQuery(query);

                if ((results != null) && (results.first())) {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    StringBuffer output = new StringBuffer();

                    output.append(SqlInjectionLesson5a.writeTable(results, resultsMetaData));

                    String appendingWhenSucceded;
                    if (usedUnion)
                        appendingWhenSucceded = "Well done! Can you also figure out a solution, by appending a new Sql Statement?";
                    else
                        appendingWhenSucceded = "Well done! Can you also figure out a solution, by using a UNION?";
                    results.last();

                    if (output.toString().contains("dave") && output.toString().contains("passW0rD")) {
                        output.append(appendingWhenSucceded);
                        return trackProgress(success().feedback("sql-injection.advanced.6a.success").feedbackArgs(output.toString()).output(" Your query was: " + query).build());
                    } else {
                        return trackProgress(failed().output(output.toString() + "<br> Your query was: " + query).build());
                    }
                } else {
                    return trackProgress(failed().feedback("sql-injection.advanced.6a.no.results").output(" Your query was: " + query).build());
                }
            } catch (SQLException sqle) {
                return trackProgress(failed().output(sqle.getMessage() + "<br> Your query was: " + query).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return trackProgress(failed().output(this.getClass().getName() + " : " + e.getMessage() + "<br> Your query was: " + query).build());
        }
    }
}