
package org.owasp.webgoat.lessons.instructor.RoleBasedAccessControl;

import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


/**
 * Copyright (c) 2006 Free Software Foundation developed under the custody of the Open Web
 * Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is
 * published by OWASP under the GPL. You should read and accept the LICENSE before you use, modify
 * and/or redistribute this software.
 * 
 */

/* STAGE 2 FIXES
Solution Summary: Edit RoleBasedAccessControl.java and change handleRequest().  
                  Modify handleRequest() with lines denoted by // STAGE 2 - FIX.
Solution Steps: 
1. This solution adds an access control check in the controller.
   Point out that their architecture may require the check to occur in the business function.
2. Look at the RoleBasedAccessControl class identify where execution happens of an action.
	a. action.handleRequest(s); is not protected by an access control check.
	b. look at handleRequest(s) to determine where access control check should occur.
	c. add protection by a programmatic authorization check before dispatching to the action:
		1. Add an isAuthorized() call before dispatching to the action, 
		       and throw an unauthorized exception.  Tell student this exception exists. 
		   Use eclipse command completion to find the isAuthorized() call on the action.  
		   From command completion - determine calling arguments of isAuthorized()
		   
		    				int userId = action.getUserId(s); 
							if (action.isAuthorized(s, userId, action.getActionName()))
							{
								action.handleRequest(s);
							}
							else				
								throw new UnauthorizedException();

Repeat stage 1 and note that the function fails with a "Not authorized" message.
 Tom will be in the list again, because the DB is reset when lesson restarts.
 Adding the access check in the RoleBasedAccessControl:handleRequest() is putting the check in the “Controller”
 The access check can also be added to DeleteProfile.deleteEmployeeProfile(), which is putting the check in the “Business Function”
*/

public class RoleBasedAccessControl_i extends RoleBasedAccessControl
{

	public void handleRequest(WebSession s)
	{
		// System.out.println("RoleBasedAccessControl.handleRequest()");
		if (s.getLessonSession(this) == null) s.openLessonSession(this);

		String requestedActionName = null;
		try
		{
			requestedActionName = s.getParser().getStringParameter("action");
		} catch (ParameterNotFoundException pnfe)
		{
			// Missing the action - send them back to login.
			requestedActionName = LOGIN_ACTION;
		}

		try
		{
			LessonAction action = getAction(requestedActionName);
			if (action != null)
			{
				// FIXME: This code has gotten much uglier
				// System.out.println("RoleBasedAccessControl.handleRequest() dispatching to: " +
				// action.getActionName());
				if (!action.requiresAuthentication())
				{
					// Access to Login does not require authentication.
					action.handleRequest(s);
				}
				else
				{
					if (action.isAuthenticated(s))
					{
						int userId = action.getUserId(s); // STAGE 2 - FIX

						// action.getActionName() returns the user requested function which
						// is tied to the button click from the listStaff jsp
						//
						// Checking isAuthorized() for the requested action

						if (action.isAuthorized(s, userId, action.getActionName())) // STAGE 2 - FIX
						{
							// Calling the handleRequest() method for the requested action
							action.handleRequest(s);
						}
						else
							throw new UnauthorizedException(); // STAGE 2 - FIX

					}
					else
						throw new UnauthenticatedException();
				}
			}
			else
				setCurrentAction(s, ERROR_ACTION);
		} catch (ParameterNotFoundException pnfe)
		{
			// System.out.println("Missing parameter");
			pnfe.printStackTrace();
			setCurrentAction(s, ERROR_ACTION);
		} catch (ValidationException ve)
		{
			// System.out.println("Validation failed");
			ve.printStackTrace();
			setCurrentAction(s, ERROR_ACTION);
		} catch (UnauthenticatedException ue)
		{
			s.setMessage("Login failed");
			// System.out.println("Authentication failure");
			ue.printStackTrace();
		} catch (UnauthorizedException ue2)
		{
			String stage = getStage(s);
			// Update lesson status if necessary.
			if (STAGE2.equals(stage))
			{
				try
				{
					if (GoatHillsFinancial.DELETEPROFILE_ACTION.equals(requestedActionName)
							&& !isAuthorized(s, getUserId(s), GoatHillsFinancial.DELETEPROFILE_ACTION))
					{
						setStageComplete(s, STAGE2);
					}
				} catch (ParameterNotFoundException pnfe)
				{
					pnfe.printStackTrace();
				}
			}
			// System.out.println("isAuthorized() exit stage: " + getStage(s));
			// Update lesson status if necessary.
			if (STAGE4.equals(stage))
			{
				try
				{
					// System.out.println("Checking for stage 4 completion");
					DefaultLessonAction action = (DefaultLessonAction) getAction(getCurrentAction(s));
					int userId = Integer.parseInt((String) s.getRequest().getSession()
							.getAttribute(getLessonName() + "." + GoatHillsFinancial.USER_ID));
					int employeeId = s.getParser().getIntParameter(GoatHillsFinancial.EMPLOYEE_ID);

					if (!action.isAuthorizedForEmployee(s, userId, employeeId))
					{
						setStageComplete(s, STAGE4);
					}
				} catch (Exception e)
				{
					// swallow this - shouldn't happen inthe normal course
					// e.printStackTrace();
				}
			}

			s.setMessage("You are not authorized to perform this function");
			// System.out.println("Authorization failure");
			setCurrentAction(s, ERROR_ACTION);
			ue2.printStackTrace();
		}

		// All this does for this lesson is ensure that a non-null content exists.
		setContent(new ElementContainer());
	}

}
