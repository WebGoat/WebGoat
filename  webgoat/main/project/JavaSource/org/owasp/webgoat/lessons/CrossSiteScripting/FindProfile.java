package org.owasp.webgoat.lessons.CrossSiteScripting;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			int userId = getIntSessionAttribute(s, getLessonName() + "." + CrossSiteScripting.USER_ID);

			String searchName = null;
			try
			{
				searchName = getRequestParameter(s, CrossSiteScripting.SEARCHNAME);
				
				Employee employee = null;
				
				employee = findEmployeeProfile(s, userId, searchName);
				if (employee == null)
				{
					setSessionAttribute(s, getLessonName() + "." + CrossSiteScripting.SEARCHRESULT_ATTRIBUTE_KEY, 
							"Employee " + searchName + " not found.");
				}
			}
			catch (ValidationException e)
			{
				if (getStage(s) == 6)
				{
				    s.setMessage("Congratulations. You have successfully completed this lesson");
				    getLesson().getLessonTracker( s ).setCompleted( true );
				}
			    throw e;
			}
			
			if (getStage(s) == 5)
			{
				if (searchName.indexOf("<script>") > -1 && searchName.indexOf("alert") > -1 && searchName.indexOf("</script>") > -1)
				{				
					s.setMessage( "Welcome to stage 6 - more input validation" );
					setStage(s, 6);
				}
			}
			
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
		String page = CrossSiteScripting.SEARCHSTAFF_ACTION;

		if (foundEmployee(s))
			page = CrossSiteScripting.VIEWPROFILE_ACTION;
		
		return page;
	}
	
	protected String getRequestParameter(WebSession s, String name)
			throws ParameterNotFoundException, ValidationException
	{
		return s.getParser().getRawParameter(name);
	}
	
	protected String getRequestParameter_BACKUP(WebSession s, String name)
			throws ParameterNotFoundException, ValidationException
	{
		return s.getParser().getRawParameter(name);
	}
	
	public Employee findEmployeeProfile(WebSession s, int userId, String pattern)
			throws UnauthorizedException 
	{
		Employee profile = null;
		
		// Query the database for the profile data of the given employee
		try
		{
			String query = "SELECT * FROM employee WHERE first_name like ? OR last_name like ?";
			
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
					setRequestAttribute(s, getLessonName() + "." + CrossSiteScripting.EMPLOYEE_ID, Integer.toString(id));
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

	private boolean foundEmployee(WebSession s)
	{
		boolean found = false;
		try
		{
			getIntRequestAttribute(s, getLessonName() + "." + CrossSiteScripting.EMPLOYEE_ID);
			found = true;
		}
		catch (ParameterNotFoundException e)
		{
		}
		
		return found;
	}
	
	protected String validate(final String parameter, final Pattern pattern) throws ValidationException
	{
		Matcher matcher = pattern.matcher(parameter);
		if (!matcher.matches())
			throw new ValidationException();
		
		return parameter;								
	}

	protected static Map patterns = new HashMap();
	static
	{
		patterns.put(CrossSiteScripting.SEARCHNAME, Pattern.compile("[a-zA-Z ]{0,20}"));
	}

}
