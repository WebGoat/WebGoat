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
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;

import org.owasp.webgoat.session.*;


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class ThreadSafetyProblem extends LessonAdapter
{
	private final static String USER_NAME = "username";
	private Connection connection = null;
	private static String currentUser;
	private String originalUser;


	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( new StringElement( "Enter user name: " ) );
			ec.addElement( new Input( Input.TEXT, USER_NAME, "" ) );
			currentUser = s.getParser().getRawParameter( USER_NAME, "" );
			originalUser = currentUser;
			
			// Store the user name
			String user1 = new String( currentUser );
			
			Element b = ECSFactory.makeButton( "Submit" );
			ec.addElement( b );
			ec.addElement( new P() );

			if ( !"".equals( currentUser ) )
			{
				Thread.sleep( 1500 );

				// Get the users info from the DB
				String query = "SELECT * FROM user_system_data WHERE user_name = '" + currentUser + "'";
				Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet results = statement.executeQuery( query );

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ec.addElement("Account information for user: " + originalUser + "<br><br>");
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
				}
				else
				{
					s.setMessage("'" + currentUser + "' is not a user in the WebGoat database.");
				}
			}
			if ( !user1.equals( currentUser ) )
			{
				makeSuccess( s );
			}

		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}


	/**
	 *  Gets the hints attribute of the ConcurrencyScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "Web applications handle many HTTP requests at the same time." );
		hints.add( "Developers use variables that are not thread safe." );
		hints.add( "Show the Java source code and trace the 'currentUser' variable" );
		hints.add( "Open two browsers and send 'jeff' in one and 'dave' in the other." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the ThreadSafetyProblem object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		
		String instructions = "The user should be able to exploit the concurrency error in this web application " + 
							  "and view login information for another user that is attempting the same function " +
							  "at the same time.  <b>This will require the use of two browsers</b>. Valid user " +
							  "names are 'jeff' and 'dave'." +
							  "<p>Please enter your username to access your account.";

		return (instructions );
	}


	private final static Integer DEFAULT_RANKING = new Integer(80);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	protected Category getDefaultCategory()
	{
		return AbstractLesson.GENERAL;
	}
	
	/**
	 *  Gets the title attribute of the ConcurrencyScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Exploit Thread Safety Problems" );
	}


	/**
	 *  Constructor for the ConcurrencyScreen object
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

