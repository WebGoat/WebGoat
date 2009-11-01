
package org.owasp.webgoat.lessons;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.HtmlColor;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.PRE;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.WebGoatI18N;


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
 * @author Sherif Koussa <a href="http://www.macadamian.com">Macadamian Technologies</a>
 * @created October 28, 2006
 */

public class LogSpoofing extends LessonAdapter
{

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private final static IMG MAC_LOGO = new IMG("images/logos/macadamian.gif").setAlt("Macadamian Technologies")
			.setBorder(0).setHspace(0).setVspace(0);

	protected Element createContent(WebSession s)
	{

		ElementContainer ec = null;
		String inputUsername = null;
		try
		{

			Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);
			TR row1 = new TR();
			TR row2 = new TR();
			TR row3 = new TR();

			row1.addElement(new TD(new StringElement(WebGoatI18N.get("UserName")+":")));
			Input username = new Input(Input.TEXT, USERNAME, "");
			row1.addElement(new TD(username));

			row2.addElement(new TD(new StringElement(WebGoatI18N.get("Password")+": ")));
			Input password = new Input(Input.PASSWORD, PASSWORD, "");
			row2.addElement(new TD(password));

			Element b = ECSFactory.makeButton(WebGoatI18N.get("Login"));
			row3.addElement(new TD(new StringElement("&nbsp; ")));
			row3.addElement(new TD(b)).setAlign("right");

			t.addElement(row1);
			t.addElement(row2);
			t.addElement(row3);

			ec = new ElementContainer();
			ec.addElement(t);

			inputUsername = new String(s.getParser().getRawParameter(USERNAME, ""));
			if (inputUsername.length() != 0)
			{
				inputUsername = URLDecoder.decode(inputUsername, "UTF-8");
			}

			ec.addElement(new PRE(" "));

			Table t2 = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);
			TR row4 = new TR();
			row4.addElement(new TD(new PRE(WebGoatI18N.get("LoginFailedForUserName")+": " + inputUsername))).setBgColor(HtmlColor.GRAY);

			t2.addElement(row4);

			ec.addElement(t2);

			if (inputUsername.length() != 0
					&& inputUsername.toUpperCase().indexOf(
															System.getProperty("line.separator")
																	+ WebGoatI18N.get("LoginSucceededForUserName")+":") >= 0)
			{
				makeSuccess(s);
			}
		} catch (UnsupportedEncodingException e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		return ec;
	}

	private final static Integer DEFAULT_RANKING = new Integer(72);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	@Override
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add(WebGoatI18N.get("LogSpoofingHint1"));
		hints.add(WebGoatI18N.get("LogSpoofingHint2"));
		hints.add(WebGoatI18N.get("LogSpoofingHint3"));
		hints.add(WebGoatI18N.get("LogSpoofingHint4"));
		return hints;
	}

	@Override
	public String getTitle()
	{
		return "Log Spoofing";
	}

	@Override
	protected Category getDefaultCategory()
	{
		return Category.INJECTION;
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by Sherif Koussa ", MAC_LOGO);
	}
}
