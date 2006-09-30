package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.session.EmployeeStub;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;

public class ListStaff extends DefaultLessonAction
{
	public ListStaff(AbstractLesson lesson, String lessonName, String actionName)
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

			List employees = getAllEmployees(s, userId);
			setSessionAttribute(s, getLessonName() + "." + RoleBasedAccessControl.STAFF_ATTRIBUTE_KEY, employees);
		}
		else
			throw new UnauthenticatedException();
	}

	public String getNextPage(WebSession s)
	{
		return RoleBasedAccessControl.LISTSTAFF_ACTION;
	}
	
	
	public List getAllEmployees(WebSession s, int userId)
			throws UnauthorizedException
	{
		// Query the database for all employees "owned" by the given employee
		
		List employees = new Vector();
		
		try
		{
			String query = "SELECT employee.userid,first_name,last_name,role FROM employee,roles WHERE employee.userid=roles.userid and employee.userid in "
				+ "(SELECT employee_id FROM ownership WHERE employer_id = " + userId + ")";
			
			try
			{
				Statement answer_statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet answer_results = answer_statement.executeQuery( query );
				answer_results.beforeFirst();
				while (answer_results.next())
				{
					int employeeId = answer_results.getInt("userid");
					String firstName = answer_results.getString("first_name");
					String lastName = answer_results.getString("last_name");
					String role = answer_results.getString("role");
					//System.out.println("Retrieving employee stub for role " + role);
					EmployeeStub stub = new EmployeeStub(employeeId, firstName, lastName, role);
					employees.add(stub);
				}
			}
			catch ( SQLException sqle )
			{
				s.setMessage( "Error getting employees" );
				sqle.printStackTrace();
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error getting employees" );
			e.printStackTrace();
		}

		
		return employees;
	}
	
	public List getAllEmployees_BACKUP(WebSession s, int userId)
		throws UnauthorizedException
	{
		// Query the database for all employees "owned" by the given employee
		
		List employees = new Vector();
		
		try
		{
			String query = "SELECT employee.userid,first_name,last_name,role FROM employee,roles WHERE employee.userid=roles.userid and employee.userid in "
				+ "(SELECT employee_id FROM ownership WHERE employer_id = " + userId + ")";
			
			try
			{
				Statement answer_statement = WebSession.getConnection(s).createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet answer_results = answer_statement.executeQuery( query );
				answer_results.beforeFirst();
				while (answer_results.next())
				{
					int employeeId = answer_results.getInt("userid");
					String firstName = answer_results.getString("first_name");
					String lastName = answer_results.getString("last_name");
					String role = answer_results.getString("role");
					//System.out.println("Retrieving employee stub for role " + role);
					EmployeeStub stub = new EmployeeStub(employeeId, firstName, lastName, role);
					employees.add(stub);
				}
			}
			catch ( SQLException sqle )
			{
				s.setMessage( "Error getting employees" );
				sqle.printStackTrace();
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error getting employees" );
			e.printStackTrace();
		}
		
		
		return employees;
	}
	
}
