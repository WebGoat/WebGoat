package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;

import org.owasp.webgoat.session.WebSession;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Sherif Koussa <a href="http://www.macadamian.com">Macadamian Technologies.</a>
 * @created    November 02, 2006
 */
public class ForcedBrowsing extends LessonAdapter
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

		if ( s.completedHackableConfig() )
		{				
			makeSuccess( s );		
		}
		else 
		{
			ec.addElement( "Can you try to force browse to the config page which  "
					+ "should only be accessed by maintenance personnel.");
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

		return AbstractLesson.A10;
	}


	/**
	 *  Gets the hints attribute of the HelloScreen object
	 *
	 * @return    The hints value
	 */
	public List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "Try to guess the URL for the config page" );
		hints.add( "The config page is guessable and hackable" );
		hints.add( "Play with the URL and try to guess what the can you replace 'attack' with." );
		
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
		return ( "Forced Browsing" );
	}
	
	public Element getCredits()
	{
		return new StringElement("This screen created by: Sherif Koussa");
	}
}

