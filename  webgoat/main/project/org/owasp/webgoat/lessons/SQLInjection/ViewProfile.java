package org.owasp.webgoat.lessons.SQLInjection;

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

		Employee employee = null;
		
		if (isAuthenticated(s))
		{
			String userId = getSessionAttribute(s, getLessonName() + "." + SQLInjection.USER_ID);
			String employeeId = null;
			try
			{
				// User selected employee
				employeeId = s.getParser().getRawParameter(SQLInjection.EMPLOYEE_ID);
			}
			catch (ParameterNotFoundException e)
			{
				// May be an internally selected employee
				employeeId = getRequestAttribute(s, getLessonName() + "." + SQLInjection.EMPLOYEE_ID);
			}

			// FIXME: If this fails and returns null, ViewProfile.jsp will blow up as it expects an Employee.
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

	public Employee getEmployeeProfile_BACKUP(WebSession s, String userId, String subjectUserId)
			throws UnauthorizedException
	{
		// Query the database to determine if this employee has access to this function
		// Query the database for the profile data of the given employee if "owned" by the given user

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
	
	
	private void updateLessonStatus(WebSession s, Employee employee)
	{
		try
		{
			String userId = getSessionAttribute(s, getLessonName() + "." + SQLInjection.USER_ID);
			String employeeId = s.getParser().getRawParameter(SQLInjection.EMPLOYEE_ID);
			switch (getStage(s))
			{
			case 3:
				// If the employee we are viewing is the prize and we are not authorized to have it, 
				// the stage is completed
				if (employee != null && employee.getId() == SQLInjection.PRIZE_EMPLOYEE_ID && 
						!isAuthorizedForEmployee(s, Integer.parseInt(userId), employee.getId()))
				{
					s.setMessage( "Welcome to stage 4" );
					setStage(s, 4);				
				}
				break;
			case 4:
				// If we were denied the employee to view, and we would have been able to view it
				// in the broken state, the stage is completed.
				// This assumes the student hasn't modified getEmployeeProfile_BACKUP().
				if (employee == null)
				{
					Employee targetEmployee = null;
					try
					{
						targetEmployee = getEmployeeProfile_BACKUP(s, userId, employeeId);
					}
					catch (UnauthorizedException e) 
					{
					}
					if (targetEmployee != null && targetEmployee.getId() == SQLInjection.PRIZE_EMPLOYEE_ID)
					{
					    s.setMessage("Congratulations. You have successfully completed this lesson");
					    getLesson().getLessonTracker( s ).setCompleted( true );
					}
				}
				break;
			default:
				break;
			}
		}
		catch (ParameterNotFoundException pnfe)
		{
		}
	}

}
