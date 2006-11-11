package org.owasp.webgoat.lessons;

import org.apache.ecs.Element;
import org.apache.ecs.StringElement;

import org.owasp.webgoat.session.WebSession;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Sherif Koussa <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */
public class NewLesson extends LessonAdapter
{
	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		// just to get the generic how to text.
		makeSuccess(s);
		return( new StringElement( "Welcome to the WebGoat hall of fame !!" ) );
	}
	/**
	 *  Gets the category attribute of the NEW_LESSON object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.NEW_LESSON;
	}

	private final static Integer DEFAULT_RANKING = new Integer(10);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the DirectoryScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to add a new WebGoat lesson" );
	}

	public Element getCredits()
	{
		return new StringElement("This screen created by: Sherif Koussa");
	}
}

