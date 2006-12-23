package org.owasp.webgoat.lessons;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
	
	private final static String LANGUAGE = "language";
	private final static String REDIRECT = "fromRedirect";
	private static String STAGE = "stage";
	
	/**
	 * Description of the Method
	 * 
	 * @param s Current WebSession
	 */
	public void handleRequest( WebSession s )
	{
		//Setting a special action to be able to submit to redirect.jsp
		Form form = new Form( "/WebGoat/lessons/General/redirect.jsp?" +
				        "Screen=" + String.valueOf(getScreenId()) +
				        "&menu=" + getDefaultCategory().getRanking().toString()
				        , Form.POST ).setName( "form" ).setEncType( "" );

		form.addElement( createContent( s ) );

        setContent(form);
	}
	
	protected Element doHTTPSplitting(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		String lang = null;
		
		try
		{
			ec.addElement(createAttackEnvironment(s));						
			lang = URLDecoder.decode(s.getParser().getRawParameter( LANGUAGE, "" ), "UTF-8") ;
		
		//Check if we are coming from the redirect page
		String fromRedirect = s.getParser().getStringParameter ( "fromRedirect" , "");		
		
		if ( lang.length() != 0 && fromRedirect.length() != 0 )
		{	
			//Split by the line separator line.separator is platform independant
			String lineSep = System.getProperty("line.separator");
			String[] arrTokens = lang.toString().toUpperCase().split(lineSep);
			
			//Check if the user ended the first request and wrote the second malacious reply
			
			if (Arrays.binarySearch(arrTokens, "CONTENT-LENGTH: 0") >= 0 &&
					Arrays.binarySearch(arrTokens, "HTTP/1.1 200 OK") >= 0 )	
			{	
				PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
				out.print(lang.substring(lang.indexOf("HTTP/1.1")));	
				out.flush();
				out.close();
				
				//we gotta set it manually here so that we don't throw an exception
				getLessonTracker(s).setCompleted(true);

				//makeSuccess( s );
				getLessonTracker(s).setStage(2);
				
				StringBuffer msg = new StringBuffer();
				
				msg.append("Good Job! ");
				msg.append("This lesson has detected your successfull attack, ");
				msg.append("time to elevate your attack to a higher level. ");
				msg.append("Try again and add Last-Modified header, intercept");
				msg.append("the reply and replace it with a 304 reply.");
				
				s.setMessage(msg.toString());
				
			}
		}
		}
		catch (Exception e)
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}
		return ( ec );	
	}
	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	protected Element doStage1( WebSession s ) throws Exception
	{
		return doHTTPSplitting( s );
	}
	
	protected Element doStage2( WebSession s ) throws Exception
	{
		return doCachePoisining( s);
	}

	protected Element createAttackEnvironment(WebSession s ) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		String  lang = null;
		
		ec.addElement( new StringElement( "Search by country : " ) );

		lang = URLDecoder.decode(s.getParser().getRawParameter( LANGUAGE, "" ), "UTF-8") ;
		
		//add the search by field
		Input input = new Input( Input.TEXT, LANGUAGE, lang.toString() );
		ec.addElement( input );

		Element b = ECSFactory.makeButton( "Search!" );
		
		ec.addElement( b );
		
		return ec;
	}
	
	protected Element doCachePoisining( WebSession s ) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		
		try 
		{
			ec.addElement("Now that you have successfully performed an HTTP Splitting, now try to poison" +
					" the victim's cache using. Type 'restart' in the input field if you wish to " +
					" to return to the HTTP Splitting lesson.<br><br>");
			if ( s.getParser().getRawParameter( LANGUAGE, "YOUR_NAME" ).equals("restart"))
			{
				getLessonTracker(s).getLessonProperties().setProperty(STAGE,"1");
				return( doHTTPSplitting(s));
			}
			
			ec.addElement(createAttackEnvironment(s));
			String lang = URLDecoder.decode(s.getParser().getRawParameter( LANGUAGE, "" ), "UTF-8") ;
			String fromRedirect = s.getParser().getStringParameter ( REDIRECT , "");
			
			if ( lang.length() != 0 && fromRedirect.length() != 0 )
			{	
				String lineSep = System.getProperty("line.separator");
				String dateStr = lang.substring(lang.indexOf("Last-Modified:") + "Last-Modified:".length(), 
						 lang.indexOf(lineSep, lang.indexOf("Last-Modified:") ));
				if (dateStr.length() != 0 )
				{
					Calendar cal = Calendar.getInstance();
					
					DateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

					if (sdf.parse(dateStr.trim()).after(cal.getTime()))
					{
						makeSuccess(s);
						getLessonTracker(s).setStage(2);
					}
				}
			}
		}
		catch (Exception ex)
		{
			ec.addElement( new P().addElement( ex.getMessage() ) );
		}
		return ec;
	}
	
	protected Category getDefaultCategory()
	{
		return AbstractLesson.GENERAL;
	}

	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "Enter a language for the system to search by." );
		hints.add( "Use CR (%0d) and LF (%0a) for a new line" );
		hints.add( "The Content-Length: 0 will tell the server that the first request is over." );
		hints.add( "A 200 OK message looks like this: HTTP/1.1 200 OK" );
		hints.add( "Try language=?foobar%0d%0aContent-Length:%200%0d%0a%0d%0aHTTP/1.1%20200%20OK%0d%0aContent-Type:%20text/html%0d%0aContent-Length:%2019%0d%0a%0d%0a<html>hahahahaha</html>" );
		return hints;
	
	}

	private final static Integer DEFAULT_RANKING = new Integer(20);

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
