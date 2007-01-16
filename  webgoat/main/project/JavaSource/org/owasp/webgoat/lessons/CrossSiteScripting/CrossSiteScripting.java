package org.owasp.webgoat.lessons.CrossSiteScripting;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.DeleteProfile;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.ListStaff;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.Login;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.Logout;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.SearchStaff;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;

/**
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
 *
 */
public class CrossSiteScripting extends LessonAdapter
{

    public final static String DESCRIPTION = "description";

    public final static String DISCIPLINARY_DATE = "disciplinaryDate";

    public final static String DISCIPLINARY_NOTES = "disciplinaryNotes";

    public final static String CCN_LIMIT = "ccnLimit";

    public final static String CCN = "ccn";

    public final static String SALARY = "salary";

    public final static String START_DATE = "startDate";

    public final static String MANAGER = "manager";

    public final static String ADDRESS1 = "address1";

    public final static String ADDRESS2 = "address2";

    public final static String PHONE_NUMBER = "phoneNumber";

    public final static String TITLE = "title";

    public final static String SSN = "ssn";

    public final static String LAST_NAME = "lastName";

    public final static String FIRST_NAME = "firstName";

    public final static String PASSWORD = "password";

    public final static String EMPLOYEE_ID = "employee_id";

    public final static String USER_ID = "user_id";

    public final static String SEARCHNAME = "search_name";

    public final static String SEARCHRESULT_ATTRIBUTE_KEY = "SearchResult";

    public final static String EMPLOYEE_ATTRIBUTE_KEY = "Employee";

    public final static String STAFF_ATTRIBUTE_KEY = "Staff";

    public final static String LOGIN_ACTION = "Login";

    public final static String LOGOUT_ACTION = "Logout";

    public final static String LISTSTAFF_ACTION = "ListStaff";

    public final static String SEARCHSTAFF_ACTION = "SearchStaff";

    public final static String FINDPROFILE_ACTION = "FindProfile";

    public final static String VIEWPROFILE_ACTION = "ViewProfile";

    public final static String EDITPROFILE_ACTION = "EditProfile";

    public final static String UPDATEPROFILE_ACTION = "UpdateProfile";

    public final static String CREATEPROFILE_ACTION = "CreateProfile";

    public final static String DELETEPROFILE_ACTION = "DeleteProfile";

    public final static String ERROR_ACTION = "error";

    private final static String LESSON_NAME = "CrossSiteScripting";

    private final static String JSP_PATH = "/lessons/" + LESSON_NAME + "/";

    private final static Integer DEFAULT_RANKING = new Integer(100);

    private static Connection connection = null;

    private Map lessonFunctions = new Hashtable();


    public static synchronized Connection getConnection(WebSession s)
	    throws SQLException, ClassNotFoundException
    {
	if (connection == null)
	{
	    connection = DatabaseUtilities.makeConnection(s);
	}

	return connection;
    }


