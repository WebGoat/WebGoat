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


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class SqlNumericInjection extends LessonAdapter
{
	
	private final static String STATION_ID = "station";
	
	private static Connection connection = null;
	private String station;

	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	
	protected Element createContent( WebSession s )
	{
		return super.createStagedContent(s);
	}
	
	protected Element doStage1( WebSession s ) throws Exception
	{
		return injectableQuery( s );
	}
	
	protected Element doStage2( WebSession s ) throws Exception
	{
		return parameterizedQuery( s);
	}
	


	protected Element injectableQuery( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		try
		{

			ec.addElement( makeStationList(s) );
			
			String query;
			
			station = s.getParser().getRawParameter( STATION_ID, null );
			
			if (station == null){
				query = "SELECT * FROM weather_data WHERE station = [station]";
			} else {
				query = "SELECT * FROM weather_data WHERE station = " + station;
			}
			
			ec.addElement( new PRE( query ) );

			if (station == null) 
				return ec;
			
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}
			
			try
			{
				Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet results = statement.executeQuery( query );

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
					results.last();
					
					// If they get back more than one row they succeeded
					if ( results.getRow() > 1 )
					{
						makeSuccess( s );
						getLessonTracker(s).setStage(2);
						s.setMessage("Start this lesson over to attack a parameterized query.");
					}
				}
				else 
				{
					ec.addElement( "No results matched.  Try Again." );
				}

			}
			catch ( SQLException sqle )
			{
				ec.addElement( new P().addElement( sqle.getMessage() ) );
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}

	protected Element parameterizedQuery( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement("Now that you have successfully performed an SQL injection, try the same " +
				" type of attack on a parameterized query.");
//		if ( s.getParser().getRawParameter( ACCT_NUM, "101" ).equals("restart"))
//		{
//			getLessonTracker(s).setStage(1);
//			return( injectableQuery(s));
//		}
		
		ec.addElement( new BR() );

		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( makeStationList(s) );

			String query = "SELECT * FROM weather_data WHERE station = ?";
			
			station = s.getParser().getRawParameter( STATION_ID, null );
			
			ec.addElement( new PRE( query ) );

			if (station == null) 
				return ec;

			try
			{
				PreparedStatement statement = connection.prepareStatement( query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				statement.setInt(1, Integer.parseInt(station));
				ResultSet results = statement.executeQuery();

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
					results.last();
					
					// If they get back more than one row they succeeded
					if ( results.getRow() > 1 )
					{
						makeSuccess( s );
					}
				}
				else 
				{
					ec.addElement( "No results matched.  Try Again." );
				}
			}
			catch ( SQLException sqle )
			{
				ec.addElement( new P().addElement( sqle.getMessage() ) );
			}
			catch ( NumberFormatException npe)
			{
				ec.addElement( new P().addElement( "Error parsing station as a number: " + npe.getMessage() ) );
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}

	protected Element makeStationList( WebSession s ) throws SQLException, ClassNotFoundException
	{
		ElementContainer ec = new ElementContainer();
		
		ec.addElement( new P().addElement( "Select your local weather station: " ) );

		Map stations = getStations( s );
		Select select = new Select(STATION_ID);
		Iterator it = stations.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			select.addElement(new Option(key).addElement((String)stations.get(key)));
		}
		ec.addElement( select );
		ec.addElement( new P() );
		
		Element b = ECSFactory.makeButton( "Go!" );
		ec.addElement( b );

		return ec;
	}

	/**
	 *  Gets the stations from the db
	 *
	 * @return    A map containing each station, indexed by station number
	 */
	protected Map getStations( WebSession s ) throws SQLException, ClassNotFoundException
	{
		
		if ( connection == null )
		{
			connection = DatabaseUtilities.makeConnection( s );
		}

		Map<String, String> stations = new TreeMap<String, String>();
		String query = "SELECT DISTINCT station, name FROM WEATHER_DATA";

		try
		{
			Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			ResultSet results = statement.executeQuery( query );

			if ( ( results != null ) && ( results.first() == true ) )
			{
				results.beforeFirst();
				
				while(results.next()) {
					String station = results.getString("station");
					String name = results.getString("name");
					
					//<START_OMIT_SOURCE>
					if(!station.equals("10001") && !station.equals("11001")) {
						stations.put(station, name);
					}
					//<END_OMIT_SOURCE>
				}
				
				results.close();
			}
		}
		catch ( SQLException sqle )
		{
			sqle.printStackTrace();
		}

		return stations;
	}
	
	/**
	 *  Gets the category attribute of the SqNumericInjection object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.A6;
	}


	/**
	 *  Gets the hints attribute of the DatabaseFieldScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "The application is taking your input and inserting it at the end of a pre-formed SQL command." );
		hints.add( "This is the code for the query being built and issued by WebGoat:<br><br> " +
					"\"SELECT * FROM weather_data WHERE station = \" + station " );
		hints.add( "Compound SQL statements can be made by joining multiple tests with keywords like AND and OR. " +
					"Try appending a SQL statement that always resolves to true.");
		hints.add( "Try entering [ 101 OR 1 = 1 ]." );

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(70);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}
	
	/**
	 *  Gets the title attribute of the DatabaseFieldScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Perform Numeric SQL Injection" );
	}


	/**
	 *  Constructor for the DatabaseFieldScreen object
	 *
	 * @param  s  Description of the Parameter
	 */
	public void handleRequest( WebSession s )
	{
		try
		{
			super.handleRequest( s );

			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}
		}
		catch ( Exception e )
		{
			System.out.println( "Exception caught: " + e );
			e.printStackTrace( System.out );
		}
	}
}

