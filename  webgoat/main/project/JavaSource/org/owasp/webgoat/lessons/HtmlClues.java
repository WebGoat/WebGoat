package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.Comment;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import org.owasp.webgoat.session.ECSFactory;
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
public class HtmlClues extends LessonAdapter
{
	/**
	 *  Description of the Field
	 */
	protected final static String PASSWORD = "Password";
	/**
	 *  Description of the Field
	 */
	protected final static String USERNAME = "Username";
	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	private boolean backdoor( WebSession s )
	{
		String username = s.getParser().getRawParameter( USERNAME, "" );
		String password = s.getParser().getRawParameter( PASSWORD, "" );

		//<START_OMIT_SOURCE>
		return ( username.equals( "admin" ) && password.equals( "adminpw" ) );
		//<END_OMIT_SOURCE>
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

		try
		{
			//<START_OMIT_SOURCE>
			ec.addElement( new Comment( "FIXME admin:adminpw" ) );
			//<END_OMIT_SOURCE>
			ec.addElement( new Comment( "Use Admin to regenerate database" ) );

			if ( backdoor( s ) )
			{
				makeSuccess( s );

				s.setMessage( "BINGO -- admin authenticated" );
				ec.addElement( makeUser( s, "jsnow", "CREDENTIALS" ) );
			}
			else
			{
				ec.addElement( makeLogin( s ) );
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
		}

		return ( ec );
	}

	
	/**
	 *  Description of the Method
	 *
	 * @param  s              Description of the Parameter
	 * @param  user           Description of the Parameter
	 * @param  method         Description of the Parameter
	 * @return                Description of the Return Value
	 * @exception  Exception  Description of the Exception
	 */
	protected Element makeUser( WebSession s, String user, String method ) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement( new P().addElement( "Welcome, " + user ) );
		ec.addElement( new P().addElement( "You have been authenticated with " + method ) );

		return ( ec );
	}


	protected Element makeLogin( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement( new H1().addElement( "Sign In " ));
		Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 0 ).setWidth("90%").setAlign("center");

		if ( s.isColor() )
		{
			t.setBorder( 1 );
		}
		
		TR tr = new TR();
		tr.addElement( new TH().addElement("Please sign in to your account.  See the OWASP admin if you do not have an account.")
				.setColSpan(2).setAlign("left"));
		t.addElement( tr );

		tr = new TR();
		tr.addElement( new TD().addElement("*Required Fields").setWidth("30%"));
		t.addElement( tr );
		
		tr = new TR();
		tr.addElement( new TD().addElement("&nbsp;").setColSpan(2));
		t.addElement( tr );
		
		TR row1 = new TR();
		TR row2 = new TR();
		row1.addElement( new TD( new B( new StringElement( "*User Name: " ) ) ));
		row2.addElement( new TD( new B(new StringElement( "*Password: " ) ) ));

		Input input1 = new Input( Input.TEXT, USERNAME, "" );
		Input input2 = new Input( Input.PASSWORD, PASSWORD, "" );
		row1.addElement( new TD( input1 ) );
		row2.addElement( new TD( input2 ) );
		t.addElement( row1 );
		t.addElement( row2 );

		Element b = ECSFactory.makeButton( "Login" );
		t.addElement( new TR( new TD( b ) ) );
		ec.addElement( t );

		return ( ec );
	}

	/**
	 *  Gets the hints attribute of the CluesScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "You can view the HTML source by selecting 'view source' in the browser menu." );
		hints.add( "There are lots of clues in the HTML" );
		hints.add( "Search for the word HIDDEN, look at URLs, look for comments." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the HtmlClues object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "Below is an example of a forms based authentication form.  Look for clues to help you log in.";

		return ( instructions );
	}




	private final static Integer DEFAULT_RANKING = new Integer(30);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the category attribute of the FailOpenAuthentication object
	 *
	 * @return    The category value
	 */	
	protected Category getDefaultCategory()
	{
		return AbstractLesson.CODE_QUALITY;
	}

	/**
	 *  Gets the title attribute of the CluesScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Discover Clues in the HTML" );
	}
}

