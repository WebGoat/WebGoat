
package org.owasp.webgoat.lessons.instructor.RoleBasedAccessControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.ViewProfile;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;

/* STAGE 4 FIXES
1. Find the code location where this flaw of directly retrieving the profile without data-level access control checking exists:
    public void handleRequest( WebSession s )
    {   …
        Employee employee = getEmployeeProfile(s, userId, employeeId);
    … }
    public Employee getEmployeeProfile(WebSession s, int employeeId, int subjectUserId) throws UnauthorizedException {  …
        return getEmployeeProfile(s, employeeId, subjectUserId);
    … }
2. The solution requires a data-level access control check to ensure the user has the rights to access the data they are requesting.
    a. There is a common method you can take advantage of: 
            isAuthorizedForEmployee(s, userId, subjectUserId)
        Either tell the student this exists or have them look in DefaultLessonAction.
        Note that this is not required to implement data access control but is for detection of violations.
    b. Uncomment the modified query retrieving the user data to have data access control
        String query = "SELECT * FROM employee,ownership WHERE employee.userid = ownership.employee_id and " +
                        "ownership.employer_id = " + userId + " and ownership.employee_id = " + subjectUserId;
3. Bundle the entire logic with this call and throw an unauthorized exception
            if (isAuthorizedForEmployee(s, userId, subjectUserId))
            {   ...
                //String query = "SELECT * FROM employee WHERE userid = " + subjectUserId;
                String query = "SELECT * FROM employee,ownership WHERE employee.userid = ownership.employee_id and " +
                        "ownership.employer_id = " + userId + " and ownership.employee_id = " + subjectUserId;  // STAGE 4 - FIX
                ...
            }   
            else
            {
                throw new UnauthorizedException();
            }
4. Repeat stage 3 and note that the function fails with a "Not authorized" message.
Adding the access check in the query is providing data-level access control.
The access check from isAuthorizedForEmployee is used to detect a violation.
The same logic could've been applied after the query but isAuthorizedForEmployee provides a nice centralized abstraction of that logic. 
*/

public class ViewProfile_i extends ViewProfile
{
    public ViewProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName)
    {
        super(lesson, lessonName, actionName);
    }

    public Employee getEmployeeProfile(WebSession s, int userId, int subjectUserId) throws UnauthorizedException
    {
        // Query the database to determine if the given employee is owned by the given user
        // Query the database for the profile data of the given employee

        Employee profile = null;

        // isAuthorizedForEmployee() allows us to determine authorization violations

        if (isAuthorizedForEmployee(s, userId, subjectUserId)) // STAGE 4 - (ALTERNATE) FIX
        {
            // Query the database for the profile data of the given employee
            try
            {
                
                // STAGE 4 - FIX
                // String query = "SELECT * FROM employee WHERE userid = " + subjectUserId; 


                // Switch to this query to add Data Access Control
                //
                // Join employee and ownership to get all valid record combinations
                // - qualify on ownership.employer_id to see only the current userId records
                // - qualify on ownership.employee_id to see the current selected employee profile

                // STAGE 4 - FIX
                String query = "SELECT * FROM employee,ownership WHERE employee.userid = ownership.employee_id and "
                        + "ownership.employer_id = " + userId + " and ownership.employee_id = " + subjectUserId; 

                try
                {
                    Statement answer_statement = WebSession.getConnection(s)
                            .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet answer_results = answer_statement.executeQuery(query);
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
/*                      System.out.println("Retrieved employee from db: " + 
                                profile.getFirstName() + " " + profile.getLastName() + 
                                " (" + profile.getId() + ")");
*/                  }
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
        }
        else
        {
            throw new UnauthorizedException(); // STAGE 4 - ALTERNATE FIX
        }

        return profile;
    }

}
