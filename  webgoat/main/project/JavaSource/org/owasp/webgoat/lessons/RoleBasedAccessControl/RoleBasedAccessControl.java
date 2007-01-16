package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ParameterNotFoundException;
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
public class RoleBasedAccessControl extends LessonAdapter
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

    private final static String LESSON_NAME = "RoleBasedAccessControl";

    private final static String JSP_PATH = "/lessons/" + LESSON_NAME + "/";

    private final static Integer DEFAULT_RANKING = new Integer(120);

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


    public RoleBasedAccessControl()
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
     *  Gets the category attribute of the CommandInjection object
     *
     * @return    The category value
     */
    public Category getDefaultCategory()
    {
	return AbstractLesson.A2;
    }


    /**
     *  Gets the hints attribute of the DirectoryScreen object
     *
     * @return    The hints value
     */
    protected List getHints()
    {
	List<String> hints = new ArrayList<String>();
	hints
		.add("Many sites attempt to restrict access to resources by role.");
	hints
		.add("Developers frequently make mistakes implementing this scheme.");
	hints.add("Attempt combinations of users, roles, and resources.");

	// Stage 1
	hints
		.add("How does the application know that the user selected the delete function?");

	// Stage 2

	// Stage 3
	hints
		.add("How does the application know that the user selected any particular employee to view?");

	// Stage 4
	hints
		.add("Note that the contents of the staff listing change depending on who is logged in.");

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
			    + ": Breaking functional access control.<br>"
			    + "You should be able to login as a regular employee and delete another user's employee "
			    + "profile, even though that is supposed to be an HR-only function.";
		    break;
		case 2:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Implementing access control in the Business Layer<br>"
			    + "Access control has already been implemented in the Presentation Layer, but as we have just "
			    + "seen, this is not enough.  Implement access control in the Businesss Layer to verify "
			    + "authorization to use the Delete function before actually executing it.";
		    break;
		case 3:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Breaking data access control.<br>"
			    + "Data Layer access control is being already done on the staff list, but it has not been "
			    + "globally implemented.  Take advantage of this to login as a regular employee and view the "
			    + "CEO's employee profile.";
		    break;
		case 4:
		    instructions = "Stage "
			    + getStage(s)
			    + ": Implementing access control in the Data Layer.<br>"
			    + "Implement Data Layer access control to prevent unauthorized (and potentially career threatening) "
			    + "access to employee personal data.";
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
	// Here is where dispatching to the various action handlers happens.
	// It would be a good place verify authorization to use an action.

	//System.out.println("RoleBasedAccessControl.handleRequest()");
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
	//System.out.println("Requested lesson action: " + requestedActionName);

	try
	{
	    LessonAction action = getAction(requestedActionName);
	    if (action != null)
	    {
		//System.out.println("RoleBasedAccessControl.handleRequest() dispatching to: " + action.getActionName());
		if (!action.requiresAuthentication())
		{
		    // Access to Login does not require authentication.
		    action.handleRequest(s);
		}
		else
		{
		    if (action.isAuthenticated(s))
		    {
			action.handleRequest(s);
		    }
		    else
			throw new UnauthenticatedException();
		}
	    }
	    else
		setCurrentAction(s, ERROR_ACTION);
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
	    setCurrentAction(s, ERROR_ACTION);
	    ue2.printStackTrace();
	}
	catch (Exception e)
	{
	    // All other errors send the user to the generic error page
	    System.out.println("handleRequest() error");
	    e.printStackTrace();
	    setCurrentAction(s, ERROR_ACTION);
	}

	// All this does for this lesson is ensure that a non-null content exists.
	setContent(new ElementContainer());
    }


    public void handleRequest_BACKUP(WebSession s)
    {
	// Here is where dispatching to the various action handlers happens.
	// It would be a good place verify authorization to use an action.

	//System.out.println("RoleBasedAccessControl.handleRequest()");
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
	//System.out.println("Requested lesson action: " + requestedActionName);

	if (requestedActionName != null)
	{
	    try
	    {
		LessonAction action = getAction(requestedActionName);
		if (action != null)
		{
		    //System.out.println("RoleBasedAccessControl.handleRequest() dispatching to: " + action.getActionName());
		    if (!action.requiresAuthentication())
		    {
			// Access to Login does not require authentication.
			action.handleRequest(s);
		    }
		    else
		    {
			if (action.isAuthenticated(s))
			{
			    int userId = action.getUserId(s);
			    if (action.isAuthorized(s, userId, action
				    .getActionName()))
			    {
				action.handleRequest(s);
			    }
			    else
			    {
				throw new UnauthorizedException();
			    }
			}
			else
			    throw new UnauthenticatedException();
		    }
		}
		else
		    setCurrentAction(s, ERROR_ACTION);
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
		setCurrentAction(s, ERROR_ACTION);
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
     *  Gets the title attribute of the DirectoryScreen object
     *
     * @return    The title value
     */
    public String getTitle()
    {
	return "LAB: Role Based Access Control";
    }


    public String getSourceFileName()
    {
	// FIXME: Need to generalize findSourceResource() and use it on the currently active 
	// LessonAction delegate to get its source file.
	//return findSourceResource(getCurrentLessonScreen()....);
	return super.getSourceFileName();
    }

}
