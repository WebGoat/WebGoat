
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Option;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.apache.ecs.html.Select;
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
public class SqlNumericInjection extends SequentialLessonAdapter
{
	private final static String STATION_ID = "station";

	private String station;

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

			ec.addElement(makeStationList(s));

			String query;

			station = s.getParser().getRawParameter(STATION_ID, null);

			if (station == null)
			{
				query = "SELECT * FROM weather_data WHERE station = [station]";
			}
			else
			{
				query = "SELECT * FROM weather_data WHERE station = " + station;
			}

			ec.addElement(new PRE(query));

			if (station == null) return ec;

			Connection connection = DatabaseUtilities.getConnection(s);

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

					// If they get back more than one row they succeeded
					if (results.getRow() > 1)
					{
						makeSuccess(s);
						getLessonTracker(s).setStage(2);
						StringBuffer msg = new StringBuffer();
						
						msg.append(WebGoatI18N.get("NumericSqlInjectionSecondStage"));

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

		ec.addElement(WebGoatI18N.get("NumericSqlInjectionSecondStage2"));
		// if ( s.getParser().getRawParameter( ACCT_NUM, "101" ).equals("restart"))
		// {
		// getLessonTracker(s).setStage(1);
		// return( injectableQuery(s));
		// }

		ec.addElement(new BR());

		try
		{
			Connection connection = DatabaseUtilities.getConnection(s);

			ec.addElement(makeStationList(s));

			String query = "SELECT * FROM weather_data WHERE station = ?";

			station = s.getParser().getRawParameter(STATION_ID, null);

			ec.addElement(new PRE(query));

			if (station == null) return ec;

			try
			{
				PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																			ResultSet.CONCUR_READ_ONLY);
				statement.setInt(1, Integer.parseInt(station));
				ResultSet results = statement.executeQuery();

				if ((results != null) && (results.first() == true))
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
					results.last();

					// If they get back more than one row they succeeded
					if (results.getRow() > 1)
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
			} catch (NumberFormatException npe)
			{
				ec.addElement(new P().addElement(WebGoatI18N.get("ErrorParsingAsNumber") + npe.getMessage()));
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	protected Element makeStationList(WebSession s) throws SQLException, ClassNotFoundException
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(new P().addElement(WebGoatI18N.get("SelectYourStation")));

		Map<String, String> stations = getStations(s);
		Select select = new Select(STATION_ID);
		Iterator<String> it = stations.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			select.addElement(new Option(key).addElement((String) stations.get(key)));
		}
		ec.addElement(select);
		ec.addElement(new P());

		Element b = ECSFactory.makeButton(WebGoatI18N.get("Go!"));
		ec.addElement(b);

		return ec;
	}

	/**
	 * Gets the stations from the db
	 * 
	 * @return A map containing each station, indexed by station number
	 */
	protected Map<String, String> getStations(WebSession s) throws SQLException, ClassNotFoundException
	{

		Connection connection = DatabaseUtilities.getConnection(s);

		Map<String, String> stations = new TreeMap<String, String>();
		String query = "SELECT DISTINCT station, name FROM WEATHER_DATA";

		try
		{
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
																ResultSet.CONCUR_READ_ONLY);
			ResultSet results = statement.executeQuery(query);

			if ((results != null) && (results.first() == true))
			{
				results.beforeFirst();

				while (results.next())
				{
					String station = results.getString("station");
					String name = results.getString("name");

					// <START_OMIT_SOURCE>
					if (!station.equals("10001") && !station.equals("11001"))
					{
						stations.put(station, name);
					}
					// <END_OMIT_SOURCE>
				}

				results.close();
			}
		} catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

		return stations;
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
		hints.add(WebGoatI18N.get("SqlNumericInjectionHint1"));
		hints.add(WebGoatI18N.get("SqlNumericInjectionHint2"));
		hints.add(WebGoatI18N.get("SqlNumericInjectionHint3"));
		hints.add(WebGoatI18N.get("SqlNumericInjectionHint4"));
		
		

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(70);

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
		return ("Numeric SQL Injection");
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
