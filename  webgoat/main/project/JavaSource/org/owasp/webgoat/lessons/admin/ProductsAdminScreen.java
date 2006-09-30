package org.owasp.webgoat.lessons.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.DatabaseUtilities;
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
public class ProductsAdminScreen extends LessonAdapter
{
	private final static String QUERY = "SELECT * FROM product_system_data";
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
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			ResultSet results = statement.executeQuery( QUERY );

			if ( results != null )
			{
				makeSuccess( s );
				ResultSetMetaData resultsMetaData = results.getMetaData();
				ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
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
	 *  Gets the category attribute of the ProductsAdminScreen object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{
		return ADMIN_FUNCTIONS;
	}

	/**
	 *  Gets the role attribute of the ProductsAdminScreen object
	 *
	 * @return    The role value
	 */
	public String getRole()
	{
		return HACKED_ADMIN_ROLE;
	}


	/**
	 *  Gets the title attribute of the ProductsAdminScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Product Information" );
	}
	
	private final static Integer DEFAULT_RANKING = new Integer(1000);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}
}

