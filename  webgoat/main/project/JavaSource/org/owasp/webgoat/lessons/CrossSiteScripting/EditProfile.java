package org.owasp.webgoat.lessons.CrossSiteScripting;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;

public class EditProfile extends DefaultLessonAction
{
	public EditProfile(AbstractLesson lesson, String lessonName, String actionName)
	{
		super(lesson, lessonName, actionName);
	}

	public void handleRequest( WebSession s )
			throws ParameterNotFoundException, UnauthenticatedException, UnauthorizedException
	{
		getLesson().setCurrentAction(s, getActionName());

		if (isAuthenticated(s))
		{
			int userId = getUserId(s);
			int employeeId = s.getParser().getIntParameter(CrossSiteScripting.EMPLOYEE_ID);
			
			Employee employee = getEmployeeProfile(s, userId, employeeId);
			setSessionAttribute(s, getLessonName() + "." + CrossSiteScripting.EMPLOYEE_ATTRIBUTE_KEY, employee);
		}
		else
			throw new UnauthenticatedException();
	}

	public String getNextPage(WebSession s)
	{
		return CrossSiteScripting.EDITPROFILE_ACTION;
	}
	
	public Employee getEmployeeProfile(WebSession s, int userId, int subjectUserId)
			throws UnauthorizedException
	{
		Employee profile = null;
		
		// Query the database for the profile data of the given employee
		try
		{
			String query = "SELECT * FROM employee WHERE userid = ?";
			
			try
			{
				PreparedStatement answer_statement = WebSession.getConnection(s).prepareStatement( query, 
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				answer_statement.setInt(1, subjectUserId);
				ResultSet answer_results = answer_statement.executeQuery();
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
		Employee profile = null;
		
		// Query the database for the profile data of the given employee
		try
		{
			String query = "SELECT * FROM employee WHERE userid = ?";
			
			try
			{
				PreparedStatement answer_statement = WebSession.getConnection(s).prepareStatement( query, 
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				answer_statement.setInt(1, subjectUserId);
				ResultSet answer_results = answer_statement.executeQuery();
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
