
package org.owasp.webgoat.lessons.admin;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Screen;
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
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */
public abstract class AdminScreen extends Screen
{

    /**
     * Description of the Field
     */
    protected String query = null;

    /**
     * Constructor for the AdminScreen object
     * 
     * @param s
     *            Description of the Parameter
     * @param q
     *            Description of the Parameter
     */
    public AdminScreen(WebSession s, String q)
    {
        setQuery(q);

        // setupAdmin(s); FIXME: what was this supposed to do?
    }

    /**
     * Constructor for the AdminScreen object
     * 
     * @param s
     *            Description of the Parameter
     */
    public AdminScreen(WebSession s)
    {
    }

    /**
     * Constructor for the AdminScreen object
     */
    public AdminScreen()
    {
    }

    /**
     * Gets the title attribute of the AdminScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Admin Information");
    }

    public String getRole()
    {
        return AbstractLesson.ADMIN_ROLE;
    }

    /**
     * Sets the query attribute of the AdminScreen object
     * 
     * @param q
     *            The new query value
     */
    public void setQuery(String q)
    {
        query = q;
    }
}
