
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;
import org.owasp.webgoat.session.*;
import org.owasp.webgoat.util.HtmlEncoder;
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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */
public class StoredXss extends LessonAdapter
{
	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	private final static String MESSAGE = "message";

	private final static int MESSAGE_COL = 3;

	private final static String NUMBER = "Num";

	private final static int NUM_COL = 1;

	private final static String STANDARD_QUERY = "SELECT * FROM messages";

	private final static String TITLE = "title";

	private final static int TITLE_COL = 2;

	private static int count = 1;

	private final static int USER_COL = 4; // Added by Chuck Willis - used to show user who posted

	// message

	/**
	 * Adds a feature to the Message attribute of the MessageBoardScreen object
	 * 
	 * @param s
	 *            The feature to be added to the Message attribute
	 */
	protected void addMessage(WebSession s)
	{
		try
		{
			String title = HtmlEncoder.encode(s.getParser().getRawParameter(TITLE, ""));
			String message = s.getParser().getRawParameter(MESSAGE, "");

			Connection connection = DatabaseUtilities.getConnection(s);

			String query = "INSERT INTO messages VALUES (?, ?, ?, ?, ? )";

			PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																		ResultSet.CONCUR_READ_ONLY);
			statement.setInt(1, count++);
			statement.setString(2, title);
			statement.setString(3, message);
			statement.setString(4, s.getUserName());
			statement.setString(5, this.getClass().getName());
			statement.execute();
		} catch (Exception e)
		{
			// ignore the empty resultset on the insert. There are a few more SQL Injection errors
			// that could be trapped here but we will let them try. One error would be something
			// like "Characters found after end of SQL statement."
			if (e.getMessage().indexOf("No ResultSet was produced") == -1)
			{
				s.setMessage(WebGoatI18N.get("CouldNotAddMessage"));
			}
			e.printStackTrace();
		}
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
		addMessage(s);

		ElementContainer ec = new ElementContainer();
		ec.addElement(makeInput(s));
		ec.addElement(new HR());
		ec.addElement(makeCurrent(s));
		ec.addElement(new HR());
		ec.addElement(makeList(s));

		return (ec);
	}

	/**
	 * Gets the category attribute of the StoredXss object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{
		return Category.XSS;
	}

	/**
	 * Gets the hints attribute of the MessageBoardScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add(WebGoatI18N.get("StoredXssHint1"));
		hints.add(WebGoatI18N.get("StoredXssHint1"));
		hints.add(WebGoatI18N.get("StoredXssHint1"));
		hints.add(WebGoatI18N.get("StoredXssHint1"));
		
		

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(100);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the MessageBoardScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Stored XSS Attacks");
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element makeCurrent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			int messageNum = s.getParser().getIntParameter(NUMBER, 0);

			Connection connection = DatabaseUtilities.getConnection(s);

			// edit by Chuck Willis - Added logic to associate similar usernames
			// The idea is that users chuck-1, chuck-2, etc will see each other's messages
			// but not anyone elses. This allows users to try out XSS to grab another user's
			// cookies, but not get confused by other users scripts

			String query = "SELECT * FROM messages WHERE user_name LIKE ? and num = ? and lesson_type = ?";
			PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																		ResultSet.CONCUR_READ_ONLY);
			statement.setString(1, getNameroot(s.getUserName()) + "%");
			statement.setInt(2, messageNum);
			statement.setString(3, this.getClass().getName());
			ResultSet results = statement.executeQuery();

			if ((results != null) && results.first())
			{
				ec.addElement(new H1(WebGoatI18N.get("MessageContentsFor")+": " + results.getString(TITLE_COL)));
				Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);
				TR row1 = new TR(new TD(new B(new StringElement(WebGoatI18N.get("Title")+":"))));
				row1.addElement(new TD(new StringElement(results.getString(TITLE_COL))));
				t.addElement(row1);

				String messageData = results.getString(MESSAGE_COL);
				TR row2 = new TR(new TD(new B(new StringElement(WebGoatI18N.get("Message")+":"))));
				row2.addElement(new TD(new StringElement(messageData)));
				t.addElement(row2);

				// Edited by Chuck Willis - added display of the user who posted the message, so
				// that
				// if users use a cross site request forgery or XSS to make another user post a
				// message,
				// they can see that the message is attributed to that user

				TR row3 = new TR(new TD(new StringElement(WebGoatI18N.get("PostedBy")+":")));
				row3.addElement(new TD(new StringElement(results.getString(USER_COL))));
				t.addElement(row3);

				ec.addElement(t);

				// Some sanity checks that the script may be correct
				if (messageData.toLowerCase().indexOf("<script>") != -1
						&& messageData.toLowerCase().indexOf("</script>") != -1
						&& messageData.toLowerCase().indexOf("alert") != -1)
				{
					makeSuccess(s);
				}

			}
			else
			{
				if (messageNum != 0)
				{
					ec.addElement(new P().addElement(WebGoatI18N.get("CouldNotFindMessage") + messageNum));
				}
			}
		} catch (Exception e)
		{
			s.setMessage(WebGoatI18N.get("ErrorGenerating") + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element makeInput(WebSession s)
	{
		Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);
		TR row1 = new TR();
		TR row2 = new TR();
		row1.addElement(new TD(new StringElement(WebGoatI18N.get("Title")+": ")));

		Input inputTitle = new Input(Input.TEXT, TITLE, "");
		row1.addElement(new TD(inputTitle));

		TD item1 = new TD();
		item1.setVAlign("TOP");
		item1.addElement(new StringElement(WebGoatI18N.get("Message")+": "));
		row2.addElement(item1);

		TD item2 = new TD();
		TextArea ta = new TextArea(MESSAGE, 5, 60);
		item2.addElement(ta);
		row2.addElement(item2);
		t.addElement(row1);
		t.addElement(row2);

		Element b = ECSFactory.makeButton(WebGoatI18N.get("Submit"));
		ElementContainer ec = new ElementContainer();
		ec.addElement(t);
		ec.addElement(new P().addElement(b));

		return (ec);
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public Element makeList(WebSession s)
	{
		Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);

		try
		{
			Connection connection = DatabaseUtilities.getConnection(s);

			// edit by Chuck Willis - Added logic to associate similar usernames
			// The idea is that users chuck-1, chuck-2, etc will see each other's messages
			// but not anyone elses. This allows users to try out XSS to grab another user's
			// cookies, but not get confused by other users scripts

			String query = "SELECT * FROM messages WHERE user_name LIKE ? and lesson_type = ?";
			PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																		ResultSet.CONCUR_READ_ONLY);
			statement.setString(1, getNameroot(s.getUserName()) + "%");
			statement.setString(2, getClass().getName());
			ResultSet results = statement.executeQuery();

			if ((results != null) && (results.first() == true))
			{
				results.beforeFirst();

				for (int i = 0; results.next(); i++)
				{
					A a = ECSFactory.makeLink(results.getString(TITLE_COL), NUMBER, results.getInt(NUM_COL));
					TD td = new TD().addElement(a);
					TR tr = new TR().addElement(td);
					t.addElement(tr);
				}
			}
		} catch (Exception e)
		{
			s.setMessage(WebGoatI18N.get("ErrorGeneratingMessageList"));
		}

		ElementContainer ec = new ElementContainer();
		ec.addElement(new H1(WebGoatI18N.get("MessageList")));
		ec.addElement(t);

		return (ec);
	}

	private static String getNameroot(String name)
	{
		String nameroot = name;
		if (nameroot.indexOf('-') != -1)
		{
			nameroot = nameroot.substring(0, nameroot.indexOf('-'));
		}
		return nameroot;
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}
