
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.xhtml.br;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.ValidationException;
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
 * @author Yiannis Pavlosoglou <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created December 05, 2009
 */
public class OffByOne extends LessonAdapter
{
	private final static String[] price_plans = { "$1.99 - 1 hour ", "$5.99 - 12 hours", "$9.99 - 24 hours"};
	
	private final static String ROOM_NUMBER = "room_no";

	private final static String FIRST_NAME = "first_name";
	
	private final static String LAST_NAME = "last_name";	
	
	private final static String PRICE_PLAN = "price_plan";
	
	private final static IMG LOGO = new IMG("images/logos/seleucus.png").setAlt("Seleucus Ltd")
	.setBorder(0).setHspace(0).setVspace(0);
	
	/**
	 * <p>The main method for creating content, implemented
	 * from the the LessonAdapter class.</p>
	 * 
	 * <p>This particular "Off-by-One" lesson belonging in 
	 * the category of "Buffer Overflows" carries three 
	 * steps.</p>
	 * 
	 * @param s
	 *            WebSession
	 * @return Description of the Return Value
	 */
	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			if(isFirstStep(s)) 
			{
				ec.addElement(makeFirstStep(s));
			} 
			else 
			{
				if (isSecondStep(s)) 
				{
					ec.addElement(makeSecondStep(s));
				} 
				else 
				{
					ec.addElement(makeThirdStep(s));
				}
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		
		return (ec);
	}

	/**
	 * <p>Returns the Buffer Overflow category for this
	 * lesson.</p>
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{
		return Category.BUFFER_OVERFLOW;
	}

	/**
	 * <p>Returns the hints as a List of Strings
	 * for this lesson.</p>
	 * 
	 * @return The hints values
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("While registering for Internet usage, see where else your details are used during the registration process.");
		hints.add("See which fields during the registration process, allow for really long input to be submitted.");
		hints.add("Check for hidden form fields during registration");
		hints.add("Typically, web-based buffer overflows occur just above the value of 2 to the power of a number. E.g. 1024 + 1, 2048 + 1, 4096 + 1");
		hints.add("Overflow the room number field with 4096+1 characters and look for hidden fields");
		hints.add("Enter the VIP name in the first and last naem fields");
		return hints;
	}

	/**
	 * <p>Get the default ranking within the "Buffer
	 * Overflow" category.</p>
	 * 
	 * <p>Currently ranked to be the first lesson in
	 * this category.</p>
	 * 
	 * @return The value of 5 as an Integer Object
	 */
	protected Integer getDefaultRanking()
	{
		return new Integer(5);
	}

	/**
	 * <p>Gets the title attribute for this lesson.</p> 
	 * 
	 * @return "Off-by-One Overflows"
	 */
	public String getTitle()
	{
		return ("Off-by-One Overflows");
	}

	/**
	 * yada, yada...
	 */
	public Element getCredits()
	{
		return super.getCustomCredits("Created by Yiannis Pavlosoglou ", LOGO);
	}
	
	/**
	 * <p>Based on the parameters currently with values, this method
	 * returns true if we are in the first step of this lesson.</p>
	 * 
	 * @param s 
	 * @return true if we are in the first step of the lesson.
	 */
	protected boolean isFirstStep(WebSession s) 
	{
		String room = s.getParser().getRawParameter(ROOM_NUMBER, "");
		String name = s.getParser().getRawParameter(FIRST_NAME, "");
		String last = s.getParser().getRawParameter(LAST_NAME, "");
		
		return (room.isEmpty() && name.isEmpty() && last.isEmpty() );
	}
	
	/**
	 * <p>Based on the parameters currently with values, this method
	 * returns true if we are in the second step of this lesson.</p>
	 * 
	 * @param s
	 * @return true if we are in the second step of the lesson
	 */
	protected boolean isSecondStep(WebSession s)
	{
		String price = s.getParser().getRawParameter(PRICE_PLAN, "");

		return price.isEmpty();
	}
	
