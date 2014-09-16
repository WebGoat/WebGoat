
package org.owasp.webgoat.lessons.DBSQLInjection;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Vector;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.session.EmployeeStub;
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
public class Login extends DefaultLessonAction
{

    private LessonAction chainedAction;

    public Login(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
    {
        super(lesson, lessonName, actionName);
        this.chainedAction = chainedAction;
    }

    public void handleRequest(WebSession s) throws ParameterNotFoundException, ValidationException
    {
        // System.out.println("Login.handleRequest()");
        getLesson().setCurrentAction(s, getActionName());

        List employees = getAllEmployees(s);
        setSessionAttribute(s, getLessonName() + "." + DBSQLInjection.STAFF_ATTRIBUTE_KEY, employees);

        String employeeId = null;
        try
        {
            employeeId = s.getParser().getStringParameter(DBSQLInjection.EMPLOYEE_ID);
            String password = s.getParser().getRawParameter(DBSQLInjection.PASSWORD);

            // Attempt authentication
            boolean authenticated = login(s, employeeId, password);

            if (authenticated)
            {
                // Execute the chained Action if authentication succeeded.
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
                s.setMessage("Login failed");

        } catch (ParameterNotFoundException pnfe)
        {
            // No credentials offered, so we log them out
            setSessionAttribute(s, getLessonName() + ".isAuthenticated", Boolean.FALSE);
        }
    }

    public String getNextPage(WebSession s)
    {
        String nextPage = DBSQLInjection.LOGIN_ACTION;

        if (isAuthenticated(s)) nextPage = chainedAction.getNextPage(s);

        return nextPage;

    }

    public boolean requiresAuthentication()
    {
        return false;
    }

    public boolean login(WebSession s, String userId, String password)
    {
        boolean authenticated = false;

        try
        {
            String call = "{ ? = call EMPLOYEE_LOGIN(?,?) }"; // NB: "call", not "CALL"! Doh!

            try
            {
                CallableStatement statement = WebSession.getConnection(s)
                        .prepareCall(call, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                statement.registerOutParameter(1, Types.INTEGER);
                statement.setInt(2, Integer.parseInt(userId));
                statement.setString(3, password);
                statement.execute();

                int rows = statement.getInt(1);
                if (rows > 0)
                {
                    setSessionAttribute(s, getLessonName() + ".isAuthenticated", Boolean.TRUE);
                    setSessionAttribute(s, getLessonName() + "." + DBSQLInjection.USER_ID, userId);
                    authenticated = true;
                    if (DBSQLInjection.STAGE1.equals(getStage(s))
                            && DBSQLInjection.PRIZE_EMPLOYEE_ID == Integer.parseInt(userId))
                    {
                        setStageComplete(s, DBSQLInjection.STAGE1);
                    }
                }
                else
                {

                    if (DBSQLInjection.STAGE2.equals(getStage(s)))
                    {
                        try
                        {
                            String call2 = "{ ? = call EMPLOYEE_LOGIN_BACKUP(?,?) }";
                            statement = WebSession.getConnection(s).prepareCall(call2,
                                                                                ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                                ResultSet.CONCUR_READ_ONLY);
                            statement.registerOutParameter(1, Types.INTEGER);
                            statement.setInt(2, Integer.parseInt(userId));
                            statement.setString(3, password);
                            statement.execute();

                            rows = statement.getInt(1);
                            if (rows > 0) setStageComplete(s, DBSQLInjection.STAGE2);
                        } catch (SQLException sqle2)
                        {
                        }
                    }
                }
            } catch (SQLException sqle)
            {
                s.setMessage("Error logging in: " + sqle.getLocalizedMessage());
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error logging in: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        // System.out.println("Lesson login result: " + authenticated);
        return authenticated;
    }

    public List getAllEmployees(WebSession s)
    {
        List<EmployeeStub> employees = new Vector<EmployeeStub>();

        // Query the database for all roles the given employee belongs to
        // Query the database for all employees "owned" by these roles

        try
        {
            String query = "SELECT employee.userid,first_name,last_name,role FROM employee,roles "
                    + "where employee.userid=roles.userid";

            try
            {
                Statement answer_statement = WebSession.getConnection(s)
                        .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet answer_results = answer_statement.executeQuery(query);
                answer_results.beforeFirst();
                while (answer_results.next())
                {
                    int employeeId = answer_results.getInt("userid");
                    String firstName = answer_results.getString("first_name");
                    String lastName = answer_results.getString("last_name");
                    String role = answer_results.getString("role");
                    EmployeeStub stub = new EmployeeStub(employeeId, firstName, lastName, role);
                    employees.add(stub);
                }
            } catch (SQLException sqle)
            {
                s.setMessage("Error getting employees");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error getting employees");
            e.printStackTrace();
        }

        return employees;
    }

}
