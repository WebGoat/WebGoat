package org.owasp.webgoat.lessons.CrossSiteScripting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.ParameterParser;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 */
public class UpdateProfile extends DefaultLessonAction
{

    private LessonAction chainedAction;


    public UpdateProfile(GoatHillsFinancial lesson, String lessonName,
	    String actionName, LessonAction chainedAction)
    {
	super(lesson, lessonName, actionName);
	this.chainedAction = chainedAction;
    }


    public void handleRequest(WebSession s) throws ParameterNotFoundException,
	    UnauthenticatedException, UnauthorizedException,
	    ValidationException
    {
	if (isAuthenticated(s))
	{
	    int userId = getIntSessionAttribute(s, getLessonName() + "."
		    + CrossSiteScripting.USER_ID);

	    int subjectId = s.getParser().getIntParameter(
		    CrossSiteScripting.EMPLOYEE_ID, 0);

	    Employee employee = null;
	    try
	    {
		employee = parseEmployeeProfile(subjectId, s);
	    }
	    catch (ValidationException e)
	    {
		if (getStage(s) == 2)
		{
		    s
			    .setMessage("Welcome to stage 3 - demonstrate Stored XSS again");
		    setStage(s, 3);
		}
		throw e;
	    }

	    if (subjectId > 0)
	    {
		this.changeEmployeeProfile(s, userId, subjectId, employee);
		setRequestAttribute(s, getLessonName() + "."
			+ CrossSiteScripting.EMPLOYEE_ID, Integer
			.toString(subjectId));
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


    protected Employee parseEmployeeProfile(int subjectId, WebSession s)
	    throws ParameterNotFoundException, ValidationException
    {
	// The input validation can be added using a parsing component 
	// or by using an inline regular expression. The parsing component 
	// is the better solution.

	HttpServletRequest request = s.getRequest();
	String firstName = request.getParameter(CrossSiteScripting.FIRST_NAME);
	String lastName = request.getParameter(CrossSiteScripting.LAST_NAME);
	String ssn = request.getParameter(CrossSiteScripting.SSN);
	String title = request.getParameter(CrossSiteScripting.TITLE);
	String phone = request.getParameter(CrossSiteScripting.PHONE_NUMBER);
	String address1 = request.getParameter(CrossSiteScripting.ADDRESS1);
	String address2 = request.getParameter(CrossSiteScripting.ADDRESS2);
	int manager = Integer.parseInt(request
		.getParameter(CrossSiteScripting.MANAGER));
	String startDate = request.getParameter(CrossSiteScripting.START_DATE);
	int salary = Integer.parseInt(request
		.getParameter(CrossSiteScripting.SALARY));
	String ccn = request.getParameter(CrossSiteScripting.CCN);
	int ccnLimit = Integer.parseInt(request
		.getParameter(CrossSiteScripting.CCN_LIMIT));
	String disciplinaryActionDate = request
		.getParameter(CrossSiteScripting.DISCIPLINARY_DATE);
	String disciplinaryActionNotes = request
		.getParameter(CrossSiteScripting.DISCIPLINARY_NOTES);
	String personalDescription = request
		.getParameter(CrossSiteScripting.DESCRIPTION);

	Employee employee = new Employee(subjectId, firstName, lastName, ssn,
		title, phone, address1, address2, manager, startDate, salary,
		ccn, ccnLimit, disciplinaryActionDate, disciplinaryActionNotes,
		personalDescription);

	return employee;
    }


    protected Employee parseEmployeeProfile_BACKUP(int subjectId, WebSession s)
	    throws ParameterNotFoundException, ValidationException
    {
	// The input validation can be added using a parsing component 
	// or by using an inline regular expression. The parsing component 
	// is the better solution.

	HttpServletRequest request = s.getRequest();
	String firstName = request.getParameter(CrossSiteScripting.FIRST_NAME);
	String lastName = request.getParameter(CrossSiteScripting.LAST_NAME);
	String ssn = request.getParameter(CrossSiteScripting.SSN);
	String title = request.getParameter(CrossSiteScripting.TITLE);
	String phone = request.getParameter(CrossSiteScripting.PHONE_NUMBER);
	String address1 = request.getParameter(CrossSiteScripting.ADDRESS1);
	String address2 = request.getParameter(CrossSiteScripting.ADDRESS2);
	int manager = Integer.parseInt(request
		.getParameter(CrossSiteScripting.MANAGER));
	String startDate = request.getParameter(CrossSiteScripting.START_DATE);
	int salary = Integer.parseInt(request
		.getParameter(CrossSiteScripting.SALARY));
	String ccn = request.getParameter(CrossSiteScripting.CCN);
	int ccnLimit = Integer.parseInt(request
		.getParameter(CrossSiteScripting.CCN_LIMIT));
	String disciplinaryActionDate = request
		.getParameter(CrossSiteScripting.DISCIPLINARY_DATE);
	String disciplinaryActionNotes = request
		.getParameter(CrossSiteScripting.DISCIPLINARY_NOTES);
	String personalDescription = request
		.getParameter(CrossSiteScripting.DESCRIPTION);

	Employee employee = new Employee(subjectId, firstName, lastName, ssn,
		title, phone, address1, address2, manager, startDate, salary,
		ccn, ccnLimit, disciplinaryActionDate, disciplinaryActionNotes,
		personalDescription);

	return employee;
    }


    protected Employee doParseEmployeeProfile(int subjectId,
	    ParameterParser parser) throws ParameterNotFoundException,
	    ValidationException
    {
	// Fix this method using the org.owasp.webgoat.session.ParameterParser class 
	return null;
    }


    public String getNextPage(WebSession s)
    {
	return CrossSiteScripting.VIEWPROFILE_ACTION;
    }


    public void changeEmployeeProfile(WebSession s, int userId, int subjectId,
	    Employee employee) throws UnauthorizedException
    {
	try
	{
	    // Note: The password field is ONLY set by ChangePassword
	    String query = "UPDATE employee SET first_name = '"
		    + employee.getFirstName() + "', last_name = '"
		    + employee.getLastName() + "', ssn = '" + employee.getSsn()
		    + "', title = '" + employee.getTitle() + "', phone = '"
		    + employee.getPhoneNumber() + "', address1 = '"
		    + employee.getAddress1() + "', address2 = '"
		    + employee.getAddress2() + "', manager = "
		    + employee.getManager()
		    + ", start_date = '"
		    + employee.getStartDate()
		    + "', ccn = '"
		    + employee.getCcn()
		    + "', ccn_limit = "
		    + employee.getCcnLimit()
		    +
		    //	"', disciplined_date = '" + employee.getDisciplinaryActionDate() +
		    //	"', disciplined_notes = '" + employee.getDisciplinaryActionNotes() +
		    ", personal_description = '"
		    + employee.getPersonalDescription() + "' WHERE userid = "
		    + subjectId;
	    //System.out.println("Query:  " + query);
	    try
	    {
		Statement answer_statement = WebSession.getConnection(s)
			.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		answer_statement.executeUpdate(query);
	    }
	    catch (SQLException sqle)
	    {
		s.setMessage("Error updating employee profile");
		sqle.printStackTrace();
	    }

	}
	catch (Exception e)
	{
	    s.setMessage("Error updating employee profile");
	    e.printStackTrace();
	}
    }


    public void doChangeEmployeeProfile_BACKUP(WebSession s, int userId,
	    int subjectId, Employee employee) throws UnauthorizedException
    {
	try
	{
	    // Note: The password field is ONLY set by ChangePassword
	    String query = "UPDATE employee SET first_name = '"
		    + employee.getFirstName() + "', last_name = '"
		    + employee.getLastName() + "', ssn = '" + employee.getSsn()
		    + "', title = '" + employee.getTitle() + "', phone = '"
		    + employee.getPhoneNumber() + "', address1 = '"
		    + employee.getAddress1() + "', address2 = '"
		    + employee.getAddress2() + "', manager = "
		    + employee.getManager()
		    + ", start_date = '"
		    + employee.getStartDate()
		    + "', ccn = '"
		    + employee.getCcn()
		    + "', ccn_limit = "
		    + employee.getCcnLimit()
		    +
		    //	"', disciplined_date = '" + employee.getDisciplinaryActionDate() +
		    //	"', disciplined_notes = '" + employee.getDisciplinaryActionNotes() +
		    ", personal_description = '"
		    + employee.getPersonalDescription() + "' WHERE userid = "
		    + subjectId;
	    //System.out.println("Query:  " + query);
	    try
	    {
		Statement answer_statement = WebSession.getConnection(s)
			.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		answer_statement.executeUpdate(query);
	    }
	    catch (SQLException sqle)
	    {
		s.setMessage("Error updating employee profile");
		sqle.printStackTrace();
	    }

	}
	catch (Exception e)
	{
	    s.setMessage("Error updating employee profile");
	    e.printStackTrace();
	}
    }


    public void createEmployeeProfile(WebSession s, int userId,
	    Employee employee) throws UnauthorizedException
    {
	try
	{
	    // FIXME: Cannot choose the id because we cannot guarantee uniqueness
	    String query = "INSERT INTO employee VALUES ( max(userid)+1, '"
		    + employee.getFirstName() + "','" + employee.getLastName()
		    + "','" + employee.getSsn() + "','"
		    + employee.getFirstName().toLowerCase() + "','"
		    + employee.getTitle() + "','" + employee.getPhoneNumber()
		    + "','" + employee.getAddress1() + "','"
		    + employee.getAddress2() + "'," + employee.getManager()
		    + ",'" + employee.getStartDate() + "',"
		    + employee.getSalary() + ",'" + employee.getCcn() + "',"
		    + employee.getCcnLimit() + ",'"
		    + employee.getDisciplinaryActionDate() + "','"
		    + employee.getDisciplinaryActionNotes() + "','"
		    + employee.getPersonalDescription() + "')";

	    //System.out.println("Query:  " + query);

	    try
	    {
		Statement statement = WebSession.getConnection(s)
			.createStatement();
		statement.executeUpdate(query);
	    }
	    catch (SQLException sqle)
	    {
		s.setMessage("Error updating employee profile");
		sqle.printStackTrace();
	    }
	}
	catch (Exception e)
	{
	    s.setMessage("Error updating employee profile");
	    e.printStackTrace();
	}
    }


    public void createEmployeeProfile_BACKUP(WebSession s, int userId,
	    Employee employee) throws UnauthorizedException
    {
	try
	{
	    // FIXME: Cannot choose the id because we cannot guarantee uniqueness
	    String query = "INSERT INTO employee VALUES ( max(userid)+1, '"
		    + employee.getFirstName() + "','" + employee.getLastName()
		    + "','" + employee.getSsn() + "','"
		    + employee.getFirstName().toLowerCase() + "','"
		    + employee.getTitle() + "','" + employee.getPhoneNumber()
		    + "','" + employee.getAddress1() + "','"
		    + employee.getAddress2() + "'," + employee.getManager()
		    + ",'" + employee.getStartDate() + "',"
		    + employee.getSalary() + ",'" + employee.getCcn() + "',"
		    + employee.getCcnLimit() + ",'"
		    + employee.getDisciplinaryActionDate() + "','"
		    + employee.getDisciplinaryActionNotes() + "','"
		    + employee.getPersonalDescription() + "')";

	    //System.out.println("Query:  " + query);

	    try
	    {
		Statement statement = WebSession.getConnection(s)
			.createStatement();
		statement.executeUpdate(query);
	    }
	    catch (SQLException sqle)
	    {
		s.setMessage("Error updating employee profile");
		sqle.printStackTrace();
	    }
	}
	catch (Exception e)
	{
	    s.setMessage("Error updating employee profile");
	    e.printStackTrace();
	}
    }


    /**
     * Validates that the given parameter value matches the given regular expression pattern.
     * 
     * @param parameter
     * @param pattern
     * @return
     * @throws ValidationException
     */
    protected String validate(final String parameter, final Pattern pattern)
	    throws ValidationException
    {
	Matcher matcher = pattern.matcher(parameter);
	if (!matcher.matches())
	    throw new ValidationException();

	return parameter;
    }

}
