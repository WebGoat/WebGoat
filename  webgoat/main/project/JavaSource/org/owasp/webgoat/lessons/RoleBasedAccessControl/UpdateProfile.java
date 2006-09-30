package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;

public class UpdateProfile extends DefaultLessonAction
{
	private LessonAction chainedAction;
	
	public UpdateProfile(AbstractLesson lesson, String lessonName, String actionName, LessonAction chainedAction)
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
			String disciplinaryActionNotes = s.getParser().getStringParameter(RoleBasedAccessControl.DISCIPLINARY_NOTES);
			String personalDescription = s.getParser().getStringParameter(RoleBasedAccessControl.DESCRIPTION);
			
			Employee employee = new Employee(subjectId, firstName, lastName, ssn, title, phone,
					address1, address2, manager, startDate, salary,
					ccn, ccnLimit, disciplinaryActionDate, disciplinaryActionNotes,
					personalDescription);
			
			if (subjectId > 0)
			{
				this.changeEmployeeProfile(s, userId, subjectId, employee);
				setRequestAttribute(s, getLessonName() + "." + RoleBasedAccessControl.EMPLOYEE_ID, Integer.toString(subjectId));
			}
			else
				this.createEmployeeProfile(s, userId, employee);
			
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
				Statement answer_statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
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

	public void changeEmployeeProfile_BACKUP(WebSession s, int userId, int subjectId, Employee employee)
			throws UnauthorizedException
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
				Statement answer_statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
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


	private int getNextUID(WebSession s)
	{
		int uid = -1;
		try
		{
			Statement statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet results = statement.executeQuery("select max(userid) as uid from employee");
			results.first();
			uid = results.getInt("uid");
		}
		catch ( SQLException sqle )
		{
			sqle.printStackTrace();
			s.setMessage( "Error updating employee profile" );
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uid + 1;
	}
	
	
	public void createEmployeeProfile(WebSession s, int userId, Employee employee)
			throws UnauthorizedException
	{
		try
		{
			int newUID = getNextUID(s);
			// FIXME: This max() thing doesn't work on InstantDB.
			String query = "INSERT INTO employee VALUES (" + newUID + ", '"
			+ employee.getFirstName() + "','"
			+ employee.getLastName() + "','"
			+ employee.getSsn() + "','goober57x','"
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
				Statement statement = WebSession.getConnection(s).createStatement();
				statement.executeUpdate(query);
			}
			catch ( SQLException sqle )
			{
				sqle.printStackTrace();
				s.setMessage( "Error updating employee profile" );
			}
			
			query = "INSERT INTO roles VALUES (" + newUID + ", 'hr')";
			
			//System.out.println("Query:  " + query);
			
			try
			{
				Statement statement = WebSession.getConnection(s).createStatement();
				statement.executeUpdate(query);
			}
			catch ( SQLException sqle )
			{
				sqle.printStackTrace();
				s.setMessage( "Error updating employee profile" );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			s.setMessage( "Error updating employee profile" );
		}
	}
}
