package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

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
public class HttpBasics extends LessonAdapter
{
	private final static String PERSON = "person";


	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		StringBuffer person = null;
		try
		{
			ec.addElement( new StringElement( "Enter your name: " ) );

			person = new StringBuffer( s.getParser().getStringParameter( PERSON, "" ) );
			person.reverse();

			Input input = new Input( Input.TEXT, PERSON, person.toString() );
			ec.addElement( input );

			Element b = ECSFactory.makeButton( "Go!" );
			ec.addElement( b );
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		if ( !person.toString().equals( "" ) && getLessonTracker(  s ).getNumVisits() > 3 )
		{
			makeSuccess( s );
		}

		return ( ec );
	}

	/**
	 *  Gets the hints attribute of the HelloScreen object
	 *
	 * @return    The hints value
	 */
	public List getHints()
	{
		List hints = new ArrayList();
		hints.add( "Type in your name and press 'go'" );
		hints.add( "Turn on Show Parameters or other features" );
		hints.add( "Press the Show Lesson Plan button to view a lesson summary" );

		return hints;
	}


	/**
	 *  Gets the ranking attribute of the HelloScreen object
	 *
	 * @return    The ranking value
	 */
	private final static Integer DEFAULT_RANKING = new Integer(10);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}
	
	protected Category getDefaultCategory()
	{
		return AbstractLesson.GENERAL;
	}


	/**
	 *  Gets the title attribute of the HelloScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Http Basics" );
	}
}

