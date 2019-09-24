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

package org.owasp.webgoat.missing_ac;

import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.HashMap;

public class Users {

    @Autowired
    private WebSession webSession;

    @Autowired
    UserSessionData userSessionData;

    @GetMapping(produces = {"application/json"})
    @ResponseBody
    protected HashMap<Integer, HashMap> getUsers() {

        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());
            String query = "SELECT * FROM user_data";

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);
                HashMap<Integer,HashMap> allUsersMap = new HashMap();

                if ((results != null) && (results.first() == true)) {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    StringBuffer output = new StringBuffer();

                    while (results.next()) {
                        int id = results.getInt(0);
                        HashMap<String,String> userMap = new HashMap<>();
                        userMap.put("first", results.getString(1));
                        userMap.put("last", results.getString(2));
                        userMap.put("cc", results.getString(3));
                        userMap.put("ccType", results.getString(4));
                        userMap.put("cookie", results.getString(5));
                        userMap.put("loginCount",Integer.toString(results.getInt(6)));
                        allUsersMap.put(id,userMap);
                    }
                    userSessionData.setValue("allUsers",allUsersMap);
                    return allUsersMap;

                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                HashMap<String,String> errMap = new HashMap() {{
                    put("err",sqle.getErrorCode() + "::" + sqle.getMessage());
                }};

                return new HashMap<Integer,HashMap>() {{
                    put(0,errMap);
                }};
            } catch (Exception e) {
                e.printStackTrace();
                HashMap<String,String> errMap = new HashMap() {{
                    put("err",e.getMessage() + "::" + e.getCause());
                }};
                e.printStackTrace();
                return new HashMap<Integer,HashMap>() {{
                    put(0,errMap);
                }};


            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String,String> errMap = new HashMap() {{
                put("err",e.getMessage() + "::" + e.getCause());
            }};
            e.printStackTrace();
            return new HashMap<Integer,HashMap>() {{
                put(0,errMap);
            }};

        }
        return null;
    }

    protected  WebSession getWebSession() {
        return webSession;
    }

//    @Override
//    public String getPath() {
//        return  "/access-control/list-users";
//    }
}
