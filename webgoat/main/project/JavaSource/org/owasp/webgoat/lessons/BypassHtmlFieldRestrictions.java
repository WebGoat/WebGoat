
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
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
 * @author Chuck Willis <a href="http://www.securityfoundry.com">Chuck's web
 *         site</a> 
 * @created October 29, 2009
 */
public class BypassHtmlFieldRestrictions extends SequentialLessonAdapter
{
	public final static A MANDIANT_LOGO = new A().setHref("http://www.mandiant.com").addElement(new IMG("images/logos/mandiant.png").setAlt("MANDIANT").setBorder(0).setHspace(0).setVspace(0));
	
	private final static String USERID = "userid";

	private String userid;

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
		
		try {
			boolean failed = false;

			// select element
			ec.addElement(new Div().addElement(new StringElement("Select field with two possible values:")));
			
			String[] allowedSelect = {"foo", "bar"};
	
			ec.addElement(new org.apache.ecs.html.Select("select", allowedSelect));
			
			// radio button element
			ec.addElement(new P());
			ec.addElement(new Div().addElement(new StringElement("Radio button with two possible values:")));
			
			
			Input radiofoo = new Input("radio", "radio", "foo");
			radiofoo.setChecked(true);
			ec.addElement(radiofoo);
			ec.addElement(new StringElement("foo"));
			ec.addElement(new BR());
			ec.addElement(new Input("radio", "radio", "bar"));
			ec.addElement(new StringElement("bar"));
			
			// checkbox
			ec.addElement(new P());
			ec.addElement(new Div().addElement(new StringElement("Checkbox:")));
			Input checkbox = new Input("checkbox", "checkbox");
			checkbox.setChecked(true);
			ec.addElement(checkbox);
			ec.addElement(new StringElement("checkbox"));
			
			// create shortinput
			ec.addElement(new P());
			ec.addElement(new Div().addElement(new StringElement("Input field restricted to 5 characters:")));
			Input shortinput = new Input(Input.TEXT, "shortinput", "12345");
			shortinput.setMaxlength(5);
			ec.addElement(shortinput);

			ec.addElement(new P());
			ec.addElement(new Div().addElement(new StringElement("Disabled input field:")));
			String defaultdisabledinputtext = "disabled";
			Input disabledinput = new Input(Input.TEXT, "disabledinput", defaultdisabledinputtext);
			disabledinput.setDisabled(true);
			ec.addElement(disabledinput);
			ec.addElement(new BR());
	
			// Submit Button
			ec.addElement(new P());
			ec.addElement(new Div().addElement(new StringElement("Submit button:")));
			String submittext = "Submit";
			Element b = ECSFactory.makeButton(submittext);
			ec.addElement(b);
			
			//  Now check inputs that were submitted (if any)
			
			// check select field
			String submittedselect = s.getParser().getRawParameter("select");
			if(submittedselect.equals("foo")) failed = true;
			if(submittedselect.equals("bar")) failed = true;
			
			// check radio buttons
			String submittedradio = s.getParser().getRawParameter("radio");
			if(submittedselect.equals("foo")) failed = true;
			if(submittedselect.equals("bar")) failed = true;
			
			// check checkbox (note - if the box is not checked, this will throw an exception, but that
			// is okay)
			if(s.getParser().getRawParameter("checkbox").equals("on")) failed = true;
			
			// check shortinput
			if(s.getParser().getRawParameter("shortinput").length() < 6) failed = true;
			
			// check disabledinput (note - if the field was not re-enabled, this will throw an exception, but that
			// is okay)
			if(s.getParser().getRawParameter("disabledinput").equals(defaultdisabledinputtext)) failed = true;
			
			// check submitbutton
			if(s.getParser().getRawParameter("SUBMIT").equals(submittext)) failed = true;
			
			
			// if we didn't fail, we succeeded!
			if(failed != true) {
				makeSuccess(s);
			}
			
		} catch(ParameterNotFoundException e) {
			//s.setMessage("Error, required parameter not found");
			e.printStackTrace();
		}
		
		return (ec);
	}

	/**
	 * Gets the category attribute of the object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{
		return Category.PARAMETER_TAMPERING;
	}

    /**
     * Gets the credits attribute of the AbstractLesson object
     * 
     * @return The credits value
     */
    public Element getCredits()
    {
    	return super.getCustomCredits("Created by Chuck Willis&nbsp;", MANDIANT_LOGO);
    }
	
	/**
	 * Gets the hints attribute of the DatabaseFieldScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		
		hints.add(WebGoatI18N.get("BypassHtmlFieldRestrictionsHint1"));
		hints.add(WebGoatI18N.get("BypassHtmlFieldRestrictionsHint2"));
		hints.add(WebGoatI18N.get("BypassHtmlFieldRestrictionsHint3"));

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(10);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the DatabaseFieldScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Bypass HTML Field Restrictions");
	}

    /**
     * Gets the instructions attribute of the SqlInjection object
     *  
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
	String instructions = "The form below uses HTML form field restrictions. " + 
		" In order to pass this lesson, submit the form with each field containing an unallowed value. "
	+ "<b>You must submit invalid values for all six fields in one form submission.</b>";

	return (instructions);
    }
	
	/**
	 * Constructor for the DatabaseFieldScreen object
	 * 
	 * @param s
	 *            Description of the Parameter
	 */
	public void handleRequest(WebSession s)
	{
		try
		{
			super.handleRequest(s);
		} catch (Exception e)
		{
			// System.out.println("Exception caught: " + e);
			e.printStackTrace(System.out);
		}
	}

}