    public CrossSiteScripting()
    {
	String myClassName = parseClassName(this.getClass().getName());
	registerAction(new ListStaff(this, myClassName, LISTSTAFF_ACTION));
	registerAction(new SearchStaff(this, myClassName, SEARCHSTAFF_ACTION));
	registerAction(new ViewProfile(this, myClassName, VIEWPROFILE_ACTION));
	registerAction(new EditProfile(this, myClassName, EDITPROFILE_ACTION));
	registerAction(new EditProfile(this, myClassName, CREATEPROFILE_ACTION));

	// These actions are special in that they chain to other actions.
	registerAction(new Login(this, myClassName, LOGIN_ACTION,
		getAction(LISTSTAFF_ACTION)));
	registerAction(new Logout(this, myClassName, LOGOUT_ACTION,
		getAction(LOGIN_ACTION)));
	registerAction(new FindProfile(this, myClassName, FINDPROFILE_ACTION,
		getAction(VIEWPROFILE_ACTION)));
	registerAction(new UpdateProfile(this, myClassName,
		UPDATEPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
	registerAction(new DeleteProfile(this, myClassName,
		DELETEPROFILE_ACTION, getAction(LISTSTAFF_ACTION)));
    }


    protected static String parseClassName(String fqcn)
    {
	String className = fqcn;

	int lastDotIndex = fqcn.lastIndexOf('.');
	if (lastDotIndex > -1)
	    className = fqcn.substring(lastDotIndex + 1);

	return className;
    }


    protected void registerAction(LessonAction action)
    {
	lessonFunctions.put(action.getActionName(), action);
    }


    /**
     *  Gets the category attribute of the CrossSiteScripting object
     *
     * @return    The category value
     */
    public Category getDefaultCategory()
    {
	return AbstractLesson.A4;
    }


    /**
     *  Gets the hints attribute of the DirectoryScreen object
     *
     * @return    The hints value
     */
    protected List getHints()
    {
	List<String> hints = new ArrayList<String>();

	// Stage 1
	hints.add("You can put HTML tags in form input fields.");
	hints
		.add("Bury a SCRIPT tag in the field to attack anyone who reads it.");
	hints
		.add("Enter this: &lt;script language=\"javascript\" type=\"text/javascript\"&gt;alert(\"Ha Ha Ha\");&lt;/script&gt; in message fields.");
	hints
		.add("Enter this: &lt;script&gtalert(\"document.cookie\");&lt;/script&gt; in message fields.");

	// Stage 2
	hints
		.add("Many scripts rely on the use of special characters such as: &lt;");
	hints
		.add("Allowing only a certain set of characters (positive filtering) is preferred to blocking a set of characters (negative filtering).");
	hints
		.add("The java.util.regex package is useful for filtering string values.");

	// Stage 3
	hints
		.add("Browsers recognize and decode HTML entity encoded content after parsing and interpretting HTML tags.");
	hints
		.add("An HTML entity encoder is provided in the ParameterParser class.");

	// Stage 4
	hints
		.add("Examine content served in response to form submissions looking for data taken from the form.");

	// Stage 5
	hints
		.add("Validate early.  Consider: out.println(\"Order for \" + request.getParameter(\"product\") + \" being processed...\");");

	return hints;
    }


    /**
     *  Gets the instructions attribute of the ParameterInjection object
     *
     * @return    The instructions value
     */
    public String getInstructions(WebSession s)
    {
	String instructions = "";

	if (!getLessonTracker(s).getCompleted())
	{
	    switch (getStage(s))
	    {
		case 1:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Execute a Stored Cross Site Scripting (XSS) attack.<br>"
			    + "For this exercise, your mission is to cause the application to serve a script of your making "
			    + " to some other user.";
		    break;
		case 2:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Block Stored XSS using Input Validation.<br>"
			    + "You will modify the application to perform input validation on the vulnerable input field "
			    + "you just exploited.";
		    break;
		case 3:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Execute a previously Stored Cross Site Scripting (XSS) attack.<br>"
			    + "The application is still vulnerable to scripts in the database.  Trigger a pre-stored "
			    + "script by logging in as employee 'David' and viewing Bruce's profile.";
		    break;
		case 4:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Block Stored XSS using Output Encoding.<br>"
			    + "Encode data served from the database to the client so that any scripts are rendered harmless.";
		    break;
		case 5:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Execute a Reflected XSS attack.<br>"
			    + "Your goal here is to craft a link containing a script which the application will "
			    + "serve right back to any client that activates the link.";
		    break;
		case 6:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Block Reflected XSS using Input Validation.<br>"
			    + "Use the input validation techniques learned ealier in this lesson to close the vulnerability "
			    + "you just exploited.";
		    break;
		default:
		    // Illegal stage value
		    break;
	    }
	}

	return instructions;

    }


    protected LessonAction getAction(String actionName)
    {
	return (LessonAction) lessonFunctions.get(actionName);
    }


    public void handleRequest(WebSession s)
    {
	if (s.getLessonSession(this) == null)
	    s.openLessonSession(this);

	String requestedActionName = null;
	try
	{
	    requestedActionName = s.getParser().getStringParameter("action");
	}
	catch (ParameterNotFoundException pnfe)
	{
	    // Let them eat login page.
	    requestedActionName = LOGIN_ACTION;
	}

	if (requestedActionName != null)
	{
	    try
	    {
		LessonAction action = getAction(requestedActionName);

		if (action != null)
		{
		    if (!action.requiresAuthentication()
			    || action.isAuthenticated(s))
		    {
			action.handleRequest(s);
			//setCurrentAction(s, action.getNextPage(s));
		    }
		}
		else
		{
		    setCurrentAction(s, ERROR_ACTION);
		}
	    }
	    catch (ParameterNotFoundException pnfe)
	    {
		System.out.println("Missing parameter");
		pnfe.printStackTrace();
		setCurrentAction(s, ERROR_ACTION);
	    }
	    catch (ValidationException ve)
	    {
		System.out.println("Validation failed");
		ve.printStackTrace();
		setCurrentAction(s, ERROR_ACTION);
	    }
	    catch (UnauthenticatedException ue)
	    {
		s.setMessage("Login failed");
		System.out.println("Authentication failure");
		ue.printStackTrace();
	    }
	    catch (UnauthorizedException ue2)
	    {
		s.setMessage("You are not authorized to perform this function");
		System.out.println("Authorization failure");
		ue2.printStackTrace();
	    }
	    catch (Exception e)
	    {
		// All other errors send the user to the generic error page
		System.out.println("handleRequest() error");
		e.printStackTrace();
		setCurrentAction(s, ERROR_ACTION);
	    }
	}

	// All this does for this lesson is ensure that a non-null content exists.
	setContent(new ElementContainer());
    }


    public boolean isAuthorized(WebSession s, int userId, String functionId)
    {
	//System.out.println("Checking authorization from " + getCurrentAction(s));
	LessonAction action = (LessonAction) lessonFunctions
		.get(getCurrentAction(s));
	return action.isAuthorized(s, userId, functionId);
    }


    public int getUserId(WebSession s) throws ParameterNotFoundException
    {
	LessonAction action = (LessonAction) lessonFunctions
		.get(getCurrentAction(s));
	return action.getUserId(s);
    }


    public String getUserName(WebSession s) throws ParameterNotFoundException
    {
	LessonAction action = (LessonAction) lessonFunctions
		.get(getCurrentAction(s));
	return action.getUserName(s);
    }


    public String getTemplatePage(WebSession s)
    {
	return JSP_PATH + LESSON_NAME + ".jsp";
    }


    public String getPage(WebSession s)
    {
	String page = JSP_PATH + getCurrentAction(s) + ".jsp";
	//System.out.println("Retrieved sub-view page for " + this.getClass().getName() + " of " + page);

	return page;
    }


    protected Integer getDefaultRanking()
    {
	return DEFAULT_RANKING;
    }


    /**
     *  Gets the title attribute of the CrossSiteScripting object
     *
     * @return    The title value
     */
    public String getTitle()
    {
	return "LAB: Cross Site Scripting (XSS)";
    }


    public String getSourceFileName()
    {
	// FIXME: Need to generalize findSourceResource() and use it on the currently active 
	// LessonAction delegate to get its source file.
	//return findSourceResource(getCurrentLessonScreen()....);
	return super.getSourceFileName();
    }

}
