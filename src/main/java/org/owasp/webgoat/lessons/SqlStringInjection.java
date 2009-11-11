
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.WebGoatI18N;


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
public class SqlStringInjection extends SequentialLessonAdapter
{
	private final static String ACCT_NAME = "account_name";

	private static String STAGE = "stage";

	private String accountName;

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	protected Element doStage1(WebSession s) throws Exception
	{
		return injectableQuery(s);
	}

	protected Element doStage2(WebSession s) throws Exception
	{
		return parameterizedQuery(s);
	}

	protected Element injectableQuery(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			Connection connection = DatabaseUtilities.getConnection(s);

			ec.addElement(makeAccountLine(s));

			String query = "SELECT * FROM user_data WHERE last_name = '" + accountName + "'";
			ec.addElement(new PRE(query));

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
					if (results.getRow() >= 6)
					{
						makeSuccess(s);
						getLessonTracker(s).setStage(2);

						StringBuffer msg = new StringBuffer();

						msg.append(WebGoatI18N.get("StringSqlInjectionSecondStage"));

						s.setMessage(msg.toString());
					}
				}
				else
				{
					ec.addElement(WebGoatI18N.get("NoResultsMatched"));
				}
			} catch (SQLException sqle)
			{
				ec.addElement(new P().addElement(sqle.getMessage()));
				sqle.printStackTrace();
			}
		} catch (Exception e)
		{
			s.setMessage(WebGoatI18N.get("ErrorGenerating") + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	protected Element parameterizedQuery(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(WebGoatI18N.get("StringSqlInjectioNSecondStage"));
		if (s.getParser().getRawParameter(ACCT_NAME, "YOUR_NAME").equals("restart"))
		{
			getLessonTracker(s).getLessonProperties().setProperty(STAGE, "1");
			return (injectableQuery(s));
		}

		ec.addElement(new BR());

		try
		{
			Connection connection = DatabaseUtilities.getConnection(s);

			ec.addElement(makeAccountLine(s));

			String query = "SELECT * FROM user_data WHERE last_name = ?";
			ec.addElement(new PRE(query));

			try
			{
				PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																			ResultSet.CONCUR_READ_ONLY);
				statement.setString(1, accountName);
				ResultSet results = statement.executeQuery();

				if ((results != null) && (results.first() == true))
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
					results.last();

					// If they get back more than one user they succeeded
					if (results.getRow() >= 6)
					{
						makeSuccess(s);
					}
				}
				else
				{
					ec.addElement(WebGoatI18N.get("NoResultsMatched"));
				}
			} catch (SQLException sqle)
			{
				ec.addElement(new P().addElement(sqle.getMessage()));
			}
		} catch (Exception e)
		{
			s.setMessage(WebGoatI18N.get("ErrorGenerating") + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	protected Element makeAccountLine(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement(new P().addElement(WebGoatI18N.get("EnterLastName")));

		accountName = s.getParser().getRawParameter(ACCT_NAME, "Your Name");
		Input input = new Input(Input.TEXT, ACCT_NAME, accountName.toString());
		ec.addElement(input);

		Element b = ECSFactory.makeButton(WebGoatI18N.get("Go!"));
		ec.addElement(b);

		return ec;

	}

	/**
	 * Gets the category attribute of the SqNumericInjection object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{
		return Category.INJECTION;
	}

	/**
	 * Gets the hints attribute of the DatabaseFieldScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		
		hints.add(WebGoatI18N.get("SqlStringInjectionHint1"));
		hints.add(WebGoatI18N.get("SqlStringInjectionHint2"));
		hints.add(WebGoatI18N.get("SqlStringInjectionHint3"));
		hints.add(WebGoatI18N.get("SqlStringInjectionHint4"));

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(75);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the DatabaseFieldScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("String SQL Injection");
	}

	/**
	 * Constructor for the DatabaseFieldScreen object
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

}
