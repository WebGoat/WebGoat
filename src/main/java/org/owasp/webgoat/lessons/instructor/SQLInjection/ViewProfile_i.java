
package org.owasp.webgoat.lessons.instructor.SQLInjection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.SQLInjection.ViewProfile;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;

/*
Solution Summary: Edit ViewProfile.java and change getEmployeeProfile().  
                  Modify getEmployeeProfile() with lines denoted by // STAGE 4 - FIX.

Solution Steps:
1. Change dynamic query to parameterized query.
   a. Replace the dynamic variables with the "?" 
    Old: String query = "SELECT employee.* " +
        "FROM employee,ownership WHERE employee.userid = ownership.employee_id and " +
        "ownership.employer_id = " + userId + " and ownership.employee_id = " + subjectUserId;

    New: String query = "SELECT employee.* " +
          "FROM employee,ownership WHERE employee.userid = ownership.employee_id and " +
          "ownership.employer_id = ? and ownership.employee_id = ?";
            
   b. Create a preparedStatement using the new query
        PreparedStatement answer_statement = SQLInjection.getConnection(s).prepareStatement( 
                query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY ); 

   c. Set the values of the parameterized query
        answer_statement.setInt(1, Integer.parseInt(userId)); // STAGE 4 - FIX
        answer_statement.setInt(2, Integer.parseInt(subjectUserId)); // STAGE 4 - FIX
        
   d. Execute the preparedStatement
        ResultSet answer_results = answer_statement.executeQuery();
*/

public class ViewProfile_i extends ViewProfile
{
    public ViewProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName)
    {
        super(lesson, lessonName, actionName);
    }

    public Employee getEmployeeProfile(WebSession s, String userId, String subjectUserId) throws UnauthorizedException
    {
        // Query the database to determine if this employee has access to this function
        // Query the database for the profile data of the given employee if "owned" by the given
        // user

        Employee profile = null;

        try
        {
            String query = "SELECT employee.* "
                    + "FROM employee,ownership WHERE employee.userid = ownership.employee_id and "
                    + "ownership.employer_id = ? and ownership.employee_id = ?";

            try
            {
                // STAGE 4 - FIX
                PreparedStatement answer_statement = WebSession.getConnection(s).prepareStatement( query, 
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY ); 
                answer_statement.setInt(1, Integer.parseInt(userId)); // STAGE 4 - FIX
                answer_statement.setInt(2, Integer.parseInt(subjectUserId)); // STAGE 4 - FIX
                ResultSet answer_results = answer_statement.executeQuery(); // STAGE 4 - FIX
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
/*                  System.out.println("Retrieved employee from db: " + 
                            profile.getFirstName() + " " + profile.getLastName() + 
                            " (" + profile.getId() + ")");
*/              }
            }
            catch ( SQLException sqle )
            {
                s.setMessage("Error getting employee profile");
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage("Error getting employee profile");
            e.printStackTrace();
        }

        return profile;
    }

}
