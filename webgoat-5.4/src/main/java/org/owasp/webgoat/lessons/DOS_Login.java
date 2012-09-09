
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.H2;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.session.ParameterNotFoundException;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
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
 * Source for this application is maintained at code.google.com, a repository for free software
 * projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class DOS_Login extends LessonAdapter
{

	/**
	 * Description of the Field
	 */
	protected final static String PASSWORD = "Password";

	/**
	 * Description of the Field
	 */
	protected final static String USERNAME = "Username";

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
			String username = "";
			String password = "";
			username = s.getParser().getRawParameter(USERNAME);
			password = s.getParser().getRawParameter(PASSWORD);

			// don;t allow user name from other lessons. it would be too simple.
			if (username.equals("jeff") || username.equals("dave"))
			{
				ec.addElement(new H2("Login Failed: 'jeff' and 'dave' are not valid for this lesson"));
				return (ec.addElement(makeLogin(s)));
			}

			// Check if the login is valid
			Connection connection = DatabaseUtilities.getConnection(s);

			String query = "SELECT * FROM user_system_data WHERE user_name = '" + username + "' and password = '"
					+ password + "'";
			ec.addElement(new StringElement(query));

			try
			{
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
																	ResultSet.CONCUR_READ_ONLY);
				ResultSet results = statement.executeQuery(query);

				if ((results != null) && (results.first() == true))
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
					results.last();

					// If they get back more than one user they succeeded
					if (results.getRow() >= 1)
					{
						// Make sure this isn't data from an sql injected query.
						if (results.getString(2).equals(username) && results.getString(3).equals(password))
						{
							String insertData1 = "INSERT INTO user_login VALUES ( '" + username + "', '"
									+ s.getUserName() + "' )";
							statement.executeUpdate(insertData1);
						}
						// check the total count of logins
						query = "SELECT * FROM user_login WHERE webgoat_user = '" + s.getUserName() + "'";
						results = statement.executeQuery(query);
						results.last();
						// If they get back more than one user they succeeded
						if (results.getRow() >= 3)
						{
							makeSuccess(s);
							String deleteData1 = "DELETE from user_login WHERE webgoat_user = '" + s.getUserName()
									+ "'";
							statement.executeUpdate(deleteData1);
							return (new H1("Congratulations! Lesson Completed"));
						}

						ec.addElement(new H2("Login Succeeded: Total login count: " + results.getRow()));
					}
				}
				else
				{
					ec.addElement(new H2("Login Failed"));
					// check the total count of logins
					query = "SELECT * FROM user_login WHERE webgoat_user = '" + s.getUserName() + "'";
					results = statement.executeQuery(query);
					results.last();
					ec.addElement(new H2("Successfull login count: " + results.getRow()));

				}
			} catch (SQLException sqle)
			{
				ec.addElement(new P().addElement(sqle.getMessage()));
				sqle.printStackTrace();
			}
		} catch (ParameterNotFoundException pnfe)
		{
			/**
			 * Catching this exception prevents the "Error generating
			 * org.owasp.webgoat.lesson.DOS_Login" message from being displayed on first load. Note
			 * that if we are missing a parameter in the request, we do not want to continue
			 * processing and we simply want to display the default login page.
			 */
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
		}

		return (ec.addElement(makeLogin(s)));
	}

	/**
	 * Gets the category attribute of the WeakAuthenticationCookie object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{
		return Category.DOS;
	}

	/**
	 * Gets the hints attribute of the CookieScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Use a SQL Injection to obtain the user names. ");
		hints
				.add("Try to generate this query: SELECT * FROM user_system_data WHERE user_name = 'goober' and password = 'dont_care' or '1' = '1'");
		hints.add("Try &quot;dont_care' or '1' = '1&quot; in the password field");
		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(90);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the CookieScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Denial of Service from Multiple Logins");
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element makeLogin(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		// add the login fields
		Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);

		if (s.isColor())
		{
			t.setBorder(1);
		}

		TR row1 = new TR();
		TR row2 = new TR();
		row1.addElement(new TD(new StringElement("User Name: ")));
		row2.addElement(new TD(new StringElement("Password: ")));

		Input input1 = new Input(Input.TEXT, USERNAME, "");
		Input input2 = new Input(Input.PASSWORD, PASSWORD, "");
		row1.addElement(new TD(input1));
		row2.addElement(new TD(input2));
		t.addElement(row1);
		t.addElement(row2);

		Element b = ECSFactory.makeButton("Login");
		t.addElement(new TR(new TD(b)));
		ec.addElement(t);

		return (ec);
	}

}
