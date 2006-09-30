package org.owasp.webgoat.lessons.admin;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Screen;
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
public abstract class AdminScreen extends Screen
{
	/**
	 *  Description of the Field
	 */
	protected String query = null;


	/**
	 *  Constructor for the AdminScreen object
	 *
	 * @param  s  Description of the Parameter
	 * @param  q  Description of the Parameter
	 */
	public AdminScreen( WebSession s, String q )
	{
		setQuery( q );

		// setupAdmin(s);  FIXME: what was this supposed to do?
	}


	/**
	 *  Constructor for the AdminScreen object
	 *
	 * @param  s  Description of the Parameter
	 */
	public AdminScreen( WebSession s ) { }


	/**
	 *  Constructor for the AdminScreen object
	 */
	public AdminScreen() { }


	/**
	 *  Gets the title attribute of the AdminScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Admin Information" );
	}


	public String getRole() {
		return AbstractLesson.ADMIN_ROLE;
	}
	
	/**
	 *  Sets the query attribute of the AdminScreen object
	 *
	 * @param  q  The new query value
	 */
	public void setQuery( String q )
	{
		query = q;
	}
}

