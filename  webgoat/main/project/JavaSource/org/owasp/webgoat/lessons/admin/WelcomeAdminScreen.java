package org.owasp.webgoat.lessons.admin;

import org.owasp.webgoat.lessons.WelcomeScreen;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
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
public class WelcomeAdminScreen extends WelcomeScreen
{
	/**
	 *  Constructor for the WelcomeAdminScreen object
	 *
	 * @param  s  Description of the Parameter
	 */
	public WelcomeAdminScreen( WebSession s )
	{
		super( s );
	}


	/**
	 *  Constructor for the WelcomeAdminScreen object
	 */
	public WelcomeAdminScreen() { }



	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement( new Center( new H1( "You are logged on as an administrator" ) ) );
		ec.addElement( super.createContent( s ) );

		return ( ec );
	}


	/**
	 *  Gets the title attribute of the WelcomeAdminScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Admin Welcome" );
	}
}

