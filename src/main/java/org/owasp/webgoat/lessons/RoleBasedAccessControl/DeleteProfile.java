
package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
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
public class DeleteProfile extends DefaultLessonAction
{

    private LessonAction chainedAction;

    public DeleteProfile(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
    {
        super(lesson, lessonName, actionName);
        this.chainedAction = chainedAction;
    }

    public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
            UnauthorizedException, ValidationException
    {
        getLesson().setCurrentAction(s, getActionName());

        int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);
        int employeeId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID);

        if (isAuthenticated(s))
        {
            if (userId != employeeId) {
            deleteEmployeeProfile(s, userId, employeeId);
            }
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
        else
            throw new UnauthenticatedException();

        updateLessonStatus(s);
    }

    public String getNextPage(WebSession s)
    {
        return RoleBasedAccessControl.LISTSTAFF_ACTION;
    }

    public void deleteEmployeeProfile(WebSession s, int userId, int employeeId) throws UnauthorizedException
    {
        try
        {
            // Note: The password field is ONLY set by ChangePassword
            String query = "DELETE FROM employee WHERE userid = " + employeeId;
            // System.out.println("Query: " + query);
            try
            {
                Statement statement = WebSession.getConnection(s).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                                    ResultSet.CONCUR_READ_ONLY);
                statement.executeUpdate(query);
            } catch (SQLException sqle)
            {
                s.setMessage("Error deleting employee profile");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error deleting employee profile");
            e.printStackTrace();
        }
    }

    public void deleteEmployeeProfile_BACKUP(WebSession s, int userId, int employeeId) throws UnauthorizedException
    {
        try
        {
            // Note: The password field is ONLY set by ChangePassword
            String query = "DELETE FROM employee WHERE userid = " + employeeId;
            // System.out.println("Query: " + query);
            try
            {
                Statement statement = WebSession.getConnection(s).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                                    ResultSet.CONCUR_READ_ONLY);
                statement.executeUpdate(query);
            } catch (SQLException sqle)
            {
                s.setMessage("Error deleting employee profile");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error deleting employee profile");
            e.printStackTrace();
        }
    }

    private void updateLessonStatus(WebSession s)
    {
        // If the logged in user is not authorized to be here, stage 1 is complete.
        if (RoleBasedAccessControl.STAGE1.equals(getStage(s))) try
        {
            int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);

            if (!isAuthorized(s, userId, RoleBasedAccessControl.DELETEPROFILE_ACTION))
            {
                setStageComplete(s, RoleBasedAccessControl.STAGE1);
            }
        } catch (ParameterNotFoundException e)
        {
        }
    }

}
