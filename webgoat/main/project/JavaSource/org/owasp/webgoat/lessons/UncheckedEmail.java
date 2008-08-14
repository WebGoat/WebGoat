
package org.owasp.webgoat.lessons;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;
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
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */

public class UncheckedEmail extends LessonAdapter
{
	private final String YOUR_REAL_GMAIL_PASSWORD = "password";

	private final String YOUR_REAL_GMAIL_ID = "GMail id";

	private final static String MESSAGE = "msg";

	private final static String HIDDEN_TO = "to";
	private final static String SUBJECT = "subject";
	private final static String GMAIL_ID = "gId";
	private final static String GMAIL_PASS = "gPass";

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	private static final String emailFromAddress = "webgoat@owasp.org";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

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
		try
		{
			String to = s.getParser().getRawParameter(HIDDEN_TO, "");
			String gId = s.getParser().getRawParameter(GMAIL_ID, "");
			String gPass = s.getParser().getRawParameter(GMAIL_PASS, "");
			String message = s.getParser().getRawParameter(MESSAGE, "");
			String subject = s.getParser().getRawParameter(SUBJECT, "");

			boolean haveCredentials = !(YOUR_REAL_GMAIL_ID.equals(gId) || YOUR_REAL_GMAIL_PASSWORD.equals(gPass));

			ec.addElement(new HR());
			createGoogleCredentials(s, ec);
			ec.addElement(new HR());
			ec.addElement(new BR());
			createMailMessage(s, subject, message, ec);

			ec.addElement(new HR());
			if (to.length() > 0)
			{

				if (haveCredentials)
				{
					Message sentMessage = sendGoogleMail(to, subject, message, emailFromAddress, gId, gPass);
					formatMail(ec, sentMessage);
				}
				else
				{
					sendSimulatedMail(ec, to, subject, message);
				}
			}

			if (to.length() > 0 && "webgoat.admin@owasp.org".equals(to) && message.contains("<script"))
			{
				s.setMessage("The attack worked! Now try to attack another person than the admin.");
			}

			// only complete the lesson if they changed the "to" hidden field and they send a
			// script tag in the message
			if (to.length() > 0 && !"webgoat.admin@owasp.org".equals(to) && message.contains("<script"))
			{
				makeSuccess(s);
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		return (ec);
	}

	private void formatMail(ElementContainer ec, Message sentMessage)
	{
		try
		{
			ec.addElement(new Center().addElement(new B().addElement("You sent the following message to: "
					+ Arrays.asList(sentMessage.getAllRecipients()))));
			ec.addElement(new BR());
			ec.addElement(new StringElement("<b>MAIL FROM:</b> " + Arrays.asList(sentMessage.getReplyTo())));
			ec.addElement(new BR());
			ec.addElement(new StringElement("<b>RCPT TO:</b> " + Arrays.asList(sentMessage.getAllRecipients())));
			ec.addElement(new BR());
			ec
					.addElement(new StringElement("<b>Message-ID:</b> "
							+ Arrays.asList(sentMessage.getHeader("Message-ID"))));
			ec.addElement(new BR());
			ec.addElement(new StringElement("<b>Date:</b> " + sentMessage.getSentDate()));
			ec.addElement(new BR());
			ec.addElement(new StringElement("<b>Subject:</b> " + sentMessage.getSubject()));
			ec.addElement(new BR());
			ec.addElement(new StringElement("<b>Message:</b> "));
			ec.addElement(new BR());
			ec.addElement(new BR());
			ec.addElement(new StringElement(sentMessage.getContent().toString()));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			ec.addElement(new StringElement("Fatal error while sending message"));
			ec.addElement(new BR());
			ec.addElement(new StringElement(e.getMessage()));
		}

	}

	/**
	 * @param ec
	 * @param to
	 * @param message
	 */
	private void sendSimulatedMail(ElementContainer ec, String to, String subject, String message)
	{
		Format formatter;
		// Get today's date
		Date date = new Date();
		formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
		String today = formatter.format(date);
		// Tue, 09 Jan 2002 22:14:02 -0500

		ec.addElement(new Center().addElement(new B().addElement("You sent the following message to: " + to)));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>Return-Path:</b> &lt;webgoat@owasp.org&gt;"));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>Delivered-To:</b> " + to));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>Received:</b> (qmail 614458 invoked by uid 239); " + today));
		ec.addElement(new BR());
		ec.addElement(new StringElement("for &lt;" + to + "&gt;; " + today));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>To:</b> " + to));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>From:</b> Blame it on the Goat &lt;webgoat@owasp.org&gt;"));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>Subject:</b> " + subject));
		ec.addElement(new BR());
		ec.addElement(new BR());
		ec.addElement(new StringElement(message));
	}

	/**
	 * @param s
	 * @param ec
	 * @return
	 */
	private void createMailMessage(WebSession s, String subject, String message, ElementContainer ec)
	{
		TR tr;
		Input input;
		Table t = new Table().setCellSpacing(0).setCellPadding(1).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		tr = new TR();
		tr.addElement(new TH().addElement("Send OWASP your Comments<BR>").setAlign("left").setColSpan(3));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setColSpan(3));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TH().addElement(new H1("Contact Us")).setAlign("left").setWidth("55%").setVAlign("BOTTOM")
				.setColSpan(2));
		tr.addElement(new TH().addElement(new H3("Contact Information:")).setAlign("left").setVAlign("BOTTOM"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement(
											"We value your comments.  " + "To send OWASP your questions or comments "
													+ "regarding the WebGoat tool, please enter your "
													+ "comments below.  The information you provide will be "
													+ "handled according to our <U>Privacy Policy</U>.").setColSpan(2));
		tr.addElement(new TD().addElement(
											"<b>OWASP</B><BR>" + "9175 Guilford Rd <BR> Suite 300 <BR>"
													+ "Columbia, MD.  21046").setVAlign("top"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setColSpan(3));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("Subject:"));
		input = new Input(Input.TEXT, SUBJECT, "Comment for WebGoat");
		tr.addElement(new TD().setAlign("LEFT").addElement(input));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);

		input = new Input(Input.HIDDEN, HIDDEN_TO, "webgoat.admin@owasp.org");
		tr = new TR();
		tr.addElement(new TD().addElement("Questions or Comments:").setColSpan(2));
		tr.addElement(new TD().setAlign("LEFT").addElement(input));
		t.addElement(tr);

		tr = new TR();
		TextArea ta = new TextArea(MESSAGE, 5, 40);
		ta.addElement(new StringElement(convertMetachars(message)));
		tr.addElement(new TD().setAlign("LEFT").addElement(ta).setColSpan(2));
		tr.addElement(new TD().setAlign("LEFT").setVAlign("MIDDLE").addElement(ECSFactory.makeButton("Send!")));
		t.addElement(tr);
		ec.addElement(t);
	}

	/**
	 * @param s
	 * @param ec
	 */
	private void createGoogleCredentials(WebSession s, ElementContainer ec)
	{
		// Allow the user to configure a real email interface using gmail
		Table t1 = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");
		t1.setStyle("border-width:3px; border-style: solid;");
		if (s.isColor())
		{
			t1.setBorder(1);
		}

		TR tr = new TR();
		tr.addElement(new TH().addElement("Google Mail Configuration (Optional)").setAlign("center").setColSpan(2));
		t1.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setAlign("left").setColSpan(2));
		t1.addElement(tr);

		tr = new TR();
		tr.addElement(new TD()
				.addElement(
							"These configurations will enable WebGoat to send email on your "
									+ "behalf using your gmail account.  Leave them as the default value "
									+ "to use WebGoat's simulated mail.").setAlign("left").setColSpan(2));
		t1.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setAlign("left").setColSpan(2));
		t1.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("GMail login id:"));
		Input input = new Input(Input.TEXT, GMAIL_ID, YOUR_REAL_GMAIL_ID);
		tr.addElement(new TD().addElement(input));
		t1.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("GMail password:"));
		input = new Input(Input.PASSWORD, GMAIL_PASS, YOUR_REAL_GMAIL_PASSWORD);
		tr.addElement(new TD().addElement(input));
		t1.addElement(tr);
		ec.addElement(t1);

	}

	private Message sendGoogleMail(String recipients, String subject, String message, String from,
			final String mailAccount, final String mailPassword) throws MessagingException
	{
		boolean debug = false;

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "false");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
		{

			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(mailAccount, mailPassword);
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[1];
		// for (int i = 0; i < recipients.length; i++)
		// {
		addressTo[0] = new InternetAddress(recipients);
		// }
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);

		return msg;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected Category getDefaultCategory()
	{
		return Category.PARAMETER_TAMPERING;
	}

	/**
	 * Gets the hints attribute of the EmailScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Try sending an anonymous message to yourself.");
		hints.add("Try inserting some html or javascript code in the message field");
		hints.add("Look at the hidden fields in the HTML.");
		hints
				.add("Insert &lt;A href=\"http://code.google.com/p/webgoat/\"&gt;Click here for the WebGoat Project&lt;/A&gt in the message field");
		hints.add("Insert &lt;script&gt;alert(\"Bad Stuff\");&lt;/script&gt; in the message field");
		return hints;
	}

	/**
	 * Gets the instructions attribute of the UncheckedEmail object
	 * 
	 * @return The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "This form is an example of a customer support page.  Using the form below try to:<br>"
				+ "1) Send a malicious script to the website admin.<br>"
				+ "2) Send a malicious script to a 'friend' from OWASP.<br>";
		return (instructions);
	}

	private final static Integer DEFAULT_RANKING = new Integer(55);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the EmailScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Exploit Unchecked Email");
	}

}
