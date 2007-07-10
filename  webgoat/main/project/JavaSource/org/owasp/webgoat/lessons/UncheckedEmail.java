package org.owasp.webgoat.lessons;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 *
 * @author     Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */

public class UncheckedEmail extends LessonAdapter
{

    private final static String MESSAGE = "msg";

    private final static String TO = "to";


    /**
     *  Description of the Method
     *
     * @param  s  Description of the Parameter
     * @return    Description of the Return Value
     */

    protected Element createContent(WebSession s)
    {

	ElementContainer ec = new ElementContainer();
	try
	{
	    String to = s.getParser().getRawParameter(TO, "");

	    Table t = new Table().setCellSpacing(0).setCellPadding(2)
		    .setBorder(0).setWidth("90%").setAlign("center");

	    if (s.isColor())
	    {
		t.setBorder(1);
	    }

	    TR tr = new TR();
	    tr.addElement(new TH().addElement("Send OWASP your Comments<BR>")
		    .setAlign("left").setColSpan(3));
	    t.addElement(tr);

	    tr = new TR();
	    tr.addElement(new TD().addElement("&nbsp;").setColSpan(3));
	    t.addElement(tr);

	    tr = new TR();
	    tr.addElement(new TH().addElement(new H1("Contact Us")).setAlign(
		    "left").setWidth("55%").setVAlign("BOTTOM"));
	    //tr.addElement(new TH().addElement("&nbsp;"));
	    tr.addElement(new TH().setColSpan(2).addElement(new H3("Contact Information:"))
		    .setAlign("left").setVAlign("BOTTOM"));
	    t.addElement(tr);

	    tr = new TR();
	    tr
		    .addElement(new TD()
			    .addElement("We value your comments.  To send OWASP your questions or comments regarding the "
				    + "WebGoat tool, please enter your comments below.  The information you provide will be handled according "
				    + "to our <U>Privacy Policy</U>."));
	    //tr.addElement(new TD().addElement("&nbsp;"));
	    tr.addElement(new TD().setColSpan(2).addElement(
		    "<b>OWASP</B><BR>" + "9175 Guilford Rd <BR> Suite 300 <BR>"
			    + "Columbia, MD.  21046").setVAlign("top"));
	    t.addElement(tr);

	    tr = new TR();
	    tr.addElement(new TD().addElement("&nbsp;").setColSpan(3));
	    t.addElement(tr);

	    Input input = new Input(Input.HIDDEN, TO, "webgoat.admin@owasp.org");
	    tr = new TR();
	    tr.addElement(new TD().addElement("Questions or Comments:"));
	    tr.addElement(new TD().addElement("&nbsp;"));
	    tr.addElement(new TD().setAlign("LEFT").addElement(input));
	    t.addElement(tr);

	    tr = new TR();
	    String message = s.getParser().getRawParameter(MESSAGE, "");
	    TextArea ta = new TextArea(MESSAGE, 5, 40);
	    ta.addElement(new StringElement(convertMetachars(message)));
	    tr.addElement(new TD().setAlign("LEFT").addElement(ta));
	    tr.addElement(new TD().setAlign("LEFT").setVAlign("MIDDLE")
		    .addElement(ECSFactory.makeButton("Send!")));
	    tr.addElement(new TD().addElement("&nbsp;"));
	    t.addElement(tr);
	    ec.addElement(t);

	    // Eventually we could send the actually mail, but the point should already be made
	    //ec.addElement(exec( use java mail here + to));

	    if (to.length() > 0)
	    {
		Format formatter;
		// Get today's date
		Date date = new Date();
		formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
		String today = formatter.format(date);
		// Tue, 09 Jan 2002 22:14:02 -0500

		ec.addElement(new HR());
		ec
			.addElement(new Center()
				.addElement(new B()
					.addElement("You sent the following message to: "
						+ to)));
		ec.addElement(new BR());
		ec.addElement(new StringElement(
			"<b>Return-Path:</b> &lt;webgoat@owasp.org&gt;"));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>Delivered-To:</b> " + to));
		ec.addElement(new BR());
		ec.addElement(new StringElement(
			"<b>Received:</b> (qmail 614458 invoked by uid 239); "
				+ today));
		ec.addElement(new BR());
		ec.addElement(new StringElement("for &lt;" + to + "&gt;; "
			+ today));
		ec.addElement(new BR());
		ec.addElement(new StringElement("<b>To:</b> " + to));
		ec.addElement(new BR());
		ec
			.addElement(new StringElement(
				"<b>From:</b> Blame it on the Goat &lt;webgoat@owasp.org&gt;"));
		ec.addElement(new BR());
		ec.addElement(new StringElement(
			"<b>Subject:</b> OWASP security issues"));
		ec.addElement(new BR());
		ec.addElement(new BR());
		ec.addElement(new StringElement(message));
	    }

	    // only complete the lesson if they changed the "to" hidden field
	    if (to.length() > 0 && !"webgoat.admin@owasp.org".equals(to))
	    {
		makeSuccess(s);
	    }
	}
	catch (Exception e)
	{
	    s.setMessage("Error generating " + this.getClass().getName());
	    e.printStackTrace();
	}
	return (ec);
    }


    /**
     *  DOCUMENT ME!
     *
     * @return    DOCUMENT ME!
     */
    protected Category getDefaultCategory()
    {
	return Category.A1;
    }


    /**
     *  Gets the hints attribute of the EmailScreen object
     *
     * @return    The hints value
     */
    protected List<String> getHints(WebSession s)
    {
	List<String> hints = new ArrayList<String>();
	hints.add("Try sending an anonymous message to yourself.");
	hints
		.add("Try inserting some html or javascript code in the message field");
	hints.add("Look at the hidden fields in the HTML.");
	hints
		.add("Insert &lt;A href=\"http://www.aspectsecurity.com/webgoat.html\"&gt;Click here for Aspect&lt;/A&gt in the message field");
	hints
		.add("Insert &lt;script&gt;alert(\"Bad Stuff\");&lt;/script&gt; in the message field");
	return hints;
    }


    /**
     *  Gets the instructions attribute of the UncheckedEmail object
     *
     * @return    The instructions value
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
     *  Gets the title attribute of the EmailScreen object
     *
     * @return    The title value
     */
    public String getTitle()
    {
	return ("How to Exploit Unchecked Email");
    }
}
