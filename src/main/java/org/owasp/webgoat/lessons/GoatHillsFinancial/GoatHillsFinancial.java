
package org.owasp.webgoat.lessons.GoatHillsFinancial;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.owasp.webgoat.lessons.RandomLessonAdapter;
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
public class GoatHillsFinancial extends RandomLessonAdapter
{
    public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
            .addElement(
                        new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
                                .setVspace(0));

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

    private final static Integer DEFAULT_RANKING = new Integer(125);

    private Map<String, LessonAction> lessonFunctions = new Hashtable<String, LessonAction>();

    public GoatHillsFinancial()
    {
        String myClassName = parseClassName(this.getClass().getName());
        registerActions(myClassName);
    }

    protected void registerActions(String className)
    {
        registerAction(new ListStaff(this, className, LISTSTAFF_ACTION));
        registerAction(new SearchStaff(this, className, SEARCHSTAFF_ACTION));
        registerAction(new ViewProfile(this, className, VIEWPROFILE_ACTION));
        registerAction(new EditProfile(this, className, EDITPROFILE_ACTION));
        registerAction(new EditProfile(this, className, CREATEPROFILE_ACTION));

        // These actions are special in that they chain to other actions.
        registerAction(new Login(this, className, LOGIN_ACTION, getAction(LISTSTAFF_ACTION)));
        registerAction(new Logout(this, className, LOGOUT_ACTION, getAction(LOGIN_ACTION)));
        registerAction(new FindProfile(this, className, FINDPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
        registerAction(new UpdateProfile(this, className, UPDATEPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
        registerAction(new DeleteProfile(this, className, DELETEPROFILE_ACTION, getAction(LISTSTAFF_ACTION)));
    }

    protected final String parseClassName(String fqcn)
    {
        String className = fqcn;

        int lastDotIndex = fqcn.lastIndexOf('.');
        if (lastDotIndex > -1) className = fqcn.substring(lastDotIndex + 1);

        return className;
    }

    protected void registerAction(LessonAction action)
    {
        lessonFunctions.put(action.getActionName(), action);
    }

    public String[] getStages()
    {
        return new String[] {};
    }

    protected List<String> getHints(WebSession s)
    {
        return new ArrayList<String>();
    }

    public String getInstructions(WebSession s)
    {
        return "";
    }

    protected LessonAction getAction(String actionName)
    {
        return lessonFunctions.get(actionName);
    }

    public void handleRequest(WebSession s)
    {
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

        try
        {
            LessonAction action = getAction(requestedActionName);
            if (action == null)
            {
                setCurrentAction(s, ERROR_ACTION);
            }
            else
            {
                // System.out.println("GoatHillsFinancial.handleRequest() dispatching to: " +
                // action.getActionName());
                if (action.requiresAuthentication())
                {
                    if (action.isAuthenticated(s))
                    {
                        action.handleRequest(s);
                    }
                    else
                        throw new UnauthenticatedException();
                }
                else
                {
                    // Access to Login does not require authentication.
                    action.handleRequest(s);
                }
            }
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

    public boolean isAuthorized(WebSession s, int userId, String functionId)
    {
        // System.out.println("Checking authorization from " + getCurrentAction(s));
        LessonAction action = getAction(getCurrentAction(s));
        return action.isAuthorized(s, userId, functionId);
    }

    public int getUserId(WebSession s) throws ParameterNotFoundException
    {
        LessonAction action = getAction(getCurrentAction(s));
        return action.getUserId(s);
    }

    public String getUserName(WebSession s) throws ParameterNotFoundException
    {
        LessonAction action = getAction(getCurrentAction(s));
        return action.getUserName(s);
    }

    protected String getJspPath()
    {
        return "/lessons/" + getLessonName() + "/";
    }

    public String getTemplatePage(WebSession s)
    {
        return getJspPath() + getLessonName() + ".jsp";
    }

    public String getPage(WebSession s)
    {
        String page = getJspPath() + getCurrentAction(s) + ".jsp";

        return page;
    }

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    public String getTitle()
    {
        return "Goat Hills Financials";
    }

    public String getSourceFileName()
    {
        // FIXME: Need to generalize findSourceResource() and use it on the currently active
        // LessonAction delegate to get its source file.
        // return findSourceResource(getCurrentLessonScreen()....);
        return super.getSourceFileName();
    }

    @Override
    protected boolean getDefaultHidden()
    {
        return getClass().equals(GoatHillsFinancial.class);
    }

    public Element getCredits()
    {
        return super.getCustomCredits("", ASPECT_LOGO);
    }

    @Override
    protected String getLessonName()
    {
        String className = getClass().getName();
        int index = className.lastIndexOf('.');
        if (index > -1) return className.substring(index + 1);
        return super.getLessonName();
    }
}
