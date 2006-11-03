package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.ecs.Element;
import org.apache.ecs.StringElement;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.PRE;
import org.apache.ecs.HtmlColor;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Sherif Koussa <a href="http://www.macadamian.com">Macadamian Technologies</a>
 * @created    October 28, 2006
 */

public class LogSpoofing extends LessonAdapter {

	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	
	protected Element createContent(WebSession s) {
		
		ElementContainer ec = null;
		String inputUsername = null; 
		try{
		
		Table t = new Table( 0 ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );
		TR row1 = new TR();
		TR row2 = new TR();
		TR row3 = new TR();
		
		row1.addElement( new TD( new StringElement( "Username: " ) ) );
		Input username = new Input( Input.TEXT, USERNAME, "" );
		row1.addElement( new TD( username ) );

		row2.addElement( new TD(new StringElement( "Password: ") ) );
		Input password = new Input ( Input.PASSWORD, PASSWORD, "");
		row2.addElement( new TD (password));
		
		Element b = ECSFactory.makeButton( "Login" );
		row3.addElement( new TD (new StringElement( "&nbsp; ")));
		row3.addElement( new TD(b) ).setAlign("right");
		
		t.addElement(row1);
		t.addElement(row2);
		t.addElement(row3);
		
		ec = new ElementContainer();
		ec.addElement( t );

		inputUsername = new String( s.getParser().getRawParameter( USERNAME, "" ) );
		if ( inputUsername.length() != 0)
		{
			inputUsername = URLDecoder.decode( inputUsername, "UTF-8"); 
		}
		
		ec.addElement( new PRE(" "));
		
		Table t2 = new Table( 0 ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );
		TR row4 = new TR();
		row4.addElement( new TD(new PRE ("Login failed for username: " + inputUsername ))).setBgColor( HtmlColor.GRAY);

		t2.addElement(row4);
		
		ec.addElement( t2 );


		if ( inputUsername.length() != 0 &&
				inputUsername.toUpperCase().indexOf( System.getProperty("line.separator") + "LOGIN SUCCEEDED FOR USERNAME:") >= 0)
		{
			makeSuccess(s);
		}
		}
		catch (UnsupportedEncodingException e)
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();			
		}
		return ec;
	}

	private final static Integer DEFAULT_RANKING = new Integer(72);
	
	protected Integer getDefaultRanking() {
		return DEFAULT_RANKING;
	}

	@Override
	protected List getHints() {
		List hints = new ArrayList();
		hints.add( "Try to fool the humane eye by using new lines." );
		hints.add( "Use CR (%0d) and LF (%0a) for a new line." );
		hints.add( "Try: fooledYa%0d%0aLogin Succeeded for username: admin" );

		return hints;
	}

	@Override
	public String getTitle() {
		return "Log Spoofing";
	}

	@Override
	public Category getCategory() {
		return super.A6;
	}

}
