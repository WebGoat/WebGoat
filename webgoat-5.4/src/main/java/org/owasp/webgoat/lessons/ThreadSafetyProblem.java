
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.A;
import org.owasp.webgoat.session.*;


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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */
public class ThreadSafetyProblem extends LessonAdapter
{
	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	private final static String USER_NAME = "username";

	private static String currentUser;

	private String originalUser;

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
			Connection connection = DatabaseUtilities.getConnection(s);

			ec.addElement(new StringElement("Enter user name: "));
			ec.addElement(new Input(Input.TEXT, USER_NAME, ""));
			currentUser = s.getParser().getRawParameter(USER_NAME, "");
			originalUser = currentUser;

			// Store the user name
			String user1 = new String(currentUser);

			Element b = ECSFactory.makeButton("Submit");
			ec.addElement(b);
			ec.addElement(new P());

			if (!"".equals(currentUser))
			{
				Thread.sleep(1500);

				// Get the users info from the DB
				String query = "SELECT * FROM user_system_data WHERE user_name = '" + currentUser + "'";
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
																	ResultSet.CONCUR_READ_ONLY);
				ResultSet results = statement.executeQuery(query);

				if ((results != null) && (results.first() == true))
				{
					ec.addElement("Account information for user: " + originalUser + "<br><br>");
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
				}
				else
				{
					s.setMessage("'" + currentUser + "' is not a user in the WebGoat database.");
				}
			}
			if (!user1.equals(currentUser))
			{
				makeSuccess(s);
			}

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	/**
	 * Gets the hints attribute of the ConcurrencyScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Web applications handle many HTTP requests at the same time.");
		hints.add("Developers use variables that are not thread safe.");
		hints.add("Show the Java source code and trace the 'currentUser' variable");
		hints.add("Open two browsers and send 'jeff' in one and 'dave' in the other.");

		return hints;
	}

	/**
	 * Gets the instructions attribute of the ThreadSafetyProblem object
	 * 
	 * @return The instructions value
	 */
	public String getInstructions(WebSession s)
	{

		String instructions = "The user should be able to exploit the concurrency error in this web application "
				+ "and view login information for another user that is attempting the same function "
				+ "at the same time.  <b>This will require the use of two browsers</b>. Valid user "
				+ "names are 'jeff' and 'dave'." + "<p>Please enter your username to access your account.";

		return (instructions);
	}

	private final static Integer DEFAULT_RANKING = new Integer(80);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	protected Category getDefaultCategory()
	{
		return Category.CONCURRENCY;
	}

	/**
	 * Gets the title attribute of the ConcurrencyScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Thread Safety Problems");
	}

	/**
	 * Constructor for the ConcurrencyScreen object
	 * 
	 * @param s
	 *            Description of the Parameter
	 */
	public void handleRequest(WebSession s)
	{
		try
		{
			super.handleRequest(s);
		} catch (Exception e)
		{
			// System.out.println("Exception caught: " + e);
			e.printStackTrace(System.out);
		}
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}
