package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
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
public class RoleBasedAccessControl extends GoatHillsFinancial
{
    private final static Integer DEFAULT_RANKING = new Integer(125);

    /**
     *  Gets the category attribute of the CommandInjection object
     *
     * @return    The category value
     */
    public Category getDefaultCategory()
    {
	return Category.A2;
    }

    /**
     *  Gets the hints attribute of the DirectoryScreen object
     *
     * @return    The hints value
     */
    protected List<String> getHints(WebSession s)
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

    @Override
	public int getStageCount() {
		return 4;
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
		// Update lesson status if necessary.
		if (getStage(s) == 2)
		{
			try
			{
			if (RoleBasedAccessControl.DELETEPROFILE_ACTION.equals(requestedActionName) &&
					!isAuthorized(s, getUserId(s), RoleBasedAccessControl.DELETEPROFILE_ACTION))
			{
				s.setMessage( "Welcome to stage 3 -- exploiting the data layer" );
				setStage(s, 3);
			}
			} catch (ParameterNotFoundException pnfe)
			{
			pnfe.printStackTrace();
			}
		}
		//System.out.println("isAuthorized() exit stage: " + getStage(s));
		// Update lesson status if necessary.
		if (getStage(s) == 4)
		{
			try
			{
			//System.out.println("Checking for stage 4 completion");
			DefaultLessonAction action = (DefaultLessonAction) getAction(getCurrentAction(s));
			int userId = Integer.parseInt((String)s.getRequest().getSession().getAttribute(getLessonName() + "."
					+ RoleBasedAccessControl.USER_ID));
			int employeeId = s.getParser().getIntParameter(
				RoleBasedAccessControl.EMPLOYEE_ID);

			if (!action.isAuthorizedForEmployee(s, userId, employeeId))
			{
			    s.setMessage("Congratulations. You have successfully completed this lesson.");
			    getLessonTracker( s ).setCompleted( true );
			}
			} catch (Exception e)
			{
				// swallow this - shouldn't happen inthe normal course
				// e.printStackTrace();
			}
		}
		
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
			// Update lesson status if necessary.
			if (getStage(s) == 2)
			{
				try
				{
				if (RoleBasedAccessControl.DELETEPROFILE_ACTION.equals(requestedActionName) &&
						!isAuthorized(s, getUserId(s), RoleBasedAccessControl.DELETEPROFILE_ACTION))
				{
					s.setMessage( "Welcome to stage 3 -- exploiting the data layer" );
					setStage(s, 3);
				}
				} catch (ParameterNotFoundException pnfe)
				{
				pnfe.printStackTrace();
				}
			}
			//System.out.println("isAuthorized() exit stage: " + getStage(s));
			// Update lesson status if necessary.
			if (getStage(s) == 4)
			{
				try
				{
				//System.out.println("Checking for stage 4 completion");
				DefaultLessonAction action = (DefaultLessonAction) getAction(getCurrentAction(s));
				int userId = Integer.parseInt((String)s.getRequest().getSession().getAttribute(getLessonName() + "."
						+ RoleBasedAccessControl.USER_ID));
				int employeeId = s.getParser().getIntParameter(
					RoleBasedAccessControl.EMPLOYEE_ID);

				if (!action.isAuthorizedForEmployee(s, userId, employeeId))
				{
				    s.setMessage("Congratulations. You have successfully completed this lesson.");
				    getLessonTracker( s ).setCompleted( true );
				}
				} catch (Exception e)
				{
					// swallow this - shouldn't happen inthe normal course
					// e.printStackTrace();
				}
			}
			
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
}
