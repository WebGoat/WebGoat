
package org.owasp.webgoat.lessons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.Cookie;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.IFrame;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.Exec;
import org.owasp.webgoat.util.ExecResults;


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
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class Challenge2Screen extends SequentialLessonAdapter
{
	private static final String USER_COOKIE = "user";

	private static final String JSP = ".jsp";

	private static final String WEBGOAT_CHALLENGE = "webgoat_challenge";

	private static final String WEBGOAT_CHALLENGE_JSP = WEBGOAT_CHALLENGE + JSP;

	private static final String PROCEED_TO_NEXT_STAGE = "Proceed to the next stage...";

	/**
	 * Description of the Field
	 */
	protected final static String CREDIT = "Credit";

	/**
	 * Description of the Field
	 */
	protected final static String PROTOCOL = "File";

	/**
	 * Description of the Field
	 */
	protected final static String MESSAGE = "Message";

	/**
	 * Description of the Field
	 */
	protected final static String PARAM = "p";

	/**
	 * Description of the Field
	 */
	protected final static String PASSWORD = "Password";

	/**
	 * Description of the Field
	 */
	protected final static String USER = "user";

	/**
	 * Description of the Field
	 */
	protected final static String USERNAME = "Username";

	private String pass = "goodbye";

	private String user = "youaretheweakestlink";

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	/**
	 * Determine the username and password
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	protected Element doStage1(WebSession s) throws Exception
	{
		setStage(s, 1);

		String username = s.getParser().getRawParameter(USERNAME, "");
		String password = s.getParser().getRawParameter(PASSWORD, "");

		if (username.equals(user) && password.equals(pass))
		{
			s.setMessage("Welcome to stage 2 -- get credit card numbers!");
			setStage(s, 2);

			return (doStage2(s));
		}

		s.setMessage("Invalid login");

		ElementContainer ec = new ElementContainer();
		ec.addElement(makeLogin(s));

		// <START_OMIT_SOURCE>
		// these are red herrings for the first stage
		Input input = new Input(Input.HIDDEN, USER, user);
		ec.addElement(input);

		Cookie newCookie = new Cookie(USER_COOKIE, Encoding.base64Encode(user));
		s.getResponse().addCookie(newCookie);
		phoneHome(s, "User: " + username + " --> " + "Pass: " + password);
		// <END_OMIT_SOURCE>

		return (ec);
	}

	// get creditcards from database

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	protected Element doStage2(WebSession s) throws Exception
	{
		// <START_OMIT_SOURCE>

		Cookie newCookie = new Cookie(USER_COOKIE, Encoding.base64Encode(user));
		s.getResponse().addCookie(newCookie);

		ElementContainer ec = new ElementContainer();
		if (s.getParser().getStringParameter(Input.SUBMIT, "").equals(PROCEED_TO_NEXT_STAGE + "(3)"))
		{
			s.setMessage("Welcome to stage 3 -- deface the site");
			setStage(s, 3);
			// Reset the defaced webpage so the lesson can start over
			resetWebPage(s);
			return doStage3(s);
		}

		Connection connection = DatabaseUtilities.getConnection(s);

		Statement statement3 = connection
				.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		// pull the USER_COOKIE from the cookies
		String user = Encoding.base64Decode(getCookie(s));
		String query = "SELECT * FROM user_data WHERE last_name = '" + user + "'";
		Vector<String> v = new Vector<String>();

		try
		{
			ResultSet results = statement3.executeQuery(query);

			while (results.next())
			{
				String type = results.getString("cc_type");
				String num = results.getString("cc_number");
				v.addElement(type + "-" + num);
			}
			if (v.size() != 13)
			{
				s.setMessage("Try to get all the credit card numbers");
			}

			ec.addElement(buildCart(s));

			Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

			ec.addElement(new BR());
			TR tr = new TR();
			tr.addElement(new TD().addElement("Please select credit card for this purchase: "));
			Element p = ECSFactory.makePulldown(CREDIT, v);
			tr.addElement(new TD().addElement(p).setAlign("right"));
			t.addElement(tr);

			tr = new TR();
			Element b = ECSFactory.makeButton("Buy Now!");
			tr.addElement(new TD().addElement(b));
			t.addElement(tr);
			ec.addElement(t);

			ec.addElement(new BR());
			Input input = new Input(Input.HIDDEN, USER, user);
			ec.addElement(input);

			// STAGE 3 BUTTON
			if (v.size() == 13)
			{
				s.setMessage("Congratulations! You stole all the credit cards, proceed to stage 3!");
				s.setMessage("  - Look in the credit card pull down to see the numbers.");
				ec.addElement(new BR());
				// TR inf = new TR();
				Center center = new Center();
				Element proceed = ECSFactory.makeButton(PROCEED_TO_NEXT_STAGE + "(3)");
				center.addElement(proceed);
				// inf.addElement(new TD().addElement(proceed).setAlign("center"));
				ec.addElement(center);
			}

		} catch (Exception e)
		{
			s.setMessage("An error occurred in the woods");
		}

		return (ec);
		// <END_OMIT_SOURCE>
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	/*
	 * (non-Javadoc)
	 * @see lessons.LessonAdapter#doStage3(session.WebSession)
	 */
	protected Element doStage3(WebSession s) throws Exception
	{
		// <START_OMIT_SOURCE>

		ElementContainer ec = new ElementContainer();
		if (s.getParser().getStringParameter(Input.SUBMIT, "").equals(PROCEED_TO_NEXT_STAGE + "(4)"))
		{
			setStage(s, 4);
			// Reset the defaced webpage so the lesson can start over
			resetWebPage(s);
			return doStage4(s);
		}

		// execute the possible attack first to determine if site is defaced.
		ElementContainer netstatResults = getNetstatResults(s);
		if (isDefaced(s))
		{
			ec.addElement(new HR());
			s.setMessage("CONGRATULATIONS - You have defaced the site!");
			Table t = new Table().setCellSpacing(0).setCellPadding(2).setWidth("90%").setAlign("center");
			if (s.isColor())
			{
				t.setBorder(1);
			}
			TR tr = new TR();
			tr.addElement(new TD().setAlign("center").addElement(ECSFactory.makeButton(PROCEED_TO_NEXT_STAGE + "(4)")));
			t.addElement(tr);
			tr = new TR();
			tr.addElement(new TD().addElement(showDefaceAttempt(s)));
			t.addElement(tr);
			ec.addElement(t);
			return ec;
		}
		else
		{
			// Setup the screen content
			try
			{
				ec.addElement(new H1("Current Network Status:"));
				ec.addElement(netstatResults);

				Table t = new Table().setCellSpacing(0).setCellPadding(2).setWidth("90%").setAlign("center");
				if (s.isColor())
				{
					t.setBorder(1);
				}
				String[] list = { "tcp", "tcpv6", "ip", "ipv6", "udp", "udpv6" };

				TR tr = new TR();
				tr.addElement(new TD().addElement(ECSFactory.makeButton("View Network")));
				tr.addElement(new TD().setWidth("35%").addElement(ECSFactory.makePulldown(PROTOCOL, list, "", 5)));
				t.addElement(tr);

				ec.addElement(t);
			} catch (Exception e)
			{
				ec.addElement(new P().addElement("Error in obtaining network status"));
			}

			ec.addElement(new HR());
			Table t = new Table().setCellSpacing(0).setCellPadding(2).setWidth("90%").setAlign("center");
			if (s.isColor())
			{
				t.setBorder(1);
			}
			TR tr = new TR();
			tr.addElement(new TD().addElement(showDefaceAttempt(s)));
			t.addElement(tr);
			ec.addElement(t);
		}
		return (ec);
		// <END_OMIT_SOURCE>
	}

	private boolean isDefaced(WebSession s)
	{
		// <START_OMIT_SOURCE>
		boolean defaced = false;
		try
		{
			// get current text and compare to the new text
			String origpath = s.getContext().getRealPath(WEBGOAT_CHALLENGE + "_" + s.getUserName() + JSP);
			String masterFilePath = s.getContext().getRealPath(WEBGOAT_CHALLENGE_JSP);
			String defacedText = getFileText(new BufferedReader(new FileReader(origpath)), false);
			String origText = getFileText(new BufferedReader(new FileReader(masterFilePath)), false);

			defaced = (!origText.equals(defacedText));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return defaced;
		// <END_OMIT_SOURCE>
	}

	private Element showDefaceAttempt(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		// show webgoat.jsp text
		ec.addElement(new H1().addElement("Original Website Text"));
		ec.addElement(new IFrame().setHeight("500").setWidth("100%").setSrc(s.getRequest().getContextPath() + "/" + WEBGOAT_CHALLENGE_JSP));
		ec.addElement(new HR());
		ec.addElement(new H1().addElement("Defaced Website Text"));
		ec.addElement(new IFrame().setHeight("500").setWidth("100%").setSrc(
				s.getRequest().getContextPath() + "/" + WEBGOAT_CHALLENGE + "_"
																					+ s.getUserName() + JSP));
		ec.addElement(new HR());

		return ec;
	}

	private void resetWebPage(WebSession s)
	{
		try
		{
			// get current text and compare to the new text
			String defacedpath = s.getContext().getRealPath(WEBGOAT_CHALLENGE + "_" + s.getUserName() + JSP);
			String masterFilePath = s.getContext().getRealPath(WEBGOAT_CHALLENGE_JSP);

			// replace the defaced text with the original
			File usersFile = new File(defacedpath);
			FileWriter fw = new FileWriter(usersFile);
			fw.write(getFileText(new BufferedReader(new FileReader(masterFilePath)), false));
			fw.close();
			// System.out.println("webgoat_guest replaced: " + getFileText( new
			// BufferedReader( new FileReader( defacedpath ) ), false ) );
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected Category getDefaultCategory()
	{
		return Category.CHALLENGE;
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	protected Element doStage4(WebSession s) throws Exception
	{
		makeSuccess(s);
		ElementContainer ec = new ElementContainer();
		ec.addElement(new H1().addElement("Thanks for coming!"));
		ec.addElement(new BR());
		ec.addElement(new H1()
				.addElement("Please remember that you will be caught and fired if you use these techniques for evil."));

		return (ec);
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	protected Element doStage5(WebSession s) throws Exception
	{
		// <START_OMIT_SOURCE>
		ElementContainer ec = new ElementContainer();
		return (ec);
		// <END_OMIT_SOURCE>
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	protected Element doStage6(WebSession s) throws Exception
	{
		return (new StringElement("not yet"));
	}

	/**
	 * Gets the hints attribute of the ChallengeScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		// <START_OMIT_SOURCE>

		List<String> hints = new ArrayList<String>();
		hints.add("You need to gain access to the Java source code for this lesson.");
		hints.add("Seriously, no more hints -- it's a CHALLENGE!");
		hints.add("Come on -- give it a rest!");
		if (getStage(s) != 1)
		;
		{
			hints.add("Persistance is always rewarded");
		}

		return hints;

		// <END_OMIT_SOURCE>
	}

	protected Element makeLogin(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(new H1().addElement("Sign In "));
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		TR tr = new TR();
		tr.addElement(new TH()
				.addElement("Please sign in to your account.  See the OWASP admin if you do not have an account.")
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
	 * Gets the instructions attribute of the ChallengeScreen object
	 * 
	 * @return The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "Your mission is to break the authentication scheme, "
				+ "steal all the credit cards from the database, and then deface the website. "
				+ "You will have to use many of the techniques you have learned in the other lessons. "
				+ "The main webpage to deface for this site is 'webgoat_challenge_" + s.getUserName() + ".jsp'";

		return (instructions);
	}

	/**
	 * Gets the ranking attribute of the ChallengeScreen object
	 * 
	 * @return The ranking value
	 */
	protected Integer getDefaultRanking()
	{
		return new Integer(130);
	}

	/**
	 * This is a deliberate 'backdoor' that would send user name and password back to the remote
	 * host. Obviously, sending the password back to the remote host isn't that useful but... you
	 * get the idea
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @param message
	 *            Description of the Parameter
	 */
	protected void phoneHome(WebSession s, String message)
	{
		try
		{
			InetAddress addr = InetAddress.getByName(s.getRequest().getRemoteHost());
			DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length());
			DatagramSocket sock = new DatagramSocket();
			sock.connect(addr, 1234);
			sock.send(dp);
			sock.close();
		} catch (Exception e)
		{
			System.out.println("Couldn't phone home");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the title attribute of the ChallengeScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("The CHALLENGE!");
	}

	/**
	 * Description of the Method
	 * 
	 * @param text
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected ElementContainer getNetstatResults(WebSession s)
	{
		// <START_OMIT_SOURCE>

		ElementContainer ec = new ElementContainer();

		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("80%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		String[] colWidths = new String[] { "55", "110", "260", "70" };
		TR tr = new TR();
		tr.addElement(new TH().addElement("Protocol").setWidth(colWidths[0]));
		tr.addElement(new TH().addElement("Local Address").setWidth(colWidths[1]));
		tr.addElement(new TH().addElement("Foreign Address").setWidth(colWidths[2]));
		tr.addElement(new TH().addElement("State").setWidth(colWidths[3]));
		t.addElement(tr);

		String protocol = s.getParser().getRawParameter(PROTOCOL, "tcp");

		String osName = System.getProperty("os.name");
		ExecResults er = null;
		if (osName.indexOf("Windows") != -1)
		{
			String cmd = "cmd.exe /c netstat -a -p " + protocol;
			er = Exec.execSimple(cmd);
		}
		else
		{
			String[] cmd = { "/bin/sh", "-c", "netstat -a -p " + protocol };
			er = Exec.execSimple(cmd);
		}

		String results = er.getOutput();
		StringTokenizer lines = new StringTokenizer(results, "\n");
		String line = lines.nextToken();
		// System.out.println(line);
		int start = 0;
		while (start == 0 && lines.hasMoreTokens())
		{
			if ((line.indexOf("Proto") != -1))
			{
				start++;
			}
			else
			{
				line = lines.nextToken();
			}
		}
		while (start > 0 && lines.hasMoreTokens())
		{
			// in order to avoid a ill-rendered screen when the user performs
			// command injection, we will wrap the screen at 4 columns
			int columnCount = 0;
			tr = new TR();
			TD td;
			StringTokenizer tokens = new StringTokenizer(lines.nextToken(), "\t ");
			while (tokens.hasMoreTokens() && columnCount < 4)
			{
				td = new TD().setWidth(colWidths[columnCount++]);
				tr.addElement(td.addElement(tokens.nextToken()));
			}
			t.addElement(tr);
		}
		// parse the results
		ec.addElement(t);
		return (ec);
		// <END_OMIT_SOURCE>

	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element makeClues(WebSession s)
	{
		return new StringElement("Clues not Available :)");
	}

	protected Element makeHints(WebSession s)
	{
		return new StringElement("Hint: Find the hints");
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @param message
	 *            Description of the Parameter
	 */
	protected void sendMessage(Socket s, String message)
	{
		try
		{
			OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
			osw.write(message);
		} catch (Exception e)
		{
			// System.out.println("Couldn't write " + message + " to " + s);
			e.printStackTrace();
		}
	}

	protected Element buildCart(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(new HR().setWidth("90%"));
		ec.addElement(new Center().addElement(new H1().addElement("Shopping Cart ")));
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		TR tr = new TR();
		tr.addElement(new TH().addElement("Shopping Cart Items -- To Buy Now").setWidth("80%"));
		tr.addElement(new TH().addElement("Price:").setWidth("10%"));
		tr.addElement(new TH().addElement("Quantity:").setWidth("3%"));
		tr.addElement(new TH().addElement("Total").setWidth("7%"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("Sympathy Bouquet"));
		tr.addElement(new TD().addElement("59.99").setAlign("right"));
		tr.addElement(new TD().addElement(" 1 ").setAlign("right"));
		tr.addElement(new TD().addElement("59.99"));
		t.addElement(tr);

		ec.addElement(t);

		t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		ec.addElement(new BR());
		tr = new TR();
		tr.addElement(new TD().addElement("The total charged to your credit card:"));
		tr.addElement(new TD().addElement("59.99"));
		t.addElement(tr);

		ec.addElement(t);

		return (ec);
	}

	public boolean canHaveClues()
	{
		return false;
	}

	/**
	 * Gets the cookie attribute of the CookieScreen object
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return The cookie value
	 */
	protected String getCookie(WebSession s)
	{
		Cookie[] cookies = s.getRequest().getCookies();

		for (int i = 0; i < cookies.length; i++)
		{
			if (cookies[i].getName().equalsIgnoreCase(USER_COOKIE)) { return (cookies[i].getValue()); }
		}

		return (null);
	}
}
