
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.WebSession;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Rogan Dawes <a href="http://dawes.za.net/rogan">Rogan Dawes</a>
 * @created March 30, 2005
 */
public class WeakSessionID extends LessonAdapter
{
    public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
            .addElement(
                        new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
                                .setVspace(0));
    /**
     * Description of the Field
     */
    protected final static String SESSIONID = "WEAKID";

    /**
     * Description of the Field
     */
    protected final static String PASSWORD = "Password";

    /**
     * Description of the Field
     */
    protected final static String USERNAME = "Username";

    protected static List<String> sessionList = new ArrayList<String>();

    protected static long seq = Math.round(Math.random() * 10240) + 10000;

    protected static long lastTime = System.currentTimeMillis();

    /**
     * Gets the credits attribute of the AbstractLesson object
     * 
     * @return The credits value
     */
    public Element getCredits()
    {
        return super.getCustomCredits("By Rogan Dawes of ", ASPECT_LOGO);
    }

    protected String newCookie(WebSession s)
    {
        long now = System.currentTimeMillis();
        seq++;
        if (seq % 29 == 0)
        {
            String target = encode(seq++, lastTime + (now - lastTime) / 2);
            sessionList.add(target);
            s.setMessage(target);
            if (sessionList.size() > 100) sessionList.remove(0);
        }
        lastTime = now;
        return encode(seq, now);
    }

    private String encode(long seq, long time)
    {
        return new String(Long.toString(seq) + "-" + Long.toString(time));
    }

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        try
        {
            String sessionid = s.getCookie(SESSIONID);
            if (sessionid != null && sessionList.indexOf(sessionid) > -1)
            {
                return makeSuccess(s);
            }
            else
            {
                return makeLogin(s);
            }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        return (null);
    }

    /**
     * Gets the category attribute of the WeakAuthenticationCookie object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.SESSION_MANAGEMENT;
    }

    /**
     * Gets the hints attribute of the CookieScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("The server skips authentication if you send the right cookie.");
        hints.add("Is the cookie value predictable? Can you see gaps where someone else has acquired a cookie?");
        hints.add("Try harder, you brute!");
        hints.add("The first part of the cookie is a sequential number, the second part is milliseconds.");
        hints.add("After the 29th try, the skipped identifier is printed to your screen. Use that to login.");
        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(90);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the CookieScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Hijack a Session");
    }

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element makeLogin(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        String weakid = s.getCookie(SESSIONID);

        if (weakid == null)
        {
            weakid = newCookie(s);
            Cookie cookie = new Cookie(SESSIONID, weakid);
            s.getResponse().addCookie(cookie);
        }

        ec.addElement(new H1().addElement("Sign In "));
        Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

        if (s.isColor())
        {
            t.setBorder(1);
        }

        String username = null;
        String password = null;

        try
        {
            username = s.getParser().getStringParameter(USERNAME);
        } catch (ParameterNotFoundException pnfe)
        {
        }
        try
        {
            password = s.getParser().getStringParameter(PASSWORD);
        } catch (ParameterNotFoundException pnfe)
        {
        }

        if (username != null || password != null)
        {
            s.setMessage("Invalid username or password.");
        }

        TR tr = new TR();
        tr.addElement(new TH().addElement("Please sign in to your account.").setColSpan(2).setAlign("left"));
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
        Input input3 = new Input(Input.HIDDEN, SESSIONID, weakid);
        row1.addElement(new TD(input1));
        row2.addElement(new TD(input2));
        t.addElement(row1);
        t.addElement(row2);
        t.addElement(input3);

        Element b = ECSFactory.makeButton("Login");
        t.addElement(new TR(new TD(b)));
        ec.addElement(t);

        return (ec);
    }
}
