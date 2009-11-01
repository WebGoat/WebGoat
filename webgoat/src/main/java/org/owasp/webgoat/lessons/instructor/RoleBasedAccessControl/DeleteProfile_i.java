
package org.owasp.webgoat.lessons.instructor.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.DeleteProfile;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;


public class DeleteProfile_i extends DeleteProfile
{

	public DeleteProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName, chainedAction);
	}

	public void doDeleteEmployeeProfile(WebSession s, int userId, int employeeId) throws UnauthorizedException
	{
		if (s.isAuthorizedInLesson(userId, RoleBasedAccessControl.DELETEPROFILE_ACTION)) // FIX
		{
			try
			{
				String query = "DELETE FROM employee WHERE userid = " + employeeId;
				// System.out.println("Query:  " + query);
				try
				{
					Statement statement = WebSession.getConnection(s)
							.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					statement.executeUpdate(query);
				} catch (SQLException sqle)
				{
					s.setMessage("Error deleting employee profile");
					sqle.printStackTrace();
				}
			} catch (Exception e)
			{
				s.setMessage("Error deleting employee profile");
				e.printStackTrace();
			}
		}
		else
		{
			throw new UnauthorizedException(); // FIX
		}
	}

}
