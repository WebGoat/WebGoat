
package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DefaultLessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.FindProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.ListStaff;
import org.owasp.webgoat.lessons.GoatHillsFinancial.Login;
import org.owasp.webgoat.lessons.GoatHillsFinancial.Logout;
import org.owasp.webgoat.lessons.GoatHillsFinancial.SearchStaff;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 */
public class RoleBasedAccessControl extends GoatHillsFinancial
{
    private final static Integer DEFAULT_RANKING = new Integer(125);

    public final static String STAGE1 = "Bypass Business Layer Access Control";

    public final static String STAGE2 = "Add Business Layer Access Control";

    public final static String STAGE3 = "Bypass Data Layer Access Control";

    public final static String STAGE4 = "Add Data Layer Access Control";

    protected void registerActions(String className)
    {
        registerAction(new ListStaff(this, className, LISTSTAFF_ACTION));
        registerAction(new SearchStaff(this, className, SEARCHSTAFF_ACTION));
        registerAction(new ViewProfile(this, className, VIEWPROFILE_ACTION));
        registerAction(new EditProfile(this, className, EDITPROFILE_ACTION));
        
        // This action has not yet been implemented. None of the lessons require it.
        registerAction(new EditProfile(this, className, CREATEPROFILE_ACTION));

        // These actions are special in that they chain to other actions.
        registerAction(new Login(this, className, LOGIN_ACTION, getAction(LISTSTAFF_ACTION)));
        registerAction(new Logout(this, className, LOGOUT_ACTION, getAction(LOGIN_ACTION)));
        registerAction(new FindProfile(this, className, FINDPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
        registerAction(new UpdateProfile(this, className, UPDATEPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
        registerAction(new DeleteProfile(this, className, DELETEPROFILE_ACTION, getAction(LISTSTAFF_ACTION)));
    }

    /**
     * Gets the category attribute of the CommandInjection object
     * 
     * @return The category value
     */
    public Category getDefaultCategory()
    {
        return Category.ACCESS_CONTROL;
    }

    /**
     * Gets the hints attribute of the DirectoryScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Many sites attempt to restrict access to resources by role.");
        hints.add("Developers frequently make mistakes implementing this scheme.");
        hints.add("Attempt combinations of users, roles, and resources.");

        // Stage 1
        hints.add("Stage1: How does the application know that the user selected the delete function?");

        // Stage 2
        hints.add("Stage2: You have to code to check the authorization of the user for the action.");


        // Stage 3
        hints.add("Stage3: How does the application know that the user selected any particular employee to view?");

        // Stage 4
        hints.add("Note that the contents of the staff listing change depending on who is logged in.");

        hints
                .add("Stage4: You have to code to check the authorization of the user for the action on a certain employee.");

        return hints;
    }

    @Override
    public String[] getStages()
    {
        if (getWebgoatContext().isCodingExercises()) return new String[] { STAGE1, STAGE2, STAGE3, STAGE4 };
        return new String[] { STAGE1, STAGE3 };
    }

    /**
     * Gets the instructions attribute of the ParameterInjection object
     * 
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
        String instructions = "";

        if (!getLessonTracker(s).getCompleted())
        {
            String stage = getStage(s);
            if (STAGE1.equals(stage))
            {
                instructions = "Stage 1: Bypass Presentational Layer Access Control.<br />"
                        + "As regular employee 'Tom', exploit weak access control to use the Delete function from the Staff List page. "
                        + "Verify that Tom's profile can be deleted. "
                        + "The passwords for users are their given names in lowercase (e.g. the password for Tom Cat is \"tom\").";
            }
            else if (STAGE2.equals(stage))
            {
                instructions = "Stage 2: Add Business Layer Access Control.<br><br />"
                        + "<b><font color=\"blue\"> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br /><br />"
                        + "Implement a fix to deny unauthorized access to the Delete function. "
                        + "To do this, you will have to alter the WebGoat code. "
                        + "Once you have done this, repeat stage 1 and verify that access to DeleteProfile functionality is properly denied.";
            }
            else if (STAGE3.equals(stage))
            {
                instructions = "Stage 3: Breaking Data Layer Access Control.<br />"
                        + "As regular employee 'Tom', exploit weak access control to View another employee's profile. Verify the access.";
            }
            else if (STAGE4.equals(stage))
            {
                instructions = "Stage 4: Add Data Layer Access Control.<br><br />"
                        + "<b><font color=\"blue\"> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br /><br />"
                        + "Implement a fix to deny unauthorized access to this data. "
                        + "Once you have done this, repeat stage 3, and verify that access to other employee's profiles is properly denied.";
            }
        }

        return instructions;
    }

    public String getLessonSolutionFileName(WebSession s)
    {
        String solutionFileName = null;
        String stage = getStage(s);
        solutionFileName = "/lesson_solutions_1/Lab Access Control/Lab " + stage + ".html";
        return solutionFileName;
    }

    @Override
    public String getSolution(WebSession s)
    {
        String src = null;

        try
        {
            src = readFromFile(new BufferedReader(new FileReader(s.getWebResource(getLessonSolutionFileName(s)))),
                                false);
        } catch (IOException e)
        {
            s.setMessage("Could not find the solution file");
            src = ("Could not find the solution file");
        }
        return src;
    }

    public void handleRequest(WebSession s)
    {
        // Here is where dispatching to the various action handlers happens.
        // It would be a good place verify authorization to use an action.

        // System.out.println("RoleBasedAccessControl.handleRequest()");
        if (s.getLessonSession(this) == null) s.openLessonSession(this);

        String requestedActionName = null;
        try
        {
            requestedActionName = s.getParser().getStringParameter("action");
        } catch (ParameterNotFoundException pnfe)
        {
            // Let them eat login page.
            requestedActionName = LOGIN_ACTION;
        }
        // System.out.println("Requested lesson action: " + requestedActionName);

        try
        {
            DefaultLessonAction action = (DefaultLessonAction) getAction(requestedActionName);
            if (action != null)
            {
                // System.out.println("RoleBasedAccessControl.handleRequest() dispatching to: " +
                // action.getActionName());
                if (!action.requiresAuthentication())
                {
                    // Access to Login does not require authentication.
                    action.handleRequest(s);
                }
                else
                {
                    // ***************CODE HERE*************************

                    // *************************************************
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
            s.setMessage("You are not authorized to perform this function");

            // Update lesson status if necessary.
            String stage = getStage(s);
            if (STAGE2.equals(stage))
            {
                try
                {
                    if (RoleBasedAccessControl.DELETEPROFILE_ACTION.equals(requestedActionName)
                            && !isAuthorized(s, getUserId(s), RoleBasedAccessControl.DELETEPROFILE_ACTION))
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
                            .getAttribute(getLessonName() + "." + RoleBasedAccessControl.USER_ID));
                    int employeeId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID);

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

            // System.out.println("Authorization failure");
            setCurrentAction(s, ERROR_ACTION);
            ue2.printStackTrace();
        } catch (Exception e)
        {
            // All other errors send the user to the generic error page
            // System.out.println("handleRequest() error");
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

        // System.out.println("RoleBasedAccessControl.handleRequest()");
        if (s.getLessonSession(this) == null) s.openLessonSession(this);

        String requestedActionName = null;
        try
        {
            requestedActionName = s.getParser().getStringParameter("action");
        } catch (ParameterNotFoundException pnfe)
        {
            // Let them eat login page.
            requestedActionName = LOGIN_ACTION;
        }
        // System.out.println("Requested lesson action: " + requestedActionName);

        if (requestedActionName != null)
        {
            try
            {
                LessonAction action = getAction(requestedActionName);
                if (action != null)
                {
                    // System.out.println("RoleBasedAccessControl.handleRequest() dispatching to: "
                    // + action.getActionName());
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
                            if (action.isAuthorized(s, userId, action.getActionName()))
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
                        if (RoleBasedAccessControl.DELETEPROFILE_ACTION.equals(requestedActionName)
                                && !isAuthorized(s, getUserId(s), RoleBasedAccessControl.DELETEPROFILE_ACTION))
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
                                .getAttribute(getLessonName() + "." + RoleBasedAccessControl.USER_ID));
                        int employeeId = s.getParser().getIntParameter(RoleBasedAccessControl.EMPLOYEE_ID);

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
            } catch (Exception e)
            {
                // All other errors send the user to the generic error page
                // System.out.println("handleRequest() error");
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
     * Gets the title attribute of the DirectoryScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return "LAB: Role Based Access Control";
    }
}
