
package org.owasp.webgoat.lessons.instructor.SQLInjection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.SQLInjection.Login;
import org.owasp.webgoat.lessons.SQLInjection.SQLInjection;
import org.owasp.webgoat.session.WebSession;

/*
Solution Summary: Edit Login.java and change login().  
                  Modify login() with lines denoted by // STAGE 2 - FIX.
Solution Steps:
1. Change dynamic query to parameterized query.
   a. Replace the dynamic varaibles with the "?" 
   		String query = "SELECT * FROM employee WHERE userid = ? and password = ?" 
   			
   b. Create a preparedStatement using the new query
   		PreparedStatement answer_statement = SQLInjection.getConnection(s).prepareStatement( 
   				query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY ); 

   c. Set the values of the parameterized query
		answer_statement.setString(1, userId); // STAGE 2 - FIX
		answer_statement.setString(2, password); // STAGE 2 - FIX
   		
   d. Execute the preparedStatement
   		ResultSet answer_results = answer_statement.executeQuery();
*/

public class Login_i extends Login
{
	public Login_i(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName, chainedAction);
	}

	public boolean login(WebSession s, String userId, String password)
	{
		// System.out.println("Logging in to lesson");
		boolean authenticated = false;

		try
		{
			// STAGE 2 - FIX
			String query = "SELECT * FROM employee WHERE userid = ? and password = ?"; 

			try
			{
				
				// STAGE 2 - FIX
				PreparedStatement answer_statement = WebSession.getConnection(s)
						.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				answer_statement.setString(1, userId); // STAGE 2 - FIX
				answer_statement.setString(2, password); // STAGE 2 - FIX
				ResultSet answer_results = answer_statement.executeQuery(); // STAGE 2 - FIX
				if (answer_results.first())
				{
					setSessionAttribute(s, getLessonName() + ".isAuthenticated", Boolean.TRUE);
					setSessionAttribute(s, getLessonName() + "." + SQLInjection.USER_ID, userId);
					authenticated = true;
				}

			} catch (SQLException sqle)
			{
				s.setMessage("Error logging in");
				sqle.printStackTrace();
			}
		} catch (Exception e)
		{
			s.setMessage("Error logging in");
			e.printStackTrace();
		}

		// System.out.println("Lesson login result: " + authenticated);
		return authenticated;
	}

}
