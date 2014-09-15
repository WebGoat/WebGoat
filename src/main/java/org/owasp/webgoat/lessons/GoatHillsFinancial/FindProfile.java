
package org.owasp.webgoat.lessons.GoatHillsFinancial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


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
 */
public class FindProfile extends DefaultLessonAction
{

    private LessonAction chainedAction;

    public FindProfile(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
    {
        super(lesson, lessonName, actionName);
        this.chainedAction = chainedAction;
    }

    public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
            UnauthorizedException, ValidationException
    {
        if (isAuthenticated(s))
        {
            int userId = getIntSessionAttribute(s, getLessonName() + "." + GoatHillsFinancial.USER_ID);

            String pattern = s.getParser().getRawParameter(GoatHillsFinancial.SEARCHNAME);

            findEmployeeProfile(s, userId, pattern);

            // Execute the chained Action if the employee was found.
            if (foundEmployee(s))
            {
                try
                {
                    chainedAction.handleRequest(s);
                } catch (UnauthenticatedException ue1)
                {
                    // System.out.println("Internal server error");
                    ue1.printStackTrace();
                } catch (UnauthorizedException ue2)
                {
                    // System.out.println("Internal server error");
                    ue2.printStackTrace();
                }
            }
        }
        else
            throw new UnauthenticatedException();
    }

    public String getNextPage(WebSession s)
    {
        String page = GoatHillsFinancial.SEARCHSTAFF_ACTION;

        if (foundEmployee(s)) page = GoatHillsFinancial.VIEWPROFILE_ACTION;

        return page;
    }

    private boolean foundEmployee(WebSession s)
    {
        boolean found = false;
        try
        {
            getIntRequestAttribute(s, getLessonName() + "." + GoatHillsFinancial.EMPLOYEE_ID);
            found = true;
        } catch (ParameterNotFoundException e)
        {
        }

        return found;
    }

    public Employee findEmployeeProfile(WebSession s, int userId, String pattern) throws UnauthorizedException
    {
        Employee profile = null;
        // Clear any residual employee id's in the session now.
        removeSessionAttribute(s, getLessonName() + "." + GoatHillsFinancial.EMPLOYEE_ID);

        // Query the database for the profile data of the given employee
        try
        {
            String query = "SELECT * FROM employee WHERE first_name LIKE ? OR last_name LIKE ?";

            try
            {
                PreparedStatement answer_statement = WebSession.getConnection(s)
                        .prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                answer_statement.setString(1, "%" + pattern + "%");
                answer_statement.setString(2, "%" + pattern + "%");
                ResultSet answer_results = answer_statement.executeQuery();

                // Just use the first hit.
                if (answer_results.next())
                {
                    int id = answer_results.getInt("userid");
                    // Note: Do NOT get the password field.
                    profile = new Employee(id, answer_results.getString("first_name"), answer_results
                            .getString("last_name"), answer_results.getString("ssn"),
                            answer_results.getString("title"), answer_results.getString("phone"), answer_results
                                    .getString("address1"), answer_results.getString("address2"), answer_results
                                    .getInt("manager"), answer_results.getString("start_date"), answer_results
                                    .getInt("salary"), answer_results.getString("ccn"), answer_results
                                    .getInt("ccn_limit"), answer_results.getString("disciplined_date"), answer_results
                                    .getString("disciplined_notes"), answer_results.getString("personal_description"));

                    /*
                     * System.out.println("Retrieved employee from db: " + profile.getFirstName() +
                     * " " + profile.getLastName() + " (" + profile.getId() + ")");
                     */
                    setRequestAttribute(s, getLessonName() + "." + GoatHillsFinancial.EMPLOYEE_ID, Integer.toString(id));
                }
            } catch (SQLException sqle)
            {
                s.setMessage("Error finding employee profile");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error finding employee profile");
            e.printStackTrace();
        }

        return profile;
    }

}
