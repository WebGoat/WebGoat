
package org.owasp.webgoat.session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.ecs.MultiPartElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;


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
 */
public class DatabaseUtilities
{

	private static Map<String, Connection> connections = new HashMap<String, Connection>();
	private static Map<String, Boolean> dbBuilt = new HashMap<String, Boolean>();

	public static Connection getConnection(WebSession s) throws SQLException
	{
		return getConnection(s.getUserName(), s.getWebgoatContext());
	}

	public static synchronized Connection getConnection(String user, WebgoatContext context) throws SQLException
	{
		Connection conn = connections.get(user);
		if (conn != null && !conn.isClosed()) return conn;
		conn = makeConnection(user, context);
		connections.put(user, conn);

		if (dbBuilt.get(user) == null)
		{
			new CreateDB().makeDB(conn);
			dbBuilt.put(user, Boolean.TRUE);
		}

		return conn;
	}

	public static synchronized void returnConnection(String user)
	{
		try
		{
			Connection connection = connections.get(user);
			if (connection == null || connection.isClosed()) return;

			if (connection.getMetaData().getDatabaseProductName().toLowerCase().contains("oracle")) connection.close();
		} catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	private static Connection makeConnection(String user, WebgoatContext context) throws SQLException
	{
		try
	{
		Class.forName(context.getDatabaseDriver());

		if (context.getDatabaseConnectionString().contains("hsqldb")) return getHsqldbConnection(user, context);

		String userPrefix = context.getDatabaseUser();
		String password = context.getDatabasePassword();
		String url = context.getDatabaseConnectionString();
		return DriverManager.getConnection(url, userPrefix + "_" + user, password);
		} catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
			throw new SQLException("Couldn't load the database driver: " + cnfe.getLocalizedMessage());
		}
	}

	private static Connection getHsqldbConnection(String user, WebgoatContext context) throws ClassNotFoundException,
			SQLException
	{
		String url = context.getDatabaseConnectionString().replaceAll("\\$\\{USER\\}", user);
		return DriverManager.getConnection(url, "sa", "");
	}

	/**
	 * Description of the Method
	 * 
	 * @param results
	 *            Description of the Parameter
	 * @param resultsMetaData
	 *            Description of the Parameter
	 * 
	 * @return Description of the Return Value
	 * 
	 * @exception IOException
	 *                Description of the Exception
	 * @exception SQLException
	 *                Description of the Exception
	 */
	public static MultiPartElement writeTable(ResultSet results, ResultSetMetaData resultsMetaData) throws IOException,
			SQLException
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
					if (str == null) str = "";
					row.addElement(new TD(str.replaceAll(" ", "&nbsp;")));
				}

				t.addElement(row);
			}

			return (t);
		}
		else
		{
			return (new B("Query Successful; however no data was returned from this query."));
		}
	}

}
