package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
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
 * @author     Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */
public class BasicAuthentication extends LessonAdapter
{

	private static final String EMPTY_STRING = "";
	private static final String WEBGOAT_BASIC = "webgoat_basic";
	private static final String AUTHORIZATION = "Authorization";
	private static final String ORIGINAL_AUTH = "Original_Auth";
	private static final String ORIGINAL_USER = "Original.user";
	private static final String BASIC = "basic";
	private static final String JSESSIONID = "JSESSIONID";
	private final static String HEADER_NAME =  "header";
	private final static String HEADER_VALUE = "value";
	
	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		return super.createStagedContent(s);
	}
	
	protected Element doStage1( WebSession s ) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		String headerName = null;
		String headerValue = null;
		try
		{
			headerName = new String( s.getParser().getStringParameter( HEADER_NAME, EMPTY_STRING ) );
			headerValue = new String( s.getParser().getStringParameter( HEADER_VALUE, EMPTY_STRING ) );
			
			//<START_OMIT_SOURCE>
			// FIXME: This won;t work for CBT, we need to use the UserTracker
			//Authorization: Basic Z3Vlc3Q6Z3Vlc3Q=
			if ( headerName.equals(AUTHORIZATION) && 
				( headerValue.equals("guest:guest") || headerValue.equals("webgoat:webgoat")))
			{
				getLessonTracker(s).setStage(2);
				return doStage2( s );
			}
			else
			{
				if ( headerName.length() > 0 && !headerName.equals(AUTHORIZATION))
				{
					s.setMessage("Basic Authentication header name is incorrect.");
				}
				if( headerValue.length() > 0 && !(headerValue.equals("guest:guest") || headerValue.equals("webgoat:webgoat")))
				{
					s.setMessage("Basic Authentication header value is incorrect.");
					
				}
			}
			//<END_OMIT_SOURCE>

			Table t = new Table( 0 ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );
			if ( s.isColor() )
			{
				t.setBorder( 1 );
			}

			TR row1 = new TR();
			TR row2 = new TR();
			row1.addElement( new TD( new StringElement( "What is the name of the authentication header: " ) ) );
			row2.addElement( new TD( new StringElement( "What is the decoded value of the authentication header: " ) ) );
			
			row1.addElement( new TD( new Input( Input.TEXT, HEADER_NAME, headerName.toString() )));
			row2.addElement( new TD( new Input( Input.TEXT, HEADER_VALUE, headerValue.toString() )));
			
			t.addElement( row1 );
			t.addElement( row2 );
			
			ec.addElement( t );
			ec.addElement( new P() );
			
			Element b = ECSFactory.makeButton( "Submit" );
			ec.addElement( b );
			

		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}


		return ( ec );
	}

	protected Element doStage2( WebSession s ) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			if ( s.getRequest().isUserInRole(WEBGOAT_BASIC) )
			{
				String originalUser = getLessonTracker(s).getLessonProperties().getProperty(ORIGINAL_USER,EMPTY_STRING);
				getLessonTracker(s, originalUser).setCompleted(true);
				getLessonTracker(s, originalUser).setStage(1);
				getLessonTracker(s, originalUser).store(s, this);
				makeSuccess(s);
				s.setMessage("Close your browser and login as " + originalUser + " to get your green stars back.");
				return ec;
			}
			else
			{
				// If we are still in the ORIGINAL_USER role see if the Basic Auth header has been manipulated
				String originalAuth = getLessonTracker(s).getLessonProperties().getProperty(ORIGINAL_AUTH, EMPTY_STRING);
				String originalSessionId = getLessonTracker(s).getLessonProperties().getProperty(JSESSIONID,s.getCookie(JSESSIONID));

				// store the original user info in the BASIC properties files
				if ( originalSessionId.equals(s.getCookie(JSESSIONID)) )
			    {
					// Store the original user name in the "basic" user properties file.  We need to use
					// the original user to access the correct properties file to update status.
					// store the initial auth header
					getLessonTracker(s).getLessonProperties().setProperty(JSESSIONID, originalSessionId);
					getLessonTracker(s).getLessonProperties().setProperty(ORIGINAL_AUTH, s.getHeader(AUTHORIZATION) );
					getLessonTracker(s, BASIC).getLessonProperties().setProperty(ORIGINAL_USER, s.getUserName() );
					getLessonTracker(s, BASIC).setStage(2);
					getLessonTracker(s, BASIC).store(s, this, BASIC);
			    }

				s.setMessage("Congratulations, you have figured out the mechanics of basic authentication." );
				s.setMessage("&nbsp;&nbsp;- Now you must try to make WebGoat reauthenticate you as:  ");
				s.setMessage("&nbsp;&nbsp;&nbsp;&nbsp;- username:  basic");
				s.setMessage("&nbsp;&nbsp;&nbsp;&nbsp;- password:  basic");

				// If the auth header is different but still the original user - tell the user
				// that the original cookie was posted bak and basic auth uses the cookie before the
				// authorization token
				if ( !originalAuth.equals("") && !originalAuth.equals( s.getHeader(AUTHORIZATION) ))
				{
					ec.addElement("You're almost there!  You've modified the " + AUTHORIZATION + " header but you are " +
							"still logged in as " + s.getUserName() + ".  Look at the request after you typed in the 'basic' " +
							"user credentials and submitted the request.  Remember the order of events that occur during Basic Authentication.");
				}
				else if (!originalSessionId.equals(s.getCookie(JSESSIONID)))
				{
					ec.addElement("You're really close!  Changing the session cookie caused the server to create a new session for you.  This did not cause the server to reauthenticate you.  " +
							"When you figure out how to force the server to perform an authentication request, you have to authenticate as:<br><br>" +
							"&nbsp;&nbsp;&nbsp;&nbsp;user name: basic<br> " +
							"&nbsp;&nbsp;&nbsp;&nbsp;password: basic<br>");
				} 
				else
				{
					ec.addElement("Use the hints!  One at a time...");
				}

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
	 *  Gets the category attribute of the ForgotPassword object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{

		return AbstractLesson.A3;
	}


	/**
	 *  Gets the hints attribute of the HelloScreen object
	 *
	 * @return    The hints value
	 */
	public List getHints()
	{
		List<String> hints = new ArrayList<String>();
//		int stage = getLessonTracker(session, BASIC).getStage();

//		switch ( stage )
//		{
//				case 1:
					hints.add( "Basic authentication uses a cookie to pass the credentials. " +
					   	"Use a proxy to intercept the request.  Look at the cookies.");
					hints.add( "Basic authentication uses Base64 encoding to 'scramble' the " +
			   		   	"user's login credentials.");
					hints.add( "Basic authentication uses 'Authorization' as the cookie name to " +
					   	"store the user's credentials.");
					hints.add( "Use WebScarab -> Tools -> Transcoder to Base64 decode the " +
					   	"the value in the Authorization cookie.");
//					break;
//				case 2:
					hints.add( "Basic authentication uses a cookie to pass the credentials. " +
						"Use a proxy to intercept the request.  Look at the cookies.");
					hints.add( "Before the WebServer requests credentials from the client, the current " +
						"session is checked for validitity.");
					hints.add( "If the session is invalid the webserver will use the basic authentication credentials");
					hints.add( "If the session is invalid and the basic authentication credentials are invalid, " +
						"new credentials will be requested from the client.");
					hints.add( "Intercept the request and corrupt the JSESSIONID and the Authorization header.");
//					break;
//		}

		return hints;
	}


	private final static Integer DEFAULT_RANKING = new Integer(100);

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
		return ( "Basic Authentication" );
	}
}

