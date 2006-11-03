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
 * @author     Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */
public class ParameterInjection extends LessonAdapter
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
		return super.createContent(s);
	}
	/**
	 *  Gets the category attribute of the CommandInjection object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.A6;
	}




	private final static Integer DEFAULT_RANKING = new Integer(40);

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
		return ( "How to Perform Parameter Injection" );
	}

	public Element getCredits()
	{
		return new StringElement("This screen created by: Your name could go here");
	}
}

