
package org.owasp.webgoat.lessons.DBCrossSiteScripting;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl;
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

			HttpServletRequest request = s.getRequest();
			int subjectId = Integer.parseInt(request.getParameter(DBCrossSiteScripting.EMPLOYEE_ID));
			String firstName = request.getParameter(DBCrossSiteScripting.FIRST_NAME);
			String lastName = request.getParameter(DBCrossSiteScripting.LAST_NAME);
			String ssn = request.getParameter(DBCrossSiteScripting.SSN);
			String title = request.getParameter(DBCrossSiteScripting.TITLE);
			String phone = request.getParameter(DBCrossSiteScripting.PHONE_NUMBER);
			String address1 = request.getParameter(DBCrossSiteScripting.ADDRESS1);
			String address2 = request.getParameter(DBCrossSiteScripting.ADDRESS2);
			int manager = Integer.parseInt(request.getParameter(DBCrossSiteScripting.MANAGER));
			String startDate = request.getParameter(DBCrossSiteScripting.START_DATE);
			int salary = Integer.parseInt(request.getParameter(DBCrossSiteScripting.SALARY));
			String ccn = request.getParameter(DBCrossSiteScripting.CCN);
			int ccnLimit = Integer.parseInt(request.getParameter(DBCrossSiteScripting.CCN_LIMIT));
			String disciplinaryActionDate = request.getParameter(DBCrossSiteScripting.DISCIPLINARY_DATE);
			String disciplinaryActionNotes = request.getParameter(DBCrossSiteScripting.DISCIPLINARY_NOTES);
			String personalDescription = request.getParameter(DBCrossSiteScripting.DESCRIPTION);

			Employee employee = new Employee(subjectId, firstName, lastName, ssn, title, phone, address1, address2,
					manager, startDate, salary, ccn, ccnLimit, disciplinaryActionDate, disciplinaryActionNotes,
					personalDescription);

			try
			{
				if (subjectId > 0)
				{
					this.changeEmployeeProfile(s, userId, subjectId, employee);
					setRequestAttribute(s, getLessonName() + "." + DBCrossSiteScripting.EMPLOYEE_ID, Integer
							.toString(subjectId));
					if (DBCrossSiteScripting.STAGE1.equals(getStage(s)))
					{
						address1 = address1.toLowerCase();
						boolean pass = address1.contains("<script>");
						pass &= address1.contains("alert");
						pass &= address1.contains("</script>");
						if (pass)
						{
							setStageComplete(s, DBCrossSiteScripting.STAGE1);
						}
					}
				}
				else
					this.createEmployeeProfile(s, userId, employee);
			} catch (SQLException e)
			{
				s.setMessage("Error updating employee profile");
				e.printStackTrace();
				if (DBCrossSiteScripting.STAGE2.equals(getStage(s))
						&& (e.getMessage().contains("ORA-06512") || e.getMessage().contains("Illegal characters"))
						&& !employee.getAddress1().matches("^[a-zA-Z0-9,\\. ]{0,80}$"))
				{
					setStageComplete(s, DBCrossSiteScripting.STAGE2);
				}

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
	}

	public String getNextPage(WebSession s)
	{
		return DBCrossSiteScripting.VIEWPROFILE_ACTION;
	}

	public void changeEmployeeProfile(WebSession s, int userId, int subjectId, Employee employee) throws SQLException
		{
			String update = " { CALL UPDATE_EMPLOYEE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
			CallableStatement call = WebSession.getConnection(s).prepareCall(update);
			// Note: The password field is ONLY set by ChangePassword
			call.setInt(1, userId);
			call.setString(2, employee.getFirstName());
			call.setString(3, employee.getLastName());
			call.setString(4, employee.getSsn());
			call.setString(5, employee.getTitle());
			call.setString(6, employee.getPhoneNumber());
			call.setString(7, employee.getAddress1());
			call.setString(8, employee.getAddress2());
			call.setInt(9, employee.getManager());
			call.setString(10, employee.getStartDate());
			call.setInt(11, employee.getSalary());
			call.setString(12, employee.getCcn());
			call.setInt(13, employee.getCcnLimit());
			call.setString(14, employee.getDisciplinaryActionDate());
			call.setString(15, employee.getDisciplinaryActionNotes());
			call.setString(16, employee.getPersonalDescription());
			call.executeUpdate();
	}

	public void createEmployeeProfile(WebSession s, int userId, Employee employee) throws UnauthorizedException
	{
		try
		{
			int nextId = getNextUID(s);
			String query = "INSERT INTO employee VALUES ( " + nextId + ", ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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

	private int getNextUID(WebSession s)
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
}
