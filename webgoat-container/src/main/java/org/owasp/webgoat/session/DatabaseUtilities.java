
package org.owasp.webgoat.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


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
 * @version $Id: $Id
 */
//TODO: class we need to refactor to new structure, we can put the connection in the current session of the user
	// start using jdbc template
public class DatabaseUtilities
{

	private static Map<String, Connection> connections = new HashMap<String, Connection>();
	private static Map<String, Boolean> dbBuilt = new HashMap<String, Boolean>();

	/**
	 * <p>getConnection.</p>
	 *
	 * @param s a {@link org.owasp.webgoat.session.WebSession} object.
	 * @return a {@link java.sql.Connection} object.
	 * @throws java.sql.SQLException if any.
	 */
	public static Connection getConnection(WebSession s) throws SQLException
	{
		return getConnection(s.getUserName(), s.getWebgoatContext());
	}

	/**
	 * <p>getConnection.</p>
	 *
	 * @param user a {@link java.lang.String} object.
	 * @param context a {@link org.owasp.webgoat.session.WebgoatContext} object.
	 * @return a {@link java.sql.Connection} object.
	 * @throws java.sql.SQLException if any.
	 */
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

	/**
	 * <p>returnConnection.</p>
	 *
	 * @param user a {@link java.lang.String} object.
	 */
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
		String url = context.getDatabaseConnectionString().replace("{USER}", user);
		return DriverManager.getConnection(url, "sa", "");
	}
	
}
