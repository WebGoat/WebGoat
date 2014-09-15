
package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
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
public class EditProfile extends DefaultLessonAction
{

    public EditProfile(GoatHillsFinancial lesson, String lessonName, String actionName)
    {
        super(lesson, lessonName, actionName);
    }

    public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
            UnauthorizedException
    {
        getLesson().setCurrentAction(s, getActionName());

        if (isAuthenticated(s))
        {
            int userId = getUserId(s);
            int employeeId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID);

            Employee employee = getEmployeeProfile(s, userId, employeeId);
            setSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ATTRIBUTE_KEY, employee);
        }
        else
            throw new UnauthenticatedException();
    }

    public String getNextPage(WebSession s)
    {
        return RoleBasedAccessControl.EDITPROFILE_ACTION;
    }

    public Employee getEmployeeProfile(WebSession s, int userId, int subjectUserId) throws UnauthorizedException
    {
        Employee profile = null;

        // Query the database for the profile data of the given employee
        try
        {
            String query = "SELECT * FROM employee WHERE userid = ?";

            try
            {
                PreparedStatement answer_statement = WebSession.getConnection(s)
                        .prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                answer_statement.setInt(1, subjectUserId);
                ResultSet answer_results = answer_statement.executeQuery();
                if (answer_results.next())
                {
                    // Note: Do NOT get the password field.
                    profile = new Employee(answer_results.getInt("userid"), answer_results.getString("first_name"),
                            answer_results.getString("last_name"), answer_results.getString("ssn"), answer_results
                                    .getString("title"), answer_results.getString("phone"), answer_results
                                    .getString("address1"), answer_results.getString("address2"), answer_results
                                    .getInt("manager"), answer_results.getString("start_date"), answer_results
                                    .getInt("salary"), answer_results.getString("ccn"), answer_results
                                    .getInt("ccn_limit"), answer_results.getString("disciplined_date"), answer_results
                                    .getString("disciplined_notes"), answer_results.getString("personal_description"));
                    /*
                     * System.out.println("Retrieved employee from db: " + profile.getFirstName() +
                     * " " + profile.getLastName() + " (" + profile.getId() + ")");
                     */}
            } catch (SQLException sqle)
            {
                s.setMessage("Error getting employee profile");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error getting employee profile");
            e.printStackTrace();
        }

        return profile;
    }

    public Employee getEmployeeProfile_BACKUP(WebSession s, int userId, int subjectUserId) throws UnauthorizedException
    {
        // Query the database to determine if this employee has access to this function
        // Query the database for the profile data of the given employee if "owned" by the given
        // user

        Employee profile = null;

        // Query the database for the profile data of the given employee
        try
        {
            String query = "SELECT * FROM employee WHERE userid = ?";

            try
            {
                PreparedStatement answer_statement = WebSession.getConnection(s)
                        .prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                answer_statement.setInt(1, subjectUserId);
                ResultSet answer_results = answer_statement.executeQuery();
                if (answer_results.next())
                {
                    // Note: Do NOT get the password field.
                    profile = new Employee(answer_results.getInt("userid"), answer_results.getString("first_name"),
                            answer_results.getString("last_name"), answer_results.getString("ssn"), answer_results
                                    .getString("title"), answer_results.getString("phone"), answer_results
                                    .getString("address1"), answer_results.getString("address2"), answer_results
                                    .getInt("manager"), answer_results.getString("start_date"), answer_results
                                    .getInt("salary"), answer_results.getString("ccn"), answer_results
                                    .getInt("ccn_limit"), answer_results.getString("disciplined_date"), answer_results
                                    .getString("disciplined_notes"), answer_results.getString("personal_description"));
                    /*
                     * System.out.println("Retrieved employee from db: " + profile.getFirstName() +
                     * " " + profile.getLastName() + " (" + profile.getId() + ")");
                     */}
            } catch (SQLException sqle)
            {
                s.setMessage("Error getting employee profile");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error getting employee profile");
            e.printStackTrace();
        }

        return profile;
    }

}