	/**
	 * <p>Method for constructing the first step and returning it as
	 * an Element.</p>
	 * 
	 * @param s
	 * @return The Element that is the first step.
	 */
	private Element makeFirstStep(WebSession s) 
	{
		ElementContainer ec = new ElementContainer();
		String param = "";

		// Header
		ec.addElement(new StringElement("In order to access the Internet, you need to provide us the following information:"));
		ec.addElement(new br());
		ec.addElement(new br());
		ec.addElement(new StringElement("Step 1/2"));
		ec.addElement(new br());
		ec.addElement(new br());
		
		ec.addElement(new StringElement("Ensure that your first and last names are entered exactly as they appear in the hotel's registration system."));
		ec.addElement(new br());
		ec.addElement(new br());

		// Table 
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}
		
		// First Name
		try {
			param  = s.getParser().getStrictAlphaParameter(FIRST_NAME, 25);
		} catch (ParameterNotFoundException e) {
			param = "";
		} catch (ValidationException e) {
			param = "";
		}
		Input input = new Input(Input.TEXT, FIRST_NAME, param);

		TR tr = new TR();
		tr.addElement(new TD().addElement("First Name: "));
		tr.addElement(new TD().addElement(input));
		tr.addElement(new TD().addElement("*"));
		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		// Last Name
		try {
			param  = s.getParser().getStrictAlphaParameter(LAST_NAME, 25);
		} catch (ParameterNotFoundException e) {
			param = "";
		} catch (ValidationException e) {
			param = "";
		}
		input = new Input(Input.TEXT, LAST_NAME, param);

		tr = new TR();
		tr.addElement(new TD().addElement("Last Name: "));
		tr.addElement(new TD().addElement(input));
		tr.addElement(new TD().addElement("*"));
		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		// Room Number
		try {
			param  = s.getParser().getStrictAlphaParameter(ROOM_NUMBER, 25);
		} catch (ParameterNotFoundException e) {
			param = "";
		} catch (ValidationException e) {
			param = "";
		}
		input = new Input(Input.TEXT, ROOM_NUMBER, param);

