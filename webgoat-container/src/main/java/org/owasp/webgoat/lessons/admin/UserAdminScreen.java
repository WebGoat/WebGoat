
package org.owasp.webgoat.lessons.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.DatabaseUtilities;
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
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @since October 28, 2003
 * @version $Id: $Id
 */
public class UserAdminScreen extends LessonAdapter
{

    private final static String QUERY = "SELECT * FROM user_system_data";

    /**
     * {@inheritDoc}
     *
     * Description of the Method
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        try
        {
            Connection connection = DatabaseUtilities.getConnection(s);

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                ResultSet.CONCUR_READ_ONLY);
            ResultSet results = statement.executeQuery(QUERY);

            if (results != null)
            {
                makeSuccess(s);
                ResultSetMetaData resultsMetaData = results.getMetaData();
                ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
            }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        return (ec);
    }

    /**
     * Gets the category attribute of the UserAdminScreen object
     *
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.ADMIN_FUNCTIONS;
    }

    private final static Integer DEFAULT_RANKING = new Integer(1000);

    /**
     * <p>getDefaultRanking.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the role attribute of the UserAdminScreen object
     *
     * @return The role value
     */
    public String getRole()
    {
        return HACKED_ADMIN_ROLE;
    }

    /**
     * Gets the title attribute of the UserAdminScreen object
     *
     * @return The title value
     */
    public String getTitle()
    {
        return ("User Information");
    }
}
