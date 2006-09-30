package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;

public class DeleteProfile extends DefaultLessonAction
{
	private LessonAction chainedAction;
	
	public DeleteProfile(AbstractLesson lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName);
		this.chainedAction = chainedAction;
	}

	public void handleRequest( WebSession s )
			throws ParameterNotFoundException, UnauthenticatedException, UnauthorizedException, ValidationException
	{
		getLesson().setCurrentAction(s, getActionName());

		int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);
		int employeeId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID);
		
		if (isAuthenticated(s))
		{
			deleteEmployeeProfile(s, userId, employeeId);
			
			try
			{
				chainedAction.handleRequest(s);
			}
			catch (UnauthenticatedException ue1)
			{
				System.out.println("Internal server error");
				ue1.printStackTrace();
			}
			catch (UnauthorizedException ue2)
			{
				System.out.println("Internal server error");
				ue2.printStackTrace();
			}
		}
		else
			throw new UnauthenticatedException();
		
		updateLessonStatus(s);
	}
	
	public String getNextPage(WebSession s)
	{
		return RoleBasedAccessControl.LISTSTAFF_ACTION;
	}
	
	
	public void deleteEmployeeProfile(WebSession s, int userId, int employeeId)
			throws UnauthorizedException
	{
		try
		{
			// Note: The password field is ONLY set by ChangePassword
			String query = "DELETE FROM employee WHERE userid = " + employeeId;
			//System.out.println("Query:  " + query);
			try
			{
				Statement statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				statement.executeUpdate(query);
			}
			catch ( SQLException sqle )
			{
				s.setMessage( "Error deleting employee profile" );
				sqle.printStackTrace();
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error deleting employee profile" );
			e.printStackTrace();
		}
	}

	public void deleteEmployeeProfile_BACKUP(WebSession s, int userId, int employeeId)
			throws UnauthorizedException
	{
		try
		{
			// Note: The password field is ONLY set by ChangePassword
			String query = "DELETE FROM employee WHERE userid = " + employeeId;
			//System.out.println("Query:  " + query);
			try
			{
				Statement statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				statement.executeUpdate(query);
			}
			catch ( SQLException sqle )
			{
				s.setMessage( "Error deleting employee profile" );
				sqle.printStackTrace();
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error deleting employee profile" );
			e.printStackTrace();
		}
	}

	
	
	private void updateLessonStatus(WebSession s)
	{
		// If the logged in user is not authorized to be here, stage is complete.
		try
		{
			int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);

			if (!isAuthorized(s, userId, RoleBasedAccessControl.DELETEPROFILE_ACTION))
			{
				s.setMessage( "Welcome to stage 2 -- protecting the business layer" );
				setStage(s, 2);
			}
		}
		catch (ParameterNotFoundException e)
		{
		}
	}

}
