package org.owasp.webgoat.lessons;
import java.util.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import org.apache.ecs.*;
import org.apache.ecs.html.*;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the 
 *  custody of the Open Web Application Security Project 
 *  (http://www.owasp.org) This software package is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you 
 *  use, modify and/or redistribute this software.
 *
 * @author     sherif@macadamian.com
 * @created    September 30, 2006
 */

public class HttpSplitting extends LessonAdapter {
	
	private final static String URL = "url";
	/**
	 * Description of the Method
	 * 
	 * @param s Description of the Parameter
	 */
	public void handleRequest( WebSession s )
	{
		// call createContent first so messages will go somewhere

		Form form = new Form( "/WebGoat/lessons/General/redirect.jsp?" +
				        "Screen=" + String.valueOf(getScreenId()) +
				        "&menu=" + getDefaultCategory().getRanking().toString()
				        , Form.POST ).setName( "form" ).setEncType( "" );

		form.addElement( createContent( s ) );

        setContent(form);
	}
	
	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		StringBuffer url = null;
		
		try
		{
			ec.addElement( new StringElement( "Search by country : " ) );

			url = new StringBuffer( s.getParser().getStringParameter( URL, "" ) );
			
			Input input = new Input( Input.TEXT, URL, url.toString() );
			ec.addElement( input );
	
			Element b = ECSFactory.makeButton( "Go!" );
			
			ec.addElement( b );
						
		}
		catch (Exception e)
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}
		
		String fromRedirect = s.getParser().getStringParameter ( "fromRedirect" , "");
		if ( url.length() != 0 && fromRedirect.length() != 0 )
		{	
			String[] arrTokens = url.toString().split(System.getProperty("line.separator"));
			if (Arrays.binarySearch(arrTokens, "Content-Length: 0") >= 0 &&
					Arrays.binarySearch(arrTokens, "HTTP/1.1 200 OK") >= 0 )	
			{	
				makeSuccess( s );
			}
		}
		return ( ec );	
	}

	public Category getCategory()
	{
		return LessonAdapter.GENERAL;
	}

	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "Enter a language for the system to search by." );
		hints.add( "Use CR (%0d) and LF (%0a) for a new line" );
		hints.add( "The Content-Length: 0 will tell the server that the first request is over." );
		hints.add( "A 200 OK message looks like this: HTTP/1.1 200 OK" );

		return hints;
		
	}

	private final static Integer DEFAULT_RANKING = new Integer(10);

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
		return ( "HTTP Splitting" );
	}

}
