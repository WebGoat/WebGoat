
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.PRE;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.DatabaseUtilities;
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
 * @author Sherif Koussa <a href="http://www.softwaresecured.com">Software Secured</a>
 */
public class BackDoors extends SequentialLessonAdapter
{

	private final static Integer DEFAULT_RANKING = new Integer(80);

	private final static String USERNAME = "username";

	private final static String SELECT_ST = "select userid, password, ssn, salary, email from employee where userid=";

	public final static A MAC_LOGO = new A().setHref("http://www.softwaresecured.com").addElement(new IMG("images/logos/softwaresecured.gif").setAlt("Software Secured").setBorder(0).setHspace(0).setVspace(0));

	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	protected Element doStage1(WebSession s) throws Exception
	{
		return concept1(s);
	}

	protected Element doStage2(WebSession s) throws Exception
	{
		return concept2(s);
	}

	private void addDBEntriesToEC(ElementContainer ec, ResultSet rs)
	{
		try
		{
			if (rs.next())
			{
				Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(1);
				TR tr = new TR();
				tr.addElement(new TH("User ID"));
				tr.addElement(new TH("Password"));
				tr.addElement(new TH("SSN"));
				tr.addElement(new TH("Salary"));
				tr.addElement(new TH("E-Mail"));
				t.addElement(tr);

				tr = new TR();
				tr.addElement(new TD(rs.getString("userid")));
				tr.addElement(new TD(rs.getString("password")));
				tr.addElement(new TD(rs.getString("ssn")));
				tr.addElement(new TD(rs.getString("salary")));
				tr.addElement(new TD(rs.getString("email")));
				t.addElement(tr);
				while (rs.next())
				{
					tr = new TR();
					tr.addElement(new TD(rs.getString("userid")));
					tr.addElement(new TD(rs.getString("password")));
					tr.addElement(new TD(rs.getString("ssn")));
					tr.addElement(new TD(rs.getString("salary")));
					tr.addElement(new TD(rs.getString("email")));
					t.addElement(tr);
				}
				ec.addElement(t);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected Element concept1(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(makeUsername(s));

		try
		{
			String userInput = s.getParser().getRawParameter(USERNAME, "");
			if (!userInput.equals(""))
			{
				userInput = SELECT_ST + userInput;
				String[] arrSQL = userInput.split(";");
				Connection conn = DatabaseUtilities.getConnection(s);
				Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
															ResultSet.CONCUR_READ_ONLY);
				if (arrSQL.length == 2)
				{
					statement.executeUpdate(arrSQL[1]);

					getLessonTracker(s).setStage(2);
					s
							.setMessage("You have succeeded in exploiting the vulnerable query and created another SQL statement. Now move to stage 2 to learn how to create a backdoor or a DB worm");
				}

				ResultSet rs = statement.executeQuery(arrSQL[0]);
				addDBEntriesToEC(ec, rs);

			}
		} catch (Exception ex)
		{
			ec.addElement(new PRE(ex.getMessage()));
		}
		return ec;
	}

	protected Element concept2(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement(makeUsername(s));

		String userInput = s.getParser().getRawParameter(USERNAME, "");

		if (!userInput.equals(""))
		{
			userInput = SELECT_ST + userInput;
			String[] arrSQL = userInput.split(";");
			Connection conn = DatabaseUtilities.getConnection(s);
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			if (arrSQL.length == 2)
			{
				if (userInput.toUpperCase().indexOf("CREATE TRIGGER") != -1)
				{
					makeSuccess(s);
				}
			}
			ResultSet rs = statement.executeQuery(arrSQL[0]);
			addDBEntriesToEC(ec, rs);

		}
		return ec;
	}

	public String getInstructions(WebSession s)
	{
		String instructions = "";

		if (!getLessonTracker(s).getCompleted())
		{
			switch (getStage(s))
			{
				case 1:
					instructions = "Stage " + getStage(s)
							+ ": Use String SQL Injection to execute more than one SQL Statement. ";
					instructions = instructions
							+ " The first stage of this lesson is to teach you how to use a vulnerable field to create two SQL ";
					instructions = instructions
							+ " statements. The first is the system's while the second is totally yours.";
					instructions = instructions
							+ " Your account ID is 101. This page allows you to see your password, ssn and salary.";
					instructions = instructions + "  Try to inject another update to update salary to something higher";
					break;
				case 2:
					instructions = "Stage " + getStage(s) + ": Use String SQL Injection to inject a backdoor. ";
					instructions = instructions
							+ " The second stage of this lesson is to teach you how to use a vulneable fields to inject the DB work or the backdoor.";
					instructions = instructions
							+ " Now try to use the same technique to inject a trigger that would act as ";
					instructions = instructions + " SQL backdoor, the syntax of a trigger is: <br>";
					instructions = instructions
							+ " CREATE TRIGGER myBackDoor BEFORE INSERT ON employee FOR EACH ROW BEGIN UPDATE employee SET email='john@hackme.com'WHERE userid = NEW.userid<br>";
					instructions = instructions
							+ " Note that nothing will actually be executed because the current underlying DB doesn't support triggers.";
					break;
			}
		}

		return instructions;
	}

	protected Element makeUsername(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		StringBuffer script = new StringBuffer();
		script.append("<STYLE TYPE=\"text/css\"> ");
		script.append(".blocklabel { margin-top: 8pt; }");
		script.append(".myClass 	{ color:red;");
		script.append(" font-weight: bold;");
		script.append("padding-left: 1px;");
		script.append("padding-right: 1px;");
		script.append("background: #DDDDDD;");
		script.append("border: thin black solid; }");
		script.append("LI	{ margin-top: 10pt; }");
		script.append("</STYLE>");
		ec.addElement(new StringElement(script.toString()));

		ec.addElement(new StringElement("User ID: "));
		Input username = new Input(Input.TEXT, "username", "");
		ec.addElement(username);

		String userInput = s.getParser().getRawParameter("username", "");

		ec.addElement(new BR());
		ec.addElement(new BR());

		String formattedInput = "<span class='myClass'>" + userInput + "</span>";
		ec.addElement(new Div(SELECT_ST + formattedInput));

		Input b = new Input();

		b.setName("Submit");
		b.setType(Input.SUBMIT);
		b.setValue("Submit");

		ec.addElement(new PRE(b));

		return ec;
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by Sherif Koussa&nbsp;", MAC_LOGO);
	}

	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Your user id is 101. Use it to see your information");
		hints.add("A semi-colon usually ends a SQL statement and starts a new one.");
		hints.add("Try this 101 or 1=1; update employee set salary=100000");
		hints.add("For stage 2, Try 101; CREATE TRIGGER myBackDoor BEFORE INSERT ON "
				+ "employee FOR EACH ROW BEGIN UPDATE employee SET email='john@hackme.com' WHERE userid = NEW.userid");
		return hints;
	}

	protected Category getDefaultCategory()
	{
		return Category.INJECTION;
	}

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	public String getTitle()
	{
		return ("Database Backdoors ");
	}
}
