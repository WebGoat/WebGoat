package org.owasp.webgoat.lessons;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.HtmlColor;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
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
public class WelcomeScreen extends Screen
{
	/**
	 *  Constructor for the WelcomeScreen object
	 *
	 * @param  s  Description of the Parameter
	 */
	public WelcomeScreen( WebSession s )
	{
		setup( s );
	}

	/**
	 *  Constructor for the WelcomeScreen object
	 */
	public WelcomeScreen() { }


	public void setup( WebSession s )
	{
		// call createContent first so messages will go somewhere

		Form form = new Form( "attack", Form.POST ).setName( "form" ).setEncType( "" );

		form.addElement( wrapForm( s ) );

		TD lowerright = new TD().setHeight( "100%" ).setVAlign( "top" ).setAlign( "left" ).addElement( form );
		TR row = new TR().addElement( lowerright );
		Table layout = new Table().setBgColor( HtmlColor.WHITE ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );

        layout.addElement( row );
   
         setContent(layout);
	}

	protected Element wrapForm( WebSession s )
	{
		if ( s == null )
		{
			return new StringElement( "Invalid Session" );
		}

		Table container = new Table().setWidth( "100%" ).setCellSpacing( 10 ).setCellPadding( 0 ).setBorder( 0 );
		
		// CreateContent can generate error messages so you MUST call it before makeMessages()
		Element content = createContent( s );
		container.addElement( new TR().addElement( new TD().setColSpan( 2 ).setVAlign( "TOP" ).addElement(
				makeMessages( s ) ) ) );
		container.addElement( new TR().addElement( new TD().setColSpan( 2 ).addElement( content ) ) );
		container.addElement( new TR() );

		return ( container );
	}

	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();
		Element b = ECSFactory.makeButton( "Start the Course!" );
		ec.addElement( new Center( b ) );

		return ( ec );
	}

	public Element getCredits()
	{
		return new ElementContainer();
	}

	/**
	 *  Gets the instructions attribute of the WelcomeScreen object
	 *
	 * @return    The instructions value
	 */
	protected String getInstructions()
	{
		String instructions = "Enter your name and learn how HTTP really works!";

		return ( instructions );
	}


	/**
	 *  Gets the title attribute of the WelcomeScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Welcome to the Penetration Testing Course" );
	}

	/* (non-Javadoc)
	 * @see session.Screen#getRole()
	 */
	public String getRole() {
		return AbstractLesson.USER_ROLE;
	}
}

