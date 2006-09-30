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


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class SqlStringInjection extends LessonAdapter
{
	private final static String ACCT_NAME = "account_name";
	private static Connection connection = null;
	private static String STAGE = "stage";
	private String accountName;

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
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( makeAccountLine( s ) );

			String query = "SELECT * FROM user_data WHERE last_name = '" + accountName +"'";
			ec.addElement( new PRE( query ) );

			try
			{
				Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet results = statement.executeQuery( query );

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
					results.last();
					
					// If they get back more than one user they succeeded
					if ( results.getRow() >= 6 )
					{
						makeSuccess( s );
						getLessonTracker(s).setStage(2);
						
						StringBuffer msg = new StringBuffer();
						
						msg.append("Bet you can't do it again! ");
						msg.append("This lesson has detected your successfull attack ");
						msg.append("and has now switch to a defensive mode. ");
						msg.append("Try again to attack a parameterized query.");
						
						s.setMessage(msg.toString());
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
				" type of attack on a parameterized query.  Type 'restart' in the input field if you wish to " +
				" to return to the injectable query");
		if ( s.getParser().getRawParameter( ACCT_NAME, "YOUR_NAME" ).equals("restart"))
		{
			getLessonTracker(s).getLessonProperties().setProperty(STAGE,"1");
			return( injectableQuery(s));
		}
		
		ec.addElement( new BR() );
		
		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( makeAccountLine( s ) );

			String query = "SELECT * FROM user_data WHERE last_name = ?";
			ec.addElement( new PRE( query ) );

			try
			{
				PreparedStatement statement = connection.prepareStatement( query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				statement.setString(1, accountName);
				ResultSet results = statement.executeQuery();

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
					results.last();
					
					// If they get back more than one user they succeeded
					if ( results.getRow() >= 6 )
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
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}

	protected Element makeAccountLine( WebSession s )
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement( new P().addElement( "Enter your last name: " ) );

		accountName = s.getParser().getRawParameter( ACCT_NAME, "Your Name" );
		Input input = new Input( Input.TEXT, ACCT_NAME, accountName.toString() );
		ec.addElement( input );

		Element b = ECSFactory.makeButton( "Go!" );
		ec.addElement( b );

		return ec;

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
					"\"SELECT * FROM user_data WHERE last_name = \" + accountName " );
		hints.add( "Compound SQL statements can be made by joining multiple tests with keywords like AND and OR." +
					"Try appending a SQL statement that always resolves to true");
		hints.add( "Try entering [ smith' OR '1' = '1 ]." );

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(75);

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
		return ( "How to Perform String SQL Injection" );
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

