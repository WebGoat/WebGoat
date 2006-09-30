package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;

public class ViewProfile extends DefaultLessonAction
{
	public ViewProfile(AbstractLesson lesson, String lessonName, String actionName)
	{
		super(lesson, lessonName, actionName);
	}

	public void handleRequest( WebSession s ) 
			throws ParameterNotFoundException, UnauthenticatedException, UnauthorizedException
	{
		getLesson().setCurrentAction(s, getActionName());

		if (isAuthenticated(s))
		{
			int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);
			int employeeId = -1;
			try
			{
				// User selected employee
				employeeId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID);
			}
			catch (ParameterNotFoundException e)
			{
				// May be an internally selected employee
				employeeId = getIntRequestAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ID);
			}

			Employee employee = getEmployeeProfile(s, userId, employeeId);
			setSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ATTRIBUTE_KEY, employee);
		}
		else
			throw new UnauthenticatedException();
		
		updateLessonStatus(s);
	}

	private void updateLessonStatus(WebSession s)
	{
		// If the logged in user is not authorized to see the given employee's data, stage is complete.
		try
		{
			int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);
			int employeeId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID);

			if (getStage(s) == 3 && !isAuthorizedForEmployee(s, userId, employeeId))
			{
				s.setMessage( "Welcome to stage 4 -- protecting the data layer" );
				setStage(s, 4);
			}
		}
		catch (ParameterNotFoundException e)
		{
		}
	}

	public String getNextPage(WebSession s)
	{
		return RoleBasedAccessControl.VIEWPROFILE_ACTION;
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
				Statement answer_statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet answer_results = answer_statement.executeQuery( query );
				if (answer_results.next())
				{
					// Note: Do NOT get the password field.
					profile = new Employee(
							answer_results.getInt("userid"),
							answer_results.getString("first_name"),
							answer_results.getString("last_name"),
							answer_results.getString("ssn"),
							answer_results.getString("title"),
							answer_results.getString("phone"),
							answer_results.getString("address1"),
							answer_results.getString("address2"),
							answer_results.getInt("manager"),
							answer_results.getString("start_date"),
							answer_results.getInt("salary"),
							answer_results.getString("ccn"),
							answer_results.getInt("ccn_limit"),
							answer_results.getString("disciplined_date"),
							answer_results.getString("disciplined_notes"),
							answer_results.getString("personal_description"));
/*					System.out.println("Retrieved employee from db: " + 
							profile.getFirstName() + " " + profile.getLastName() + 
							" (" + profile.getId() + ")");
*/				}
			}
			catch ( SQLException sqle )
			{
				s.setMessage( "Error getting employee profile" );
				sqle.printStackTrace();
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error getting employee profile" );
			e.printStackTrace();
		}
		
		return profile;
	}

	public Employee getEmployeeProfile_BACKUP(WebSession s, int userId, int subjectUserId)
			throws UnauthorizedException
	{
		// Query the database to determine if the given employee is owned by the given user
		// Query the database for the profile data of the given employee

		Employee profile = null;
		
		// Query the database for the profile data of the given employee
		try
		{
			String query = "SELECT * FROM employee WHERE userid = " + subjectUserId;
			
			try
			{
				Statement answer_statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet answer_results = answer_statement.executeQuery( query );
				if (answer_results.next())
				{
					// Note: Do NOT get the password field.
					profile = new Employee(
							answer_results.getInt("userid"),
							answer_results.getString("first_name"),
							answer_results.getString("last_name"),
							answer_results.getString("ssn"),
							answer_results.getString("title"),
							answer_results.getString("phone"),
							answer_results.getString("address1"),
							answer_results.getString("address2"),
							answer_results.getInt("manager"),
							answer_results.getString("start_date"),
							answer_results.getInt("salary"),
							answer_results.getString("ccn"),
							answer_results.getInt("ccn_limit"),
							answer_results.getString("disciplined_date"),
							answer_results.getString("disciplined_notes"),
							answer_results.getString("personal_description"));
/*					System.out.println("Retrieved employee from db: " + 
							profile.getFirstName() + " " + profile.getLastName() + 
							" (" + profile.getId() + ")");
*/				}
			}
			catch ( SQLException sqle )
			{
				s.setMessage( "Error getting employee profile" );
				sqle.printStackTrace();
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error getting employee profile" );
			e.printStackTrace();
		}
		
		return profile;		
	}	
}
