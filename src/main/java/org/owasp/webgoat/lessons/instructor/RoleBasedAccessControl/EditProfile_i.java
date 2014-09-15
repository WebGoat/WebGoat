
package org.owasp.webgoat.lessons.instructor.RoleBasedAccessControl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.EditProfile;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;


/**
 * Copyright (c) 2006 Free Software Foundation developed under the custody of the Open Web
 * Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is
 * published by OWASP under the GPL. You should read and accept the LICENSE before you use, modify
 * and/or redistribute this software.
 * 
 */

/*************************************************/
/*                                               */
/* This file is not currently used in the course */
/*                                               */
/*************************************************/

public class EditProfile_i extends EditProfile
{
    public EditProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName)
    {
        super(lesson, lessonName, actionName);
    }

    public Employee getEmployeeProfile(WebSession s, int userId, int subjectUserId) throws UnauthorizedException
    {
        // Query the database to determine if this employee has access to this function
        // Query the database for the profile data of the given employee if "owned" by the given
        // user

        Employee profile = null;

        if (s.isAuthorizedInLesson(userId, RoleBasedAccessControl.EDITPROFILE_ACTION)) // FIX
        {
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
                        profile = new Employee(
                            answer_results.getInt("userid"), 
                            answer_results.getString("first_name"),
                            answer_results.getString("last_name"), 
                            answer_results.getString("ssn"), 
                            answer_results.getString("title"), 
                            answer_results.getString("phone"), 
                            answer_results.getString("address1"), 
                            answer_results.getString("address2"), 
                            answer_results.getInt("manager"), 
                            answer_results.getString("start_date"), 
                            answer_results.getInt("salary"), 
                            answer_results.getString("ccn"), 
                            answer_results.getInt("ccn_limit"), 
                            answer_results.getString("disciplined_date"),
                            answer_results.getString("disciplined_notes"), 
                            answer_results.getString("personal_description"));
                        /*
                         * System.out.println("Retrieved employee from db: " +
                         * profile.getFirstName() + " " + profile.getLastName() + " (" +
                         * profile.getId() + ")");
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
        }
        else
        {
            throw new UnauthorizedException(); // FIX
        }

        return profile;
    }

}
