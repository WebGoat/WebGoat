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
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.*;

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class WeakAuthenticationCookie extends LessonAdapter
{

    /**
     *  Description of the Field
     */
    protected final static String AUTHCOOKIE = "AuthCookie";

    /**
     *  Description of the Field
     */
    protected final static String LOGOUT = "WACLogout";

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
     * @param  s              Description of the Parameter
     * @return                Description of the Return Value
     * @exception  Exception  Description of the Exception
     */
    protected String checkCookie(WebSession s) throws Exception
    {
	String cookie = getCookie(s);

	if (cookie != null)
	{
	    if (cookie.equals(encode("webgoat12345")))
	    {
		return ("webgoat");
	    }

	    if (cookie.equals(encode("aspect12345")))
	    {
		return ("aspect");
	    }

	    if (cookie.equals(encode("alice12345")))
	    {
		makeSuccess(s);
		return ("alice");
	    }
	    else
	    {
		s.setMessage("Invalid cookie");
		s.eatCookies();
	    }
	}

	return (null);
    }


    /**
     *  Description of the Method
     *
     * @param  s              Description of the Parameter
     * @return                Description of the Return Value
     * @exception  Exception  Description of the Exception
     */
    protected String checkParams(WebSession s) throws Exception
    {
	String username = s.getParser().getStringParameter(USERNAME, "");
	String password = s.getParser().getStringParameter(PASSWORD, "");

	if ((username.length() > 0) && (password.length() > 0))
	{
	    String loginID = "";

	    if (username.equals("webgoat") && password.equals("webgoat"))
	    {
		loginID = encode("webgoat12345");
	    }
	    else if (username.equals("aspect") && password.equals("aspect"))
	    {
		loginID = encode("aspect12345");
	    }

	    if (loginID != "")
	    {
		Cookie newCookie = new Cookie(AUTHCOOKIE, loginID);
		s.setMessage("Your identity has been remembered");
		s.getResponse().addCookie(newCookie);

		return (username);
	    }
	    else
	    {
		s.setMessage("Invalid username and password entered.");
	    }
	}

	return (null);
    }


    /**
     *  Description of the Method
     *
     * @param  s  Description of the Parameter
     * @return    Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
	boolean logout = s.getParser().getBooleanParameter(LOGOUT, false);

	if (logout)
	{
	    s.setMessage("Goodbye!  Your password has been forgotten");
	    s.eatCookies();

	    return (makeLogin(s));
	}

	try
	{
	    String user = checkCookie(s);

	    if ((user != null) && (user.length() > 0))
	    {
		return (makeUser(s, user, "COOKIE"));
	    }

	    user = checkParams(s);

	    if ((user != null) && (user.length() > 0))
	    {
		return (makeUser(s, user, "PARAMETERS"));
	    }
	}
	catch (Exception e)
	{
	    s.setMessage("Error generating " + this.getClass().getName());
	    e.printStackTrace();
	}

	return (makeLogin(s));
    }


    /**
     *  Description of the Method
     *
     * @param  value  Description of the Parameter
     * @return        Description of the Return Value
     */
    private String encode(String value)
    {
	//<START_OMIT_SOURCE>
	StringBuffer encoded = new StringBuffer();

	for (int i = 0; i < value.length(); i++)
	{
	    encoded.append(String.valueOf((char) (value.charAt(i) + 1)));
	}

	return encoded.reverse().toString();
	//<END_OMIT_SOURCE>
    }


    /**
     *  Gets the category attribute of the WeakAuthenticationCookie object
     *
     * @return    The category value
     */
    protected Category getDefaultCategory()
    {
	return AbstractLesson.A3;
    }


    /**
     *  Gets the cookie attribute of the CookieScreen object
     *
     * @param  s  Description of the Parameter
     * @return    The cookie value
     */
    protected String getCookie(WebSession s)
    {
	Cookie[] cookies = s.getRequest().getCookies();

	for (int i = 0; i < cookies.length; i++)
	{
	    if (cookies[i].getName().equalsIgnoreCase(AUTHCOOKIE))
	    {
		return (cookies[i].getValue());
	    }
	}

	return (null);
    }


    /**
     *  Gets the hints attribute of the CookieScreen object
     *
     * @return    The hints value
     */
    protected List getHints()
    {
	List<String> hints = new ArrayList<String>();
	hints
		.add("The server skips authentication if you send the right cookie.");
	hints
		.add("Is the AuthCookie value guessable knowing the username and password?");
	hints
		.add("Add 'AuthCookie=********;' to the Cookie: header using <A href=\"http://www.owasp.org/development/webscarab\">WebScarab</A>.");

	return hints;
    }


    /**
     *  Gets the instructions attribute of the WeakAuthenticationCookie object
     *
     * @return    The instructions value
     */
    public String getInstructions(WebSession s)
    {
	String instructions = "Login using the webgoat/webgoat account to see what happens. You may also try aspect/aspect. When you understand the authentication cookie, try changing your identity to alice.";

	return (instructions);
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
    public String getTitle()
    {
	return ("How to Spoof an Authentication Cookie");
    }


    /**
     *  Description of the Method
     *
     * @param  s  Description of the Parameter
     * @return    Description of the Return Value
     */
    protected Element makeLogin(WebSession s)
    {
	ElementContainer ec = new ElementContainer();

	ec.addElement(new H1().addElement("Sign In "));
	Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0)
		.setWidth("90%").setAlign("center");

	if (s.isColor())
	{
	    t.setBorder(1);
	}

	TR tr = new TR();
	tr
		.addElement(new TH()
			.addElement(
				"Please sign in to your account.  See the OWASP admin if you do not have an account.")
			.setColSpan(2).setAlign("left"));
	t.addElement(tr);

	tr = new TR();
	tr.addElement(new TD().addElement("*Required Fields").setWidth("30%"));
	t.addElement(tr);

	tr = new TR();
	tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
	t.addElement(tr);

	TR row1 = new TR();
	TR row2 = new TR();
	row1.addElement(new TD(new B(new StringElement("*User Name: "))));
	row2.addElement(new TD(new B(new StringElement("*Password: "))));

	Input input1 = new Input(Input.TEXT, USERNAME, "");
	Input input2 = new Input(Input.PASSWORD, PASSWORD, "");
	row1.addElement(new TD(input1));
	row2.addElement(new TD(input2));
	t.addElement(row1);
	t.addElement(row2);

	Element b = ECSFactory.makeButton("Login");
	t.addElement(new TR(new TD(b)));
	ec.addElement(t);

	return (ec);
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
    protected Element makeUser(WebSession s, String user, String method)
	    throws Exception
    {
	ElementContainer ec = new ElementContainer();
	ec.addElement(new P().addElement("Welcome, " + user));
	ec.addElement(new P().addElement("You have been authenticated with "
		+ method));
	ec.addElement(new P().addElement(ECSFactory.makeLink("Logout", LOGOUT,
		true)));
	ec.addElement(new P()
		.addElement(ECSFactory.makeLink("Refresh", "", "")));

	return (ec);
    }
}
