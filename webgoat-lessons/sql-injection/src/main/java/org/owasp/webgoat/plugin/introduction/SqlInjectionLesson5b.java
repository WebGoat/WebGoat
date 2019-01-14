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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.*;


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
@AssignmentPath("/SqlInjection/assignment5b")
@AssignmentHints(value = {"SqlStringInjectionHint5b1", "SqlStringInjectionHint5b2", "SqlStringInjectionHint5b3", "SqlStringInjectionHint5b4"})
public class SqlInjectionLesson5b extends AssignmentEndpoint {

  @RequestMapping(method = RequestMethod.POST)
  public
  @ResponseBody
  AttackResult completed(@RequestParam String userid, @RequestParam String login_count, HttpServletRequest request) throws IOException {
    return injectableQuery(login_count, userid);
  }


  protected AttackResult injectableQuery(String login_count, String accountName) {
    String queryString = "SELECT * From user_data WHERE Login_Count = ? and userid= " + accountName;
    try {
      Connection connection = DatabaseUtilities.getConnection(getWebSession());
      PreparedStatement query = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);

      int count = 0;
      try {
        count = Integer.parseInt(login_count);
      } catch(Exception e) {
        return trackProgress(failed().output("Could not parse: " + login_count + " to a number" +
                "<br> Your query was: " + queryString.replace("?", login_count)).build());
      }

      query.setInt(1, count);
      //String query = "SELECT * FROM user_data WHERE Login_Count = " + login_count + " and userid = " + accountName, ;
      try {
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet results = query.executeQuery();

        if ((results != null) && (results.first() == true)) {
          ResultSetMetaData resultsMetaData = results.getMetaData();
          StringBuffer output = new StringBuffer();

          output.append(SqlInjectionLesson5a.writeTable(results, resultsMetaData));
          results.last();

          // If they get back more than one user they succeeded
          if (results.getRow() >= 6) {
            return trackProgress(success().feedback("sql-injection.5b.success").output("Your query was: " + queryString.replace("?", login_count)).feedbackArgs(output.toString()).build());
          } else {
            return trackProgress(failed().output(output.toString() + "<br> Your query was: " + queryString.replace("?", login_count)).build());
          }

        } else {
          return trackProgress(failed().feedback("sql-injection.5b.no.results").output("Your query was: " + queryString.replace("?", login_count)).build());

//                    output.append(getLabelManager().get("NoResultsMatched"));
        }
      } catch (SQLException sqle) {

        return trackProgress(failed().output(sqle.getMessage() + "<br> Your query was: " + queryString.replace("?", login_count)).build());
      }
    } catch (Exception e) {
      return trackProgress(failed().output(this.getClass().getName() + " : " + e.getMessage() + "<br> Your query was: " + queryString.replace("?", login_count)).build());
    }
  }
}