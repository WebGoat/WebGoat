
package org.owasp.webgoat.lessons.instructor.RoleBasedAccessControl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.UpdateProfile;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;


/**
 * Copyright (c) 2006 Free Software Foundation developed under the custody of the Open Web
 * Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is
 * published by OWASP under the GPL. You should read and accept the LICENSE before you use, modify
 * and/or redistribute this software.
 * 
 */

/*************************************************/
/*												 */
/* This file is not currently used in the course */
/*												 */
/*************************************************/

public class UpdateProfile_i extends UpdateProfile
{
	public UpdateProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName, chainedAction);
	}

	public void changeEmployeeProfile(WebSession s, int userId, int subjectId, Employee employee)
			throws UnauthorizedException
	{
		if (s.isAuthorizedInLesson(userId, RoleBasedAccessControl.UPDATEPROFILE_ACTION)) // FIX
		{
			try
			{
				// Note: The password field is ONLY set by ChangePassword
				String query = "UPDATE employee SET first_name = ?, last_name = ?, ssn = ?, title = ?, phone = ?, address1 = ?, address2 = ?,"
						+ " manager = ?, start_date = ?, ccn = ?, ccn_limit = ?,"
						+ " personal_description = ? WHERE userid = ?;";
				try
				{
					PreparedStatement ps = WebSession.getConnection(s)
							.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

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
		else
		{
			throw new UnauthorizedException(); // FIX
		}
	}

	public void createEmployeeProfile(WebSession s, int userId, Employee employee) throws UnauthorizedException
	{
		if (s.isAuthorizedInLesson(userId, RoleBasedAccessControl.UPDATEPROFILE_ACTION)) // FIX
		{
			try
			{
				// FIXME: Cannot choose the id because we cannot guarantee uniqueness
				int nextId = getNextUID(s);
				String query = "INSERT INTO employee VALUES ( " + nextId + ", ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				// System.out.println("Query:  " + query);

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
		else
		{
			throw new UnauthorizedException(); // FIX
		}
	}

}
