
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;


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
 * @author Sherif Koussa <a href="http://www.softwaresecured.com">Software Secured</a>
 * 
 */
public class CSRF extends LessonAdapter
{
	protected static final String TRANSFER_FUNDS_PARAMETER = "transferFunds";
	protected static final String TRANSFER_FUNDS_PAGE = "main";
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
	public final static A MAC_LOGO = new A().setHref("http://www.softwaresecured.com").addElement(new IMG("images/logos/softwaresecured.gif").setAlt("Software Secured").setBorder(0).setHspace(0).setVspace(0));
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

			Connection connection = DatabaseUtilities.getConnection(getNameroot(s.getUserName()),s.getWebgoatContext());

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
			s.setMessage("Could not add message to database");
		}
	}

	@Override
	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		
		if (isTransferFunds(s)){
			ec.addElement(doTransfer(s));
		} else {
			addMessage(s);
			ec.addElement(makeInput(s));
			ec.addElement(new HR());
			ec.addElement(makeCurrent(s));
			ec.addElement(new HR());
			ec.addElement(makeList(s));
		}
		return ec;
	}

	/**
	 * if TRANSFER_FUND_PARAMETER is a parameter, then doTransfer is invoked.  doTranser presents the 
	 * web content to display the electronic transfer of funds.  An request
	 * should have a dollar amount specified.  When this page is accessed it will mark the lesson complete  
	 * 
	 * @param s
	 * @return Element will appropriate web content for a transfer of funds.
	 */
	protected Element doTransfer(WebSession s) {
		String transferFunds = HtmlEncoder.encode(s.getParser().getRawParameter(TRANSFER_FUNDS_PARAMETER, ""));
		ElementContainer ec = new ElementContainer();
		
		if (transferFunds.equalsIgnoreCase(TRANSFER_FUNDS_PAGE)){
			
			//transfer form
			ec.addElement(new H1("Electronic Transfer:"));
			String action = getLink();
			Form form = new Form(action, Form.POST);
			form.addElement( new Input(Input.text, TRANSFER_FUNDS_PARAMETER, "0"));
			//if this token is present we won't mark the lesson as completed
			form.addElement( new Input(Input.submit));
			ec.addElement(form);
			//present transfer funds form
		} else if (transferFunds.length() != 0){
			
			//transfer is confirmed
			ec.addElement(new H1("Electronic Transfer Complete"));
			ec.addElement(new StringElement("Amount Transfered: "+transferFunds));
			makeSuccess(s);
		} 
		return ec;
	}

	/**
	 * @param s current web session
	 * @return true if the page should be rendered as a Transfer of funds page or false for the normal message posting page.
	 */
	protected boolean isTransferFunds(WebSession s) {
		return s.getRequest().getParameterMap().containsKey(TRANSFER_FUNDS_PARAMETER);
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
		row1.addElement(new TD(new StringElement("Title: ")));

		Input inputTitle = new Input(Input.TEXT, TITLE, "");
		row1.addElement(new TD(inputTitle));

		TD item1 = new TD();
		item1.setVAlign("TOP");
		item1.addElement(new StringElement("Message: "));
		row2.addElement(item1);

		TD item2 = new TD();
		TextArea ta = new TextArea(MESSAGE, 12, 60);
		ta.addAttribute("wrap", "soft");
		item2.addElement(ta);
		row2.addElement(item2);
		t.addElement(row1);
		t.addElement(row2);

		Element b = ECSFactory.makeButton("Submit");
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
			Connection connection = DatabaseUtilities.getConnection(getNameroot(s.getUserName()),s.getWebgoatContext());

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
					String link = "<a href='" + getLink() + "&" + NUMBER + "=" + results.getInt(NUM_COL)
							+ "' style='cursor:hand'>" + results.getString(TITLE_COL) + "</a>";
					TD td = new TD().addElement(link);
					TR tr = new TR().addElement(td);
					t.addElement(tr);
				}
			}
		} catch (Exception e)
		{
			s.setMessage("Error while getting message list.");
		}

		ElementContainer ec = new ElementContainer();
		ec.addElement(new H1("Message List"));
		ec.addElement(t);
		String transferFunds = s.getParser().getRawParameter("transferFunds", "");
		if (transferFunds.length() != 0)
		{
			makeSuccess(s);
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
	protected Element makeCurrent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			int messageNum = s.getParser().getIntParameter(NUMBER, 0);

			Connection connection = DatabaseUtilities.getConnection(getNameroot(s.getUserName()),s.getWebgoatContext());

			String query = "SELECT * FROM messages WHERE user_name LIKE ? and num = ? and lesson_type = ?";
			PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																		ResultSet.CONCUR_READ_ONLY);
			statement.setString(1, getNameroot(s.getUserName()) + "%");
			statement.setInt(2, messageNum);
			statement.setString(3, this.getClass().getName());
			ResultSet results = statement.executeQuery();

			if ((results != null) && results.first())
			{
				ec.addElement(new H1("Message Contents For: " + results.getString(TITLE_COL)));
				Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);
				TR row1 = new TR(new TD(new B(new StringElement("Title:"))));
				row1.addElement(new TD(new StringElement(results.getString(TITLE_COL))));
				t.addElement(row1);

				String messageData = results.getString(MESSAGE_COL);
				TR row2 = new TR(new TD(new B(new StringElement("Message:"))));
				row2.addElement(new TD(new StringElement(messageData)));
				t.addElement(row2);

				TR row3 = new TR(new TD(new StringElement("Posted By:")));
				row3.addElement(new TD(new StringElement(results.getString(USER_COL))));
				t.addElement(row3);

				ec.addElement(t);

			}
			else
			{
				if (messageNum != 0)
				{
					ec.addElement(new P().addElement("Could not find message " + messageNum));
				}
			}

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	@Override
	protected Category getDefaultCategory()
	{
		return Category.XSS;
	}

	private final static Integer DEFAULT_RANKING = new Integer(121);

	@Override
	protected Integer getDefaultRanking()
	{

		return DEFAULT_RANKING;
	}

	@Override
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Enter some text and try to include an image in there.");
		hints.add("In order to make the picture almost invisible try to add width=\"1\" and height=\"1\".");
		hints.add("The format of an image in html is <pre>&lt;img src=\"[URL]\" width=\"1\" height=\"1\" /&gt;</pre>");
		hints.add("Include this URL in the message <pre>&lt;img src='" + getLink()
				+ "&transferFunds=5000' width=\"1\" height=\"1\" /&gt;</pre>");

		return hints;
	}

	/**
	 * Gets the title attribute of the MessageBoardScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Cross Site Request Forgery (CSRF)");
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
		return super.getCustomCredits("Created by Sherif Koussa&nbsp;", MAC_LOGO);
	}

}
