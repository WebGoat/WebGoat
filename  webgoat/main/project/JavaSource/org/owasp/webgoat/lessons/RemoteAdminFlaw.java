package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.session.WebSession;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */
public class RemoteAdminFlaw extends LessonAdapter
{

	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		if ( s.completedHackableAdmin() )
		{
			makeSuccess( s );		
		}
		else 
		{
			ec.addElement( "WebGoat has an admin interface.  To 'complete' this lesson you must figure "
					+ "out how to access the administrative interface for WebGoat.");
		}
		return ec;

	}


	/**
	 *  Gets the category attribute of the ForgotPassword object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{

		return AbstractLesson.A2;
	}

	/**
	 *  Gets the hints attribute of the HelloScreen object
	 *
	 * @return    The hints value
	 */
	public List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "WebGoat has 2 admin interfaces." );
		hints.add( "WebGoat has one admin interface that is controlled via a URL parameter and is 'hackable'" );
		hints.add( "WebGoat has one admin interface that is controlled via server side security constraints and should not be 'hackable'" );
		hints.add( "Follow the Source!" );

		return hints;
	}



	private final static Integer DEFAULT_RANKING = new Integer(15);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the HelloScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Remote Admin Access" );
	}
	


}

