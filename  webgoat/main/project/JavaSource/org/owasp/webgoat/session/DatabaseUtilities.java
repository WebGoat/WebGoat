package org.owasp.webgoat.session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.ecs.MultiPartElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

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
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 */
public class DatabaseUtilities
{

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @exception ClassNotFoundException Description of the Exception
     * @exception SQLException Description of the Exception
     */
    public static Connection makeConnection(WebSession s)
	    throws ClassNotFoundException, SQLException
    {
    	return makeConnection(s.getWebgoatContext());
    }
    
    public static Connection makeConnection(WebgoatContext context)
    	throws ClassNotFoundException, SQLException
    {
	Class.forName(context.getDatabaseDriver());

	String password = context.getDatabasePassword();
	String conn = context.getDatabaseConnectionString();
	if (password == null || password.equals("")) {
		return (DriverManager.getConnection(conn));
	} else {
		String user = context.getDatabaseUser();
		return DriverManager.getConnection(conn, user, password);
	}
    }

    /**
     * Description of the Method
     *
     * @param results Description of the Parameter
     * @param resultsMetaData Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @exception IOException Description of the Exception
     * @exception SQLException Description of the Exception
     */
    public static MultiPartElement writeTable(ResultSet results,
	    ResultSetMetaData resultsMetaData) throws IOException, SQLException
    {
	int numColumns = resultsMetaData.getColumnCount();
	results.beforeFirst();

	if (results.next())
	{
	    Table t = new Table(1); // 1 = with border
	    t.setCellPadding(1);

	    TR tr = new TR();

	    for (int i = 1; i < (numColumns + 1); i++)
	    {
		tr.addElement(new TD(new B(resultsMetaData.getColumnName(i))));
	    }

	    t.addElement(tr);
	    results.beforeFirst();

	    while (results.next())
	    {
		TR row = new TR();

		for (int i = 1; i < (numColumns + 1); i++)
		{
			String str = results.getString(i);
			if (str == null)
				str = "";
		    row.addElement(new TD(str.replaceAll(" ", "&nbsp;")));
		}

		t.addElement(row);
	    }

	    return (t);
	}
	else
	{
	    return (new B(
		    "Query Successful; however no data was returned from this query."));
	}
    }
}
