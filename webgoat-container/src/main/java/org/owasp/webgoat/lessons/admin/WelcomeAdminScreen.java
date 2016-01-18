
package org.owasp.webgoat.lessons.admin;

import org.owasp.webgoat.lessons.WelcomeScreen;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.owasp.webgoat.session.WebSession;


/**
 *************************************************************************************************
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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @since October 28, 2003
 * @version $Id: $Id
 */
public class WelcomeAdminScreen extends WelcomeScreen
{

    /**
     * Constructor for the WelcomeAdminScreen object
     *
     * @param s
     *            Description of the Parameter
     */
    public WelcomeAdminScreen(WebSession s)
    {
        super(s);
    }

    /**
     * Constructor for the WelcomeAdminScreen object
     */
    public WelcomeAdminScreen()
    {
    }

    /**
     * {@inheritDoc}
     *
     * Description of the Method
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(new Center(new H1("You are logged on as an administrator")));
        ec.addElement(super.createContent(s));

        return (ec);
    }

    /**
     * Gets the title attribute of the WelcomeAdminScreen object
     *
     * @return The title value
     */
    public String getTitle()
    {
        return ("Admin Welcome");
    }
}
