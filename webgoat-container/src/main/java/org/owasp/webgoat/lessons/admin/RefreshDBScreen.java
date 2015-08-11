
package org.owasp.webgoat.lessons.admin;

import java.sql.Connection;
import org.owasp.webgoat.lessons.*;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.*;


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
public class RefreshDBScreen extends LessonAdapter
{

    private final static String REFRESH = "Refresh";

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

        try
        {
            boolean refresh = s.getParser().getBooleanParameter(REFRESH, false);

            if (refresh)
            {
                refreshDB(s);
                ec.addElement(new StringElement("Successfully refreshed the database."));
            }
            else
            {
                Element label = new StringElement("Refresh the database? ");
                A link1 = ECSFactory.makeLink("Yes", REFRESH, true);
                A link2 = ECSFactory.makeLink("No", REFRESH, false);
                TD td1 = new TD().addElement(label);
                TD td2 = new TD().addElement(link1);
                TD td3 = new TD().addElement(link2);
                TR row = new TR().addElement(td1).addElement(td2).addElement(td3);
                Table t = new Table().setCellSpacing(40).setWidth("50%");

                if (s.isColor())
                {
                    t.setBorder(1);
                }

                t.addElement(row);
                ec.addElement(t);
            }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        return (ec);
    }

    /**
     * Gets the category attribute of the RefreshDBScreen object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.ADMIN_FUNCTIONS;
    }

    private final static Integer DEFAULT_RANKING = new Integer(1000);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the role attribute of the RefreshDBScreen object
     * 
     * @return The role value
     */
    public String getRole()
    {
        return ADMIN_ROLE;
    }

    /**
     * Gets the title attribute of the RefreshDBScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Refresh Database");
    }

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     */
    public void refreshDB(WebSession s)
    {
        try
        {
            Connection connection = DatabaseUtilities.getConnection(s);

            CreateDB db = new CreateDB();
            db.makeDB(connection);
            System.out.println("Successfully refreshed the database.");
        } catch (Exception e)
        {
            s.setMessage("Error refreshing database " + this.getClass().getName());
            e.printStackTrace();
        }
    }
}
