
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.WebGoatI18N;


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
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class RemoteAdminFlaw extends LessonAdapter
{

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        if (s.completedHackableAdmin())
        {
            makeSuccess(s);
        }
        return ec;

    }

    /**
     * Gets the category attribute of the ForgotPassword object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.ACCESS_CONTROL;
    }

    /**
     * Gets the hints attribute of the HelloScreen object
     * 
     * @return The hints value
     */
    public List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add(WebGoatI18N.get("RemoteAdminFlawHint1"));
        hints.add(WebGoatI18N.get("RemoteAdminFlawHint2"));
        hints.add(WebGoatI18N.get("RemoteAdminFlawHint3"));
        hints.add(WebGoatI18N.get("RemoteAdminFlawHint4"));
        hints.add(WebGoatI18N.get("RemoteAdminFlawHint5"));

        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(160);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the HelloScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Remote Admin Access");
    }

}
