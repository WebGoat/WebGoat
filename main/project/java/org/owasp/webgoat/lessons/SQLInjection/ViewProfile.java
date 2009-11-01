
package org.owasp.webgoat.lessons.SQLInjection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class ViewProfile extends DefaultLessonAction
{

	public ViewProfile(GoatHillsFinancial lesson, String lessonName, String actionName)
	{
		super(lesson, lessonName, actionName);
	}

	public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
			UnauthorizedException
	{
		getLesson().setCurrentAction(s, getActionName());

		Employee employee = null;

		if (isAuthenticated(s))
		{
			String userId = getSessionAttribute(s, getLessonName() + "." + SQLInjection.USER_ID);
			String employeeId = null;
			try
			{
				// User selected employee
				employeeId = s.getParser().getRawParameter(SQLInjection.EMPLOYEE_ID);
			} catch (ParameterNotFoundException e)
			{
				// May be an internally selected employee
				employeeId = getRequestAttribute(s, getLessonName() + "." + SQLInjection.EMPLOYEE_ID);
			}

			// FIXME: If this fails and returns null, ViewProfile.jsp will blow up as it expects an
			// Employee.
			// Most other JSP's can handle null session attributes.
			employee = getEmployeeProfile(s, userId, employeeId);
			// If employee==null redirect to the error page.
			if (employee == null)
				getLesson().setCurrentAction(s, SQLInjection.ERROR_ACTION);
			else
				setSessionAttribute(s, getLessonName() + "." + SQLInjection.EMPLOYEE_ATTRIBUTE_KEY, employee);
		}
		else
			throw new UnauthenticatedException();

		updateLessonStatus(s, employee);
	}

	public String getNextPage(WebSession s)
	{
		return SQLInjection.VIEWPROFILE_ACTION;
	}

	public Employee getEmployeeProfile(WebSession s, String userId, String subjectUserId) throws UnauthorizedException
	{
		Employee profile = null;

		// Query the database for the profile data of the given employee
		try
		{
			String query = "SELECT employee.* "
					+ "FROM employee,ownership WHERE employee.userid = ownership.employee_id and "
					+ "ownership.employer_id = " + userId + " and ownership.employee_id = " + subjectUserId;

			try
			{
				Statement answer_statement = WebSession.getConnection(s)
						.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet answer_results = answer_statement.executeQuery(query);
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
					// System.out.println("Profile: " + profile);
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

	public Employee getEmployeeProfile_BACKUP(WebSession s, String userId, String subjectUserId)
			throws UnauthorizedException
	{
		// Query the database to determine if this employee has access to this function
		// Query the database for the profile data of the given employee if "owned" by the given
		// user

		Employee profile = null;

		// Query the database for the profile data of the given employee
		try
		{
			String query = "SELECT * FROM employee WHERE userid = " + subjectUserId;

			try
			{
				Statement answer_statement = WebSession.getConnection(s)
						.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet answer_results = answer_statement.executeQuery(query);
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

	private void updateLessonStatus(WebSession s, Employee employee)
	{
		try
		{
			String userId = getSessionAttribute(s, getLessonName() + "." + SQLInjection.USER_ID);
			String employeeId = s.getParser().getRawParameter(SQLInjection.EMPLOYEE_ID);
			String stage = getStage(s);
			if (SQLInjection.STAGE3.equals(stage))
			{
				// If the employee we are viewing is the prize and we are not authorized to have it,
				// the stage is completed
				if (employee != null && employee.getId() == SQLInjection.PRIZE_EMPLOYEE_ID
						&& !isAuthorizedForEmployee(s, Integer.parseInt(userId), employee.getId()))
				{
					setStageComplete(s, SQLInjection.STAGE3);
				}
			}
			else if (SQLInjection.STAGE4.equals(stage))
			{
				// If we were denied the employee to view, and we would have been able to view it
				// in the broken state, the stage is completed.
				// This assumes the student hasn't modified getEmployeeProfile_BACKUP().
				if (employee == null)
				{
					Employee targetEmployee = null;
					try
					{
						targetEmployee = getEmployeeProfile_BACKUP(s, userId, employeeId);
					} catch (UnauthorizedException e)
					{
					}
					if (targetEmployee != null && targetEmployee.getId() == SQLInjection.PRIZE_EMPLOYEE_ID)
					{
						setStageComplete(s, SQLInjection.STAGE4);
					}
				}
			}
		} catch (ParameterNotFoundException pnfe)
		{
		}
	}

}
