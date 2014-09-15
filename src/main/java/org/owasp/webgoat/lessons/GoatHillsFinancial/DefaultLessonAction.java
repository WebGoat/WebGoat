
package org.owasp.webgoat.lessons.GoatHillsFinancial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl;
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
public abstract class DefaultLessonAction implements LessonAction
{
    // FIXME: We could parse this class name to get defaults for these fields.
    private String lessonName;
    private String actionName;

    private GoatHillsFinancial lesson;

    public DefaultLessonAction(GoatHillsFinancial lesson, String lessonName, String actionName)
    {
        this.lesson = lesson;
        this.lessonName = lessonName;
        this.actionName = actionName;
    }

    public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
            UnauthorizedException, ValidationException
    {
        getLesson().setCurrentAction(s, getActionName());

        if (isAuthenticated(s))
        {
        }
        else
            throw new UnauthenticatedException();
    }

    public abstract String getNextPage(WebSession s);

    public GoatHillsFinancial getLesson()
    {
        return lesson;
    }

    public String getLessonName()
    {
        return lessonName;
    }

    public String getActionName()
    {
        return actionName;
    }

    public void setSessionAttribute(WebSession s, String name, Object value)
    {
        s.getRequest().getSession().setAttribute(name, value);
    }

    public void setRequestAttribute(WebSession s, String name, Object value)
    {
        s.getRequest().setAttribute(name, value);
    }

    public void removeSessionAttribute(WebSession s, String name)
    {
        s.getRequest().getSession().removeAttribute(name);
    }

    protected String getSessionAttribute(WebSession s, String name) throws ParameterNotFoundException
    {
        String value = (String) s.getRequest().getSession().getAttribute(name);
        if (value == null) { throw new ParameterNotFoundException(); }

        return value;
    }

    protected boolean getBooleanSessionAttribute(WebSession s, String name) throws ParameterNotFoundException
    {
        boolean value = false;

        Object attribute = s.getRequest().getSession().getAttribute(name);
        if (attribute == null)
        {
            throw new ParameterNotFoundException();
        }
        else
        {
            // System.out.println("Attribute " + name + " is of type " +
            // s.getRequest().getSession().getAttribute(name).getClass().getName());
            // System.out.println("Attribute value: " +
            // s.getRequest().getSession().getAttribute(name));
            value = ((Boolean) attribute).booleanValue();
        }
        return value;
    }

    protected int getIntSessionAttribute(WebSession s, String name) throws ParameterNotFoundException
    {
        int value = -1;
        String ss = (String) s.getRequest().getSession().getAttribute(name);
        if (ss == null)
        {
            throw new ParameterNotFoundException();
        }
        else
        {
            try
            {
                value = Integer.parseInt(ss);
            } catch (NumberFormatException nfe)
            {
            }
        }

        return value;
    }

    protected String getRequestAttribute(WebSession s, String name) throws ParameterNotFoundException
    {
        String value = (String) s.getRequest().getAttribute(name);
        if (value == null) { throw new ParameterNotFoundException(); }

        return value;
    }

    protected int getIntRequestAttribute(WebSession s, String name) throws ParameterNotFoundException
    {
        int value = -1;
        String ss = (String) s.getRequest().getAttribute(name);
        if (ss == null)
        {
            throw new ParameterNotFoundException();
        }
        else
        {
            try
            {
                value = Integer.parseInt(ss);
            } catch (NumberFormatException nfe)
            {
            }
        }

        return value;
    }

    public int getUserId(WebSession s) throws ParameterNotFoundException
    {
        return getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);
    }

    public String getUserName(WebSession s) throws ParameterNotFoundException
    {
        String name = null;

        int employeeId = getUserId(s);
        try
        {
            String query = "SELECT first_name FROM employee WHERE userid = " + employeeId;

            try
            {
                Statement answer_statement = WebSession.getConnection(s)
                        .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet answer_results = answer_statement.executeQuery(query);
                if (answer_results.next()) name = answer_results.getString("first_name");
            } catch (SQLException sqle)
            {
                s.setMessage("Error getting user name");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error getting user name");
            e.printStackTrace();
        }

        return name;
    }

    public boolean requiresAuthentication()
    {
        // Default to true
        return true;
    }

    public boolean isAuthenticated(WebSession s)
    {
        boolean authenticated = false;

        try
        {
            authenticated = getBooleanSessionAttribute(s, getLessonName() + ".isAuthenticated");
        } catch (ParameterNotFoundException e)
        {
        }

        return authenticated;
    }

    public boolean isAuthorized(WebSession s, int employeeId, String functionId)
    {
        String employer_id = (String) s.getRequest().getSession()
                .getAttribute(getLessonName() + "." + RoleBasedAccessControl.USER_ID);
        // System.out.println("Authorizing " + employeeId + " for use of function: " + functionId +
        // " having USER_ID = "
        // + employer_id );
        boolean authorized = false;

        try
        {
            String query = "SELECT * FROM auth WHERE auth.role in (SELECT roles.role FROM roles WHERE userid = "
                    + employeeId + ") and functionid = '" + functionId + "'";

            try
            {
                Statement answer_statement = WebSession.getConnection(s)
                        .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet answer_results = answer_statement.executeQuery(query);
                authorized = answer_results.first();

                /*
                 * User is validated for function, but can the user perform that function on the
                 * specified user?
                 */
                if (authorized)
                {
                    authorized = isAuthorizedForEmployee(s, Integer.parseInt(employer_id), employeeId);
                }
            } catch (SQLException sqle)
            {
                s.setMessage("Error authorizing");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error authorizing");
            e.printStackTrace();
        }

        // System.out.println("Authorized? " + authorized);
        return authorized;
    }

    public boolean isAuthorizedForEmployee(WebSession s, int userId, int employeeId)
    {
        // System.out.println("Authorizing " + userId + " for access to employee: " + employeeId);
        boolean authorized = false;

        try
        {
            String query = "SELECT * FROM ownership WHERE employer_id = ? AND employee_id = ?";

            try
            {

                PreparedStatement answer_statement = WebSession.getConnection(s)
                        .prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                answer_statement.setInt(1, userId);
                answer_statement.setInt(2, employeeId);
                ResultSet answer_results = answer_statement.executeQuery();
                authorized = answer_results.first();
            } catch (SQLException sqle)
            {
                s.setMessage("Error authorizing");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error authorizing");
            e.printStackTrace();
        }

        return authorized;
    }

    protected void setStage(WebSession s, String stage)
    {
        getLesson().setStage(s, stage);
    }

    protected void setStageComplete(WebSession s, String stage)
    {
        getLesson().setStageComplete(s, stage);
    }

    protected String getStage(WebSession s)
    {
        return getLesson().getStage(s);
    }

    public String toString()
    {
        return getActionName();
    }

}
