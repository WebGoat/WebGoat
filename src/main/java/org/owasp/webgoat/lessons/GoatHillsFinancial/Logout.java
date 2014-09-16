
package org.owasp.webgoat.lessons.GoatHillsFinancial;

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
public class Logout extends DefaultLessonAction
{

    private LessonAction chainedAction;

    public Logout(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
    {
        super(lesson, lessonName, actionName);
        this.chainedAction = chainedAction;
    }

    public void handleRequest(WebSession s) throws ParameterNotFoundException, ValidationException
    {
        // System.out.println("Logging out");

        setSessionAttribute(s, getLessonName() + ".isAuthenticated", Boolean.FALSE);

        // FIXME: Maybe we should forward to Login.
        try
        {
            chainedAction.handleRequest(s);
        } catch (UnauthenticatedException ue1)
        {
            // System.out.println("Internal server error");
            ue1.printStackTrace();
        } catch (UnauthorizedException ue2)
        {
            // System.out.println("Internal server error");
            ue2.printStackTrace();
        }

    }

    public String getNextPage(WebSession s)
    {
        return chainedAction.getNextPage(s);
    }

}
