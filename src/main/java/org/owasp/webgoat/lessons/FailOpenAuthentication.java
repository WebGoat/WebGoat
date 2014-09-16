
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */
public class FailOpenAuthentication extends WeakAuthenticationCookie
{

    public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
            .addElement(
                        new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
                                .setVspace(0));

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        boolean logout = s.getParser().getBooleanParameter(LOGOUT, false);

        if (logout)
        {
            s.setMessage("Goodbye!");
            s.eatCookies();

            return (makeLogin(s));
        }

        try
        {
            String username = "";
            String password = "";

            try
            {
                username = s.getParser().getRawParameter(USERNAME);
                password = s.getParser().getRawParameter(PASSWORD);

                // if credentials are bad, send the login page
                if (!"webgoat".equals(username) || !password.equals("webgoat"))
                {
                    s.setMessage("Invalid username and password entered.");

                    return (makeLogin(s));
                }
            } catch (Exception e)
            {
                // The parameter was omitted. set fail open status complete
                if (username.length() > 0 && e.getMessage().indexOf("not found") != -1)
                {
                    if ((username != null) && (username.length() > 0))
                    {
                        makeSuccess(s);
                        return (makeUser(s, username, "Fail Open Error Handling"));
                    }
                }
            }

            // Don't let the fail open pass with a blank password.
            if (password.length() == 0)
            {
                // We make sure the username was submitted to avoid telling the user an invalid
                // username/password was entered when they first enter the lesson via the side menu.
                // This also suppresses the error if they just hit the login and both fields are
                // empty.
                if (username.length() != 0)
                {
                    s.setMessage("Invalid username and password entered.");
                }

                return (makeLogin(s));

            }

            // otherwise authentication is good, show the content
            if ((username != null) && (username.length() > 0)) { return (makeUser(s, username,
                                                                                    "Parameters.  You did not exploit the fail open.")); }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
        }

        return (makeLogin(s));
    }

    /**
     * Gets the category attribute of the FailOpenAuthentication object
     * 
     * @return The category value
     */
    public Category getDefaultCategory()
    {
        return Category.ERROR_HANDLING;
    }

    /**
     * Gets the hints attribute of the AuthenticateScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("You can force errors during the authentication process.");
        hints.add("You can change length, existance, or values of authentication parameters.");
        hints
                .add("Try removing a parameter ENTIRELY with <A href=\"http://www.owasp.org/index.php/Category:OWASP_WebScarab_Project\">OWASP ZAP</A>.");

        return hints;
    }

    /**
     * Gets the instructions attribute of the FailOpenAuthentication object
     * 
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
        return "Due to an error handling problem in the authentication mechanism, it is possible to authenticate "
                + "as the 'webgoat' user without entering a password.  Try to login as the webgoat user without "
                + "specifying a password.";
    }

    private final static Integer DEFAULT_RANKING = new Integer(20);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the AuthenticateScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Fail Open Authentication Scheme");
    }

    public Element getCredits()
    {
        return super.getCustomCredits("", ASPECT_LOGO);
    }
}
