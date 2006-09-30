package org.owasp.webgoat.lessons.instructor.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.UpdateProfile;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;

/**
 *  Copyright (c) 2006 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 */

/*************************************************/
/*												 */
/* This file is not currently used in the course */
/*												 */
/*************************************************/


public class UpdateProfile_i extends UpdateProfile
{
	public UpdateProfile_i(AbstractLesson lesson, String lessonName, String actionName, LessonAction chainedAction)
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
				String query = "UPDATE employee SET first_name = '" + employee.getFirstName() + 
						"', last_name = '" + employee.getLastName() +
						"', ssn = '" + employee.getSsn() +
						"', title = '" + employee.getTitle() +
						"', phone = '" + employee.getPhoneNumber() +
						"', address1 = '" + employee.getAddress1() +
						"', address2 = '" + employee.getAddress2() +
						"', manager = " + employee.getManager() +		
						", start_date = '" + employee.getStartDate() +
						"', ccn = '" + employee.getCcn() +
						"', ccn_limit = " + employee.getCcnLimit() +
					//	"', disciplined_date = '" + employee.getDisciplinaryActionDate() +
					//	"', disciplined_notes = '" + employee.getDisciplinaryActionNotes() +
						", personal_description = '" + employee.getPersonalDescription() + 
						"' WHERE userid = " + subjectId;
				//System.out.println("Query:  " + query);
				try
				{
					Statement answer_statement = RoleBasedAccessControl.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
					ResultSet answer_results = answer_statement.executeQuery( query );
				}
				catch ( SQLException sqle )
				{
					s.setMessage( "Error updating employee profile" );
					sqle.printStackTrace();
				}
				
			}
			catch ( Exception e )
			{
				s.setMessage( "Error updating employee profile" );
				e.printStackTrace();
			}		
		}
		else
		{
			throw new UnauthorizedException(); // FIX
		}
	}


	public void createEmployeeProfile(WebSession s, int userId, Employee employee)
			throws UnauthorizedException
	{
		if (s.isAuthorizedInLesson(userId, RoleBasedAccessControl.UPDATEPROFILE_ACTION)) // FIX
		{
			try
			{
				// FIXME: Cannot choose the id because we cannot guarantee uniqueness
				String query = "INSERT INTO employee VALUES ( max(userid)+1, '"
				+ employee.getFirstName() + "','"
				+ employee.getLastName() + "','"
				+ employee.getSsn() + "','"
				+ employee.getFirstName().toLowerCase() + "','"
				+ employee.getTitle() + "','"
				+ employee.getPhoneNumber() + "','"
				+ employee.getAddress1() + "','"
				+ employee.getAddress2() + "',"
				+ employee.getManager() + ",'"
				+ employee.getStartDate() + "',"
				+ employee.getSalary() + ",'"
				+ employee.getCcn() + "',"
				+ employee.getCcnLimit() + ",'"
				+ employee.getDisciplinaryActionDate() + "','"
				+ employee.getDisciplinaryActionNotes() + "','"
				+ employee.getPersonalDescription()
				+ "')";
				
				//System.out.println("Query:  " + query);
				
				try
				{
					Statement statement = RoleBasedAccessControl.getConnection(s).createStatement();
					statement.executeUpdate(query);
				}
				catch ( SQLException sqle )
				{
					s.setMessage( "Error updating employee profile" );
					sqle.printStackTrace();
				}
			}
			catch ( Exception e )
			{
				s.setMessage( "Error updating employee profile" );
				e.printStackTrace();
			}			
		}
		else
		{
			throw new UnauthorizedException(); // FIX
		}
	}

}
