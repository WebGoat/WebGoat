
package org.owasp.webgoat.lessons.GoatHillsFinancial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

		int userId = getIntSessionAttribute(s, getLessonName() + "." + GoatHillsFinancial.USER_ID);
		int employeeId = s.getParser().getIntParameter(GoatHillsFinancial.EMPLOYEE_ID);

		if (isAuthenticated(s))
		{
			deleteEmployeeProfile(s, userId, employeeId);

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

	}

	public String getNextPage(WebSession s)
	{
		return GoatHillsFinancial.LISTSTAFF_ACTION;
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

}
