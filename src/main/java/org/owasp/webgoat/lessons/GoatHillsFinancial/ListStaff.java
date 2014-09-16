
package org.owasp.webgoat.lessons.GoatHillsFinancial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import org.owasp.webgoat.session.EmployeeStub;
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
public class ListStaff extends DefaultLessonAction
{

    public ListStaff(GoatHillsFinancial lesson, String lessonName, String actionName)
    {
        super(lesson, lessonName, actionName);
    }

    public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
            UnauthorizedException
    {
        getLesson().setCurrentAction(s, getActionName());

        if (isAuthenticated(s))
        {
            int userId = getIntSessionAttribute(s, getLessonName() + "." + GoatHillsFinancial.USER_ID);

            List<EmployeeStub> employees = getAllEmployees(s, userId);
            setSessionAttribute(s, getLessonName() + "." + GoatHillsFinancial.STAFF_ATTRIBUTE_KEY, employees);
        }
        else
            throw new UnauthenticatedException();
    }

    public String getNextPage(WebSession s)
    {
        return GoatHillsFinancial.LISTSTAFF_ACTION;
    }

    public List<EmployeeStub> getAllEmployees(WebSession s, int userId) throws UnauthorizedException
    {
        // Query the database for all employees "owned" by the given employee

        List<EmployeeStub> employees = new Vector<EmployeeStub>();

        try
        {
            String query = "SELECT employee.userid,first_name,last_name,role FROM employee,roles WHERE employee.userid=roles.userid and employee.userid in "
                    + "(SELECT employee_id FROM ownership WHERE employer_id = " + userId + ")";

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
                    // System.out.println("Retrieving employee stub for role " + role);
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
