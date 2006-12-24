package org.owasp.webgoat.lessons.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.owasp.webgoat.lessons.*;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
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
public class ViewDatabase extends LessonAdapter
{
	private final static String SQL = "sql";
	private static Connection connection = null;


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
			ec.addElement( new StringElement( "Enter a SQL statement: " ) );

			StringBuffer sqlStatement = new StringBuffer( s.getParser().getRawParameter( SQL, "" ) );
			Input input = new Input( Input.TEXT, SQL, sqlStatement.toString() );
			ec.addElement( input );

			Element b = ECSFactory.makeButton( "Go!" );
			ec.addElement( b );

			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}
			
			if(sqlStatement.length() > 0)
			{

				Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet results = statement.executeQuery( sqlStatement.toString() );
	
				if ( ( results != null ) && ( results.first() == true ) )
				{
					makeSuccess( s );
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
				}
				
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
	 *  Gets the category attribute of the DatabaseScreen object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{
		return ADMIN_FUNCTIONS;
	}

	private final static Integer DEFAULT_RANKING = new Integer(1000);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the hints attribute of the DatabaseScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "There are no hints defined" );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the ViewDatabase object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "Please post a message to to the WebGoat forum. Your messages will be available for everyone to read.";

		return ( instructions );
	}


	/**
	 *  Gets the role attribute of the ViewDatabase object
	 *
	 * @return    The role value
	 */
	public String getRole()
	{
		return HACKED_ADMIN_ROLE;
	}


	/**
	 *  Gets the title attribute of the DatabaseScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Database Dump" );
	}
}

