
package org.owasp.webgoat.session;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.HtmlColor;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H2;
import org.apache.ecs.html.Small;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;


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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created November 4, 2003
 */
public class ErrorScreen extends Screen
{
	/**
	 * Description of the Field
	 */
	protected Throwable error;

	/**
	 * Description of the Field
	 */
	protected String message;

	/**
	 * Constructor for the ErrorScreen object
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @param t
	 *            Description of the Parameter
	 */
	public ErrorScreen(WebSession s, Throwable t)
	{
		this.error = t;
		fixCurrentScreen(s);
		setup(s);
	}

	/**
	 * Constructor for the ErrorScreen object
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @param msg
	 *            Description of the Parameter
	 */
	public ErrorScreen(WebSession s, String msg)
	{
		this.message = msg;
		fixCurrentScreen(s);
		setup(s);
	}

	public void fixCurrentScreen(WebSession s)
	{
		// So the user can't get stuck on the error screen, reset the
		// current screen to something known
		if (s != null)
		{
			try
			{
				s.setCurrentScreen(s.getCourse().getFirstLesson().getScreenId());
			} catch (Throwable t)
			{
				s.setCurrentScreen(WebSession.WELCOME);
			}
		}
	}

	public void setup(WebSession s)
	{
		// call createContent first so messages will go somewhere

		Form form = new Form("attack", Form.POST).setName("form").setEncType("");

		form.addElement(wrapForm(s));

		TD lowerright = new TD().setHeight("100%").setVAlign("top").setAlign("left").addElement(form);
		TR row = new TR().addElement(lowerright);
		Table layout = new Table().setBgColor(HtmlColor.WHITE).setCellSpacing(0).setCellPadding(0).setBorder(0);

		layout.addElement(row);

		setContent(layout);
	}

	protected Element wrapForm(WebSession s)
	{
		if (s == null) { return new StringElement("Invalid Session"); }

		Table container = new Table().setWidth("100%").setCellSpacing(10).setCellPadding(0).setBorder(0);

		// CreateContent can generate error messages so you MUST call it before makeMessages()
		Element content = createContent(s);
		container.addElement(new TR().addElement(new TD().setColSpan(2).setVAlign("TOP").addElement(makeMessages(s))));
		container.addElement(new TR().addElement(new TD().setColSpan(2).addElement(content)));
		container.addElement(new TR());

		return (container);
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
		System.out.println("errorscreen createContent Error:" + this.error + " message:" + this.message);

		Element content;

		if (this.error != null)
		{
			content = createContent(this.error);
		}
		else if (this.message != null)
		{
			content = createContent(this.message);
		}
		else
		{
			content = new StringElement("An unknown error occurred.");
		}

		return content;
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element createContent(String s)
	{
		StringElement list = new StringElement(s);

		return (list);
	}

	/**
	 * Description of the Method
	 * 
	 * @param t
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element createContent(Throwable t)
	{
		StringElement list = new StringElement();
		list.addElement(new H2().addElement(new StringElement("Error Message: " + t.getMessage())));
		list.addElement(formatStackTrace(t));

		if (t instanceof ServletException)
		{
			Throwable root = ((ServletException) t).getRootCause();

			if (root != null)
			{
				list.addElement(new H2().addElement(new StringElement("Root Message: " + root.getMessage())));
				list.addElement(formatStackTrace(root));
			}
		}

		return (new Small().addElement(list));
	}

	public Element getCredits()
	{
		return new ElementContainer();
	}

	/**
	 * Description of the Method
	 * 
	 * @param t
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static Element formatStackTrace(Throwable t)
	{
		String trace = getStackTrace(t);
		StringElement list = new StringElement();
		StringTokenizer st = new StringTokenizer(trace, "\r\n\t");

		while (st.hasMoreTokens())
		{
			String line = st.nextToken();
			list.addElement(new Div(line));
		}

		return (list);
	}

	/**
	 * Gets the stackTrace attribute of the ErrorScreen class
	 * 
	 * @param t
	 *            Description of the Parameter
	 * @return The stackTrace value
	 */
	public static String getStackTrace(Throwable t)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(bytes, true);
		t.printStackTrace(writer);

		return (bytes.toString());
	}

	/**
	 * Gets the title attribute of the ErrorScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Error");
	}

	public String getRole()
	{
		return AbstractLesson.USER_ROLE;
	}
}