		tr = new TR();
		tr.addElement(new TD().addElement("Room Number: "));
		tr.addElement(new TD().addElement(input));
		tr.addElement(new TD().addElement("*"));
		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		// Submit
		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement(ECSFactory.makeButton("Submit")));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		ec.addElement(t);
		
		// Footer
		ec.addElement(new br());
		ec.addElement(new br());
		ec.addElement(new StringElement("* The above fields are required for login."));
		ec.addElement(new br());
		ec.addElement(new br());
		
		
		return ec;
	}
	
	/**
	 * <p>Method for constructing the second step and returning it as
	 * an Element.</p>
	 * 
	 * @param s
	 * @return The Element that is the second step.
	 */
	private Element makeSecondStep(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		String param = "";

		// Header
		ec.addElement(new StringElement("Please select from the following available price plans:"));
		ec.addElement(new br());
		ec.addElement(new br());
		ec.addElement(new StringElement("Step 2/2"));
		ec.addElement(new br());
		ec.addElement(new br());
		
		ec.addElement(new StringElement("Ensure that your selection matches the hours of usage, as no refunds are given for this service."));
		ec.addElement(new br());
		ec.addElement(new br());

		// Table
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		
		// First Empty Row
		TR tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		// Price Plans
		tr = new TR();
		tr.addElement(new TD().addElement("Available Price Plans:"));
		tr.addElement(new TD().addElement(ECSFactory.makePulldown(PRICE_PLAN, price_plans, price_plans[2], 1)));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		// Submit
		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement(ECSFactory.makeButton("Accept Terms")));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		ec.addElement(t);
		ec.addElement("\r\n");

		// Hidden Form Fields
		param = s.getParser().getStringParameter(LAST_NAME, "");
		Input input = new Input(Input.HIDDEN, LAST_NAME, param);
		ec.addElement(input);
		ec.addElement("\r\n");

		param = s.getParser().getStringParameter(FIRST_NAME, "");
		input = new Input(Input.HIDDEN, FIRST_NAME, param);
		ec.addElement(input);
		ec.addElement("\r\n");

		param = s.getParser().getStringParameter(ROOM_NUMBER, "");
		input = new Input(Input.HIDDEN, ROOM_NUMBER, param);
		ec.addElement(input);
		ec.addElement("\r\n");

		
		// Footer
		ec.addElement(new br());
		ec.addElement(new br());
		ec.addElement(new StringElement("By Clicking on the above you accept the terms and conditions."));
		ec.addElement(new br());
		ec.addElement(new br());
		
		
		return ec;
	}
	
	/**
	 * <p>Method for constructing the third step and returning it as
	 * an Element.</p>
	 * 
	 * @param s
	 * @return The Element that is the third step.
	 */
	private Element makeThirdStep(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		String param1 = "";
		String param2 = "";
		String param3 = "";

		// Header
		ec.addElement(new StringElement("You have now completed the 2 step process and have access to the Internet"));
		ec.addElement(new br());
		ec.addElement(new br());
		ec.addElement(new StringElement("Process complete"));
		ec.addElement(new br());
		ec.addElement(new br());
		
		ec.addElement(new StringElement("Your connection will remain active for the time allocated for starting now."));
		ec.addElement(new br());
		ec.addElement(new br());

		// Table
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		
		// First Empty Row
		TR tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		// Price Plans
		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		// Submit
		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		tr.addElement(new TD().addElement("&nbsp;"));
		t.addElement(tr);
		
		ec.addElement(t);
		ec.addElement("\r\n");

		// Hidden Form Fields
		param1 = s.getParser().getStringParameter(LAST_NAME, "");
		Input input = new Input(Input.HIDDEN, "a", param1);
		ec.addElement(input);
		ec.addElement("\r\n");

		param2 = s.getParser().getStringParameter(FIRST_NAME, "");
		input = new Input(Input.HIDDEN, "b", param2);
		ec.addElement(input);
		ec.addElement("\r\n");

		param3 = s.getParser().getStringParameter(ROOM_NUMBER, "");
		input = new Input(Input.HIDDEN, "c", param3);
		ec.addElement(input);
		ec.addElement("\r\n");

		// And finally the check...
		if(param3.length() > 4096)
		{
			ec.addElement(new Input(Input.hidden, "d", "Johnathan"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "e", "Ravern"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "f", "4321"));
			ec.addElement("\r\n");

			ec.addElement(new Input(Input.hidden, "g", "John"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "h", "Smith"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "i", "56"));
			ec.addElement("\r\n");

			ec.addElement(new Input(Input.hidden, "j", "Ana"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "k", "Arneta"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "l", "78"));
			ec.addElement("\r\n");
			
			ec.addElement(new Input(Input.hidden, "m", "Lewis"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "n", "Hamilton"));
			ec.addElement("\r\n");
			ec.addElement(new Input(Input.hidden, "o", "9901"));
			ec.addElement("\r\n");

			s.setMessage("To complete the lesson, restart lesson and enter VIP first/last name");

		}
		if (("Johnathan".equalsIgnoreCase(param2) || "John".equalsIgnoreCase(param2)
				|| "Ana".equalsIgnoreCase(param2) ||"Lewis".equalsIgnoreCase(param2))
				&& ("Ravern".equalsIgnoreCase(param1) || "Smith".equalsIgnoreCase(param1)
						|| "Arneta".equalsIgnoreCase(param1) ||"Hamilton".equalsIgnoreCase(param1)))
		{
			// :)
			// Allows for mixed VIP names, but that's not really the point
			makeSuccess(s);
		}
		
		// Footer
		ec.addElement(new br());
		ec.addElement(new br());
		ec.addElement(new StringElement("We would like to thank you for your payment."));
		ec.addElement(new br());
		ec.addElement(new br());
		
		return ec;
	}
		

}
