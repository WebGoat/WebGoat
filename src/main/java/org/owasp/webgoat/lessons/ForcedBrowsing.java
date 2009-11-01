
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
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
 * @author Sherif Koussa <a href="http://www.macadamian.com">Macadamian Technologies.</a>
 * @created November 02, 2006
 */
public class ForcedBrowsing extends LessonAdapter
{

	private final static String SUCCEEDED = "succeeded";

	private final static IMG MAC_LOGO = new IMG("images/logos/macadamian.gif").setAlt("Macadamian Technologies")
			.setBorder(0).setHspace(0).setVspace(0);

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
		String success = new String(s.getParser().getStringParameter(SUCCEEDED, ""));

		if (success.length() != 0 && success.equals("yes"))
		{
			ec.addElement(new BR().addElement(new H1().addElement("Welcome to WebGoat Configuration Page")));
			ec.addElement(new BR());
			Table t1 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("90%").setAlign("center");

			TR tr = new TR();
			tr.addElement(new TD(new StringElement("Set Admin Privileges for: ")));

			Input input1 = new Input(Input.TEXT, "", "");
			tr.addElement(new TD(input1));
			t1.addElement(tr);

			tr = new TR();
			tr.addElement(new TD(new StringElement("Set Admin Password:")));

			input1 = new Input(Input.PASSWORD, "", "");
			tr.addElement(new TD(input1));
			t1.addElement(tr);

			Element b = ECSFactory.makeButton("Submit");
			t1.addElement(new TR(new TD(b).setColSpan(2).setAlign("right")));
			ec.addElement(t1);

			makeSuccess(s);
		}
		else
		{
			ec
					.addElement("Can you try to force browse to the config page which should only be accessed by maintenance personnel.");
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
		return Category.INSECURE_CONFIGURATION;
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Try to guess the URL for the config page");
		hints.add("The config page is guessable and hackable");
		hints.add("Play with the URL and try to guess what you can replace 'attack' with.");
		hints.add("Try to navigate to http://localhost/WebGoat/conf");
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
		return ("Forced Browsing");
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by Sherif Koussa ", MAC_LOGO);
	}
}
