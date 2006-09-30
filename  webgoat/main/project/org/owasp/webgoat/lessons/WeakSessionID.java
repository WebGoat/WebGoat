package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.WebSession;


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Rogan Dawes <a href="http://dawes.za.net/rogan">Rogan Dawes</a>
 * @created    March 30, 2005
 */
public class WeakSessionID extends LessonAdapter {
    /**
     *  Description of the Field
     */
    protected final static String SESSIONID = "WEAKID";
    
    /**
     *  Description of the Field
     */
    protected final static String PASSWORD = "Password";
    
    /**
     *  Description of the Field
     */
    protected final static String USERNAME = "Username";
    
    protected static List<String> sessionList = new ArrayList<String>();
    protected static long seq = Math.round(Math.random() * 10240) + 10000;
    protected static long lastTime = System.currentTimeMillis();
    
    /**
     *  Gets the credits attribute of the AbstractLesson object
     *
     * @return    The credits value
     */
    public Element getCredits() {
        return new StringElement("By Rogan Dawes");
    }
    
    protected String newCookie() {
        long now = System.currentTimeMillis();
        seq ++;
        if (seq % 29 == 0) {
            String target = encode(seq++, lastTime + (now - lastTime)/2);
            sessionList.add(target);
            if (sessionList.size()>100)
                sessionList.remove(0);
        }
        lastTime = now;
        return encode(seq, now);
    }
    
    private String encode(long seq, long time) {
        return new String( Long.toString(seq) + "-" + Long.toString(time) );
    }
    
    /**
     *  Description of the Method
     *
     * @param  s  Description of the Parameter
     * @return    Description of the Return Value
     */
    protected Element createContent( WebSession s ) {
        try {
            String sessionid = s.getCookie( SESSIONID );
            if ( sessionid != null && sessionList.indexOf(sessionid) > -1) {
                return makeSuccess( s );
            }
            else {
                return makeLogin( s );
            }
        }
        catch ( Exception e ) {
            s.setMessage( "Error generating " + this.getClass().getName() );
            e.printStackTrace();
        }
        
        return ( null );
    }
    
    
    /**
     *  Gets the category attribute of the WeakAuthenticationCookie object
     *
     * @return    The category value
     */
    protected Category getDefaultCategory() {
        return AbstractLesson.A3;
    }
    
        
    
    /**
     *  Gets the hints attribute of the CookieScreen object
     *
     * @return    The hints value
     */
    protected List getHints() {
        List<String> hints = new ArrayList<String>();
        hints.add( "The server skips authentication if you send the right cookie." );
        hints.add( "Is the cookie value predictable? Can you see gaps where someone else has acquired a cookie?" );
        hints.add( "Try harder, you brute!" );
        
        return hints;
    }
    
	private final static Integer DEFAULT_RANKING = new Integer(90);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}
    
    /**
     *  Gets the title attribute of the CookieScreen object
     *
     * @return    The title value
     */
    public String getTitle() {
        return ( "How to hijack a session" );
    }
    
    
    /**
     *  Description of the Method
     *
     * @param  s  Description of the Parameter
     * @return    Description of the Return Value
     */
    protected Element makeLogin( WebSession s ) {
        ElementContainer ec = new ElementContainer();
        
        String weakid = s.getCookie(SESSIONID);
        
        if (weakid == null) {
            weakid = newCookie();
            Cookie cookie = new Cookie( SESSIONID, weakid );
            s.getResponse().addCookie(cookie);
        }
        
        ec.addElement( new H1().addElement( "Sign In " ));
        Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 0 ).setWidth("90%").setAlign("center");
        
        if ( s.isColor() ) {
            t.setBorder( 1 );
        }
        
        String username = null;
        String password = null;
        
        try {
            username = s.getParser().getStringParameter( USERNAME );
        } catch (ParameterNotFoundException pnfe) {}
        try {
            password = s.getParser().getStringParameter( PASSWORD );
        } catch (ParameterNotFoundException pnfe) {}
        
        if (username != null || password != null) {
            s.setMessage("Invalid username or password.");
        }
        
        TR tr = new TR();
        tr.addElement( new TH().addElement("Please sign in to your account.")
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
        Input input3 = new Input( Input.HIDDEN, SESSIONID, weakid );
        row1.addElement( new TD( input1 ) );
        row2.addElement( new TD( input2 ) );
        t.addElement( row1 );
        t.addElement( row2 );
        t.addElement( input3 );
        
        Element b = ECSFactory.makeButton( "Login" );
        t.addElement( new TR( new TD( b ) ) );
        ec.addElement( t );
        
        return ( ec );
    }
}

