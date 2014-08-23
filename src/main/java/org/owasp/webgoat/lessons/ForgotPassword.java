
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
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
 * Source for this application is maintained at code.google.com, a repository for free software
 * projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Eric Sheridan <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created December 18, 2005
 */
public class ForgotPassword extends LessonAdapter
{

	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	private final static String USERNAME = "Username";

	private static String USERNAME_RESPONSE = "";

	private final static String COLOR = "Color";

	private static String COLOR_RESPONSE = "";

	private static int STAGE = 1;

	private final static HashMap<String, String> USERS = new HashMap<String, String>();

	private final static HashMap<String, String> COLORS = new HashMap<String, String>();

	private void populateTables()
	{
		USERS.put("admin", "2275$starBo0rn3");
		USERS.put("jeff", "(_I_)illia(V)s");
		USERS.put("dave", "\\V/ich3r$");
		USERS.put("intern", "H3yn0w");
		USERS.put("webgoat", "webgoat");

		COLORS.put("admin", "green");
		COLORS.put("jeff", "orange");
		COLORS.put("dave", "purple");
		COLORS.put("intern", "yellow");
		COLORS.put("webgoat", "red");
	}

	protected Element doStage1(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(new BR().addElement(new H1().addElement("Webgoat Password Recovery ")));
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		TR tr = new TR();
		tr.addElement(new TH()
				.addElement("Please input your username.  See the OWASP admin if you do not have an account.")
				.setColSpan(2).setAlign("left"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("*Required Fields").setWidth("30%"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
		t.addElement(tr);

		TR row1 = new TR();
		row1.addElement(new TD(new B(new StringElement("*User Name: "))));

		Input input1 = new Input(Input.TEXT, USERNAME, "");
		row1.addElement(new TD(input1));
		t.addElement(row1);

		Element b = ECSFactory.makeButton("Submit");
		t.addElement(new TR(new TD(b)));
		ec.addElement(t);

		return (ec);
	}

	protected Element doStage2(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(new H1().addElement("Webgoat Password Recovery "));
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		TR tr = new TR();
		tr.addElement(new TH().addElement("Secret Question: What is your favorite color?").setColSpan(2)
				.setAlign("left"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("*Required Fields").setWidth("30%"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
		t.addElement(tr);

		TR row1 = new TR();
		row1.addElement(new TD(new B(new StringElement("*Answer: "))));

		Input input1 = new Input(Input.TEXT, COLOR, "");
		row1.addElement(new TD(input1));
		t.addElement(row1);

		Element b = ECSFactory.makeButton("Submit");
		t.addElement(new TR(new TD(b)));
		ec.addElement(t);

		return (ec);
	}

	protected Element doStage3(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(new H1().addElement("Webgoat Password Recovery "));
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		TR tr = new TR();
		tr.addElement(new TH().addElement("For security reasons, please change your password immediately.")
				.setColSpan(2).setAlign("left"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement(new BR().addElement(new B().addElement(new StringElement("Results:"))))
				.setAlign("left"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement(new StringElement("Username: " + USERNAME_RESPONSE)));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement(new StringElement("Color: " + COLOR_RESPONSE)));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement(new StringElement("Password: " + USERS.get(USERNAME_RESPONSE).toString())));
		t.addElement(tr);

		ec.addElement(t);

		if (USERNAME_RESPONSE.equals("admin") && COLOR_RESPONSE.equals("green"))
		{
			makeSuccess(s);
		}
		else if (!USERNAME_RESPONSE.equals("webgoat") && USERS.containsKey(USERNAME_RESPONSE))
		{
			s.setMessage("Close. Now try to get the password of a privileged account.");
		}
		return ec;
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
		ElementContainer ec = new ElementContainer();
		String username = "";
		String color = "";

		color = s.getParser().getStringParameter(COLOR, "");

		if (color.length() > 0)
			STAGE = 2;
		else
			STAGE = 1;

		if (USERS.size() == 0)
		{
			populateTables();
		}

		if (STAGE == 2)
		{
			color = s.getParser().getStringParameter(COLOR, "");

			if (COLORS.get(USERNAME_RESPONSE).equals(color))
			{
				STAGE = 1;
				COLOR_RESPONSE = color;
				ec.addElement(doStage3(s));
			}
			else
			{
				s.setMessage("Incorrect response for " + USERNAME_RESPONSE + ". Please try again!");
				ec.addElement(doStage2(s));
			}
		}
		else if (STAGE == 1)
		{
			username = s.getParser().getStringParameter(USERNAME, "");

			if (USERS.containsKey(username))
			{
				STAGE = 2;
				USERNAME_RESPONSE = username;
				ec.addElement(doStage2(s));
			}
			else
			{
				if (username.length() > 0)
				{
					s.setMessage("Not a valid username. Please try again.");
				}
				ec.addElement(doStage1(s));
			}
		}
		else
		{
			ec.addElement(doStage1(s));
			STAGE = 1;
		}

		return ec;
	}

	/**
	 * Gets the category attribute of the ForgotPassword object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{

		return Category.AUTHENTICATION;
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();

		hints.add("There is no lock out policy in place, brute force your way!");
		hints.add("Try using usernames you might encounter throughout WebGoat.");
		hints.add("There are only so many possible colors, can you guess one?");
		hints.add("The administrative account is \"admin\"");

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(15);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the HelloScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Forgot Password");
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}
