
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
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
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
 * Source for this application is maintained at code.google.com, a repository for free software
 * projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
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
		setSessionAttribute(s, getLessonName() + "." + GoatHillsFinancial.STAFF_ATTRIBUTE_KEY, employees);

		int employeeId = -1;
		try
		{
			employeeId = s.getParser().getIntParameter(GoatHillsFinancial.EMPLOYEE_ID);
			String password = s.getParser().getStringParameter(GoatHillsFinancial.PASSWORD);

			// Attempt authentication
			if (login(s, employeeId, password))
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

	/**
	 * After this.handleRequest() is called, when the View asks for the current JSP to load, it will
	 * get one initialized by this call.
	 */
	public String getNextPage(WebSession s)
	{
		String nextPage = GoatHillsFinancial.LOGIN_ACTION;

		if (isAuthenticated(s)) nextPage = chainedAction.getNextPage(s);

		return nextPage;

	}

	public boolean requiresAuthentication()
	{
		return false;
	}

	public boolean login(WebSession s, int userId, String password)
	{
		// System.out.println("Logging in to lesson");
		boolean authenticated = false;

		try
		{
			String query = "SELECT * FROM employee WHERE userid = " + userId + " and password = '" + password + "'";

			try
			{
				Statement answer_statement = WebSession.getConnection(s)
						.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet answer_results = answer_statement.executeQuery(query);
				if (answer_results.first())
				{
					setSessionAttribute(s, getLessonName() + ".isAuthenticated", Boolean.TRUE);
					setSessionAttribute(s, getLessonName() + "." + GoatHillsFinancial.USER_ID, Integer.toString(userId));
					authenticated = true;
				}

			} catch (SQLException sqle)
			{
				s.setMessage("Error logging in");
				sqle.printStackTrace();
			}
		} catch (Exception e)
		{
			s.setMessage("Error logging in");
			e.printStackTrace();
		}

		// System.out.println("Lesson login result: " + authenticated);
		return authenticated;
	}

	public List<EmployeeStub> getAllEmployees(WebSession s)
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
