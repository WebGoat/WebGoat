
package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
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
public class UpdateProfile extends DefaultLessonAction
{

	private LessonAction chainedAction;

	public UpdateProfile(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName);
		this.chainedAction = chainedAction;
	}

	public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
			UnauthorizedException, ValidationException
	{
		if (isAuthenticated(s))
		{
			int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);

			int subjectId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID, 0);

			String firstName = s.getParser().getStringParameter(RoleBasedAccessControl.FIRST_NAME);
			String lastName = s.getParser().getStringParameter(RoleBasedAccessControl.LAST_NAME);
			String ssn = s.getParser().getStringParameter(RoleBasedAccessControl.SSN);
			String title = s.getParser().getStringParameter(RoleBasedAccessControl.TITLE);
			String phone = s.getParser().getStringParameter(RoleBasedAccessControl.PHONE_NUMBER);
			String address1 = s.getParser().getStringParameter(RoleBasedAccessControl.ADDRESS1);
			String address2 = s.getParser().getStringParameter(RoleBasedAccessControl.ADDRESS2);
			int manager = s.getParser().getIntParameter(RoleBasedAccessControl.MANAGER);
			String startDate = s.getParser().getStringParameter(RoleBasedAccessControl.START_DATE);
			int salary = s.getParser().getIntParameter(RoleBasedAccessControl.SALARY);
			String ccn = s.getParser().getStringParameter(RoleBasedAccessControl.CCN);
			int ccnLimit = s.getParser().getIntParameter(RoleBasedAccessControl.CCN_LIMIT);
			String disciplinaryActionDate = s.getParser().getStringParameter(RoleBasedAccessControl.DISCIPLINARY_DATE);
			String disciplinaryActionNotes = s.getParser()
					.getStringParameter(RoleBasedAccessControl.DISCIPLINARY_NOTES);
			String personalDescription = s.getParser().getStringParameter(RoleBasedAccessControl.DESCRIPTION);

			Employee employee = new Employee(subjectId, firstName, lastName, ssn, title, phone, address1, address2,
					manager, startDate, salary, ccn, ccnLimit, disciplinaryActionDate, disciplinaryActionNotes,
					personalDescription);

			if (subjectId > 0)
			{
				this.changeEmployeeProfile(s, userId, subjectId, employee);
				setRequestAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ID, Integer
						.toString(subjectId));
			}
			else
				this.createEmployeeProfile(s, userId, employee);

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
		return RoleBasedAccessControl.VIEWPROFILE_ACTION;
	}

	public void changeEmployeeProfile(WebSession s, int userId, int subjectId, Employee employee)
			throws UnauthorizedException
	{
		try
		{
			// Note: The password field is ONLY set by ChangePassword
			String query = "UPDATE employee SET first_name = ?, last_name = ?, ssn = ?, title = ?, phone = ?, address1 = ?, address2 = ?,"
					+ " manager = ?, start_date = ?, ccn = ?, ccn_limit = ?,"
					+ " personal_description = ? WHERE userid = ?;";
			try
			{
				PreparedStatement ps = WebSession.getConnection(s).prepareStatement(query,
																					ResultSet.TYPE_SCROLL_INSENSITIVE,
																					ResultSet.CONCUR_READ_ONLY);

				ps.setString(1, employee.getFirstName());
				ps.setString(2, employee.getLastName());
				ps.setString(3, employee.getSsn());
				ps.setString(4, employee.getTitle());
				ps.setString(5, employee.getPhoneNumber());
				ps.setString(6, employee.getAddress1());
				ps.setString(7, employee.getAddress2());
				ps.setInt(8, employee.getManager());
				ps.setString(9, employee.getStartDate());
				ps.setString(10, employee.getCcn());
				ps.setInt(11, employee.getCcnLimit());
				ps.setString(12, employee.getPersonalDescription());
				ps.setInt(13, subjectId);
				ps.execute();
			} catch (SQLException sqle)
			{
				s.setMessage("Error updating employee profile");
				sqle.printStackTrace();
			}

		} catch (Exception e)
		{
			s.setMessage("Error updating employee profile");
			e.printStackTrace();
		}
	}

	public void changeEmployeeProfile_BACKUP(WebSession s, int userId, int subjectId, Employee employee)
			throws UnauthorizedException
	{
		try
		{
			// Note: The password field is ONLY set by ChangePassword
			String query = "UPDATE employee SET first_name = ?, last_name = ?, ssn = ?, title = ?, phone = ?, address1 = ?, address2 = ?,"
					+ " manager = ?, start_date = ?, ccn = ?, ccn_limit = ?,"
					+ " personal_description = ? WHERE userid = ?;";
			try
			{
				PreparedStatement ps = WebSession.getConnection(s).prepareStatement(query,
																					ResultSet.TYPE_SCROLL_INSENSITIVE,
																					ResultSet.CONCUR_READ_ONLY);

				ps.setString(1, employee.getFirstName());
				ps.setString(2, employee.getLastName());
				ps.setString(3, employee.getSsn());
				ps.setString(4, employee.getTitle());
				ps.setString(5, employee.getPhoneNumber());
				ps.setString(6, employee.getAddress1());
				ps.setString(7, employee.getAddress2());
				ps.setInt(8, employee.getManager());
				ps.setString(9, employee.getStartDate());
				ps.setString(10, employee.getCcn());
				ps.setInt(11, employee.getCcnLimit());
				ps.setString(12, employee.getPersonalDescription());
				ps.setInt(13, subjectId);
				ps.executeUpdate(query);
			} catch (SQLException sqle)
			{
				s.setMessage("Error updating employee profile");
				sqle.printStackTrace();
			}

		} catch (Exception e)
		{
			s.setMessage("Error updating employee profile");
			e.printStackTrace();
		}
	}

	protected int getNextUID(WebSession s)
	{
		int uid = -1;
		try
		{
			Statement statement = WebSession.getConnection(s).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
																				ResultSet.CONCUR_READ_ONLY);
			ResultSet results = statement.executeQuery("select max(userid) as uid from employee");
			results.first();
			uid = results.getInt("uid");
		} catch (SQLException sqle)
		{
			sqle.printStackTrace();
			s.setMessage("Error updating employee profile");
		}
		return uid + 1;
	}

	public void createEmployeeProfile(WebSession s, int userId, Employee employee) throws UnauthorizedException
	{
		try
		{
			// FIXME: Cannot choose the id because we cannot guarantee uniqueness
			int nextId = getNextUID(s);
			String query = "INSERT INTO employee VALUES ( " + nextId + ", ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			// System.out.println("Query: " + query);

			try
			{
				PreparedStatement ps = WebSession.getConnection(s).prepareStatement(query);

				ps.setString(1, employee.getFirstName().toLowerCase());
				ps.setString(2, employee.getLastName());
				ps.setString(3, employee.getSsn());
				ps.setString(4, employee.getTitle());
				ps.setString(5, employee.getPhoneNumber());
				ps.setString(6, employee.getAddress1());
				ps.setString(7, employee.getAddress2());
				ps.setInt(8, employee.getManager());
				ps.setString(9, employee.getStartDate());
				ps.setString(10, employee.getCcn());
				ps.setInt(11, employee.getCcnLimit());
				ps.setString(12, employee.getDisciplinaryActionDate());
				ps.setString(13, employee.getDisciplinaryActionNotes());
				ps.setString(14, employee.getPersonalDescription());

				ps.execute();
			} catch (SQLException sqle)
			{
				s.setMessage("Error updating employee profile");
				sqle.printStackTrace();
			}
		} catch (Exception e)
		{
			s.setMessage("Error updating employee profile");
			e.printStackTrace();
		}
	}
}
