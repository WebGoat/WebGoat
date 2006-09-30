package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;

public class FindProfile extends DefaultLessonAction
{
	private LessonAction chainedAction;
	
	public FindProfile(AbstractLesson lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName);
		this.chainedAction = chainedAction;
	}

	public void handleRequest( WebSession s )
			throws ParameterNotFoundException, UnauthenticatedException, UnauthorizedException, ValidationException
	{
		if (isAuthenticated(s))
		{
			int userId = getIntSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.USER_ID);

			String pattern = s.getParser().getRawParameter(RoleBasedAccessControl.SEARCHNAME);
			
			findEmployeeProfile(s, userId, pattern);
			
			// Execute the chained Action if the employee was found.
			if (foundEmployee(s))
			{
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
		}
		else
			throw new UnauthenticatedException();
	}

	public String getNextPage(WebSession s)
	{
		String page = RoleBasedAccessControl.SEARCHSTAFF_ACTION;

		if (foundEmployee(s))
			page = RoleBasedAccessControl.VIEWPROFILE_ACTION;
		
		return page;
	}
	
	private boolean foundEmployee(WebSession s)
	{
		boolean found = false;
		try
		{
			int id = getIntRequestAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ID);
			found = true;
		}
		catch (ParameterNotFoundException e)
		{
		}
		
		return found;
	}
	
	public Employee findEmployeeProfile(WebSession s, int userId, String pattern)
			throws UnauthorizedException
	{
		Employee profile = null;
		// Clear any residual employee id's in the session now.
		removeSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ID);
		
		// Query the database for the profile data of the given employee
		try
		{
			String query = "SELECT * FROM employee WHERE first_name like ? OR last_name = ?";
			
			try
			{
				PreparedStatement answer_statement = WebSession.getConnection(s).prepareStatement( query, 
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				answer_statement.setString(1, "%" + pattern + "%");
				answer_statement.setString(2, "%" + pattern + "%");
				ResultSet answer_results = answer_statement.executeQuery();
				
				// Just use the first hit.
				if (answer_results.next())
				{				
					int id = answer_results.getInt("userid");
					// Note: Do NOT get the password field.
					profile = new Employee(
							id,
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
*/					
					setRequestAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ID, Integer.toString(id));
				}
			}
			catch ( SQLException sqle )
			{
				s.setMessage( "Error finding employee profile" );
				sqle.printStackTrace();
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error finding employee profile" );
			e.printStackTrace();
		}
		
		return profile;
	}

}
