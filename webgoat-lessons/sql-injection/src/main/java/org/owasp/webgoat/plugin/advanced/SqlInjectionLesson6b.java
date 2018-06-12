
package org.owasp.webgoat.plugin.advanced;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/***************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * For details, please see http://webgoat.github.io
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
@AssignmentPath("/SqlInjection/attack6b")
public class SqlInjectionLesson6b extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String userid_6b) throws IOException {
        if (userid_6b.toString().equals(getPassword())) {
            return trackProgress(success().build());
        } else {
            return trackProgress(failed().build());
        }
    }

    protected String getPassword() {

        String password = "dave";
        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());
            String query = "SELECT password FROM user_system_data WHERE user_name = 'dave'";

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);

                if ((results != null) && (results.first() == true)) {
                    password = results.getString("password");
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                // do nothing
            }
        } catch (Exception e) {
            e.printStackTrace();
            // do nothing
        }
        return (password);
    }
}
