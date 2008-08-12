
package org.owasp.webgoat.lessons.CrossSiteScripting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
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
			UnauthorizedException, ValidationException
	{
		getLesson().setCurrentAction(s, getActionName());

		if (isAuthenticated(s))
		{
			int userId = getIntSessionAttribute(s, getLessonName() + "." + CrossSiteScripting.USER_ID);
			int employeeId = -1;
			try
			{
				// User selected employee
				employeeId = s.getParser().getIntParameter(CrossSiteScripting.EMPLOYEE_ID);
			} catch (ParameterNotFoundException e)
			{
				// May be an internally selected employee
				employeeId = getIntRequestAttribute(s, getLessonName() + "." + CrossSiteScripting.EMPLOYEE_ID);
			}

			Employee employee = getEmployeeProfile(s, userId, employeeId);
			setSessionAttribute(s, getLessonName() + "." + CrossSiteScripting.EMPLOYEE_ATTRIBUTE_KEY, employee);

			updateLessonStatus(s, employee);
		}
		else
			throw new UnauthenticatedException();
	}

	public String getNextPage(WebSession s)
	{
		return CrossSiteScripting.VIEWPROFILE_ACTION;
	}

	public Employee getEmployeeProfile(WebSession s, int userId, int subjectUserId) throws UnauthorizedException
	{
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

	public Employee getEmployeeProfile_BACKUP(WebSession s, int userId, int subjectUserId) throws UnauthorizedException
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
		String stage = getStage(s);
		int userId = -1;
		try
		{
			userId = getIntSessionAttribute(s, getLessonName() + "." + CrossSiteScripting.USER_ID);
		} catch (ParameterNotFoundException pnfe)
		{
		}
		if (CrossSiteScripting.STAGE1.equals(stage))
		{
			String address1 = employee.getAddress1().toLowerCase();
			if (userId != employee.getId() && address1.indexOf("<script>") > -1 && address1.indexOf("alert") > -1
					&& address1.indexOf("</script>") > -1)
			{
				setStageComplete(s, CrossSiteScripting.STAGE1);
			}
		}
		else if (CrossSiteScripting.STAGE3.equals(stage))
		{
			String address2 = employee.getAddress1().toLowerCase();
			if (address2.indexOf("<script>") > -1 && address2.indexOf("alert") > -1
					&& address2.indexOf("</script>") > -1)
			{
				setStageComplete(s, CrossSiteScripting.STAGE3);
			}
		}
		else if (CrossSiteScripting.STAGE4.equals(stage))
		{
			if (employee.getAddress1().toLowerCase().indexOf("&lt;") > -1)
			{
				setStageComplete(s, CrossSiteScripting.STAGE4);
			}
		}
	}

}
