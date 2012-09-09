
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.LI;
import org.apache.ecs.html.OL;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
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
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Reto Lippuner, Marcel Wirth
 * @created April 7, 2008
 */

public class PasswordStrength extends LessonAdapter
{

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
			if (s.getParser().getStringParameter("pass1", "").equals("0")
					&& s.getParser().getStringParameter("pass2", "").equals("1394")
					&& s.getParser().getStringParameter("pass3", "").equals("5")
					&& s.getParser().getStringParameter("pass4", "").equals("2")
					&& s.getParser().getStringParameter("pass5", "").equals("41"))
			{
				makeSuccess(s);
				ec.addElement(new StringElement("As a guideline not bound to a single solution."));
				ec.addElement(new BR());
				ec.addElement(new StringElement("Assuming the brute-force power of 1,000,000 hash/second: "));
				ec.addElement(new BR());
				OL ol = new OL();
				ol.addElement(new LI("123456 - 0 seconds        (dictionary based, one of top 100)"));
				ol.addElement(new LI("abzfez - up to 5 minutes  ( 26 chars on 6 positions = 26^6 seconds)"));
				ol.addElement(new LI("a9z1ez - up to 40 minutes ( 26+10 chars on 6 positions = 36^6 seconds)"));
				ol.addElement(new LI("aB8fEz - up to 16 hours   ( 26+26+10 chars on 6 positions = 62^6 seconds)"));
				ol.addElement(new LI("z8!E?7 - up to 50 days    ( 127 chars on 6 positions = 127^6 seconds)"));
				ec.addElement(ol);
			} else
			{

				ec.addElement(new StringElement("How much time you need for these passwords? "));
				ec.addElement(new BR());
				ec.addElement(new BR());
				ec.addElement(new BR());
				Table table = new Table();
				table.addAttribute("align='center'", 0);
				TR tr1 = new TR();
				TD td1 = new TD();
				TD td2 = new TD();
				Input input1 = new Input(Input.TEXT, "pass1", "");
				td1.addElement(new StringElement("Password = 123456"));
				td2.addElement(input1);
				td2.addElement(new StringElement("seconds"));
				tr1.addElement(td1);
				tr1.addElement(td2);
	
				TR tr2 = new TR();
				TD td3 = new TD();
				TD td4 = new TD();
				Input input2 = new Input(Input.TEXT, "pass2", "");
				td3.addElement(new StringElement("Password = abzfez"));
				td4.addElement(input2);
				td4.addElement(new StringElement("seconds"));
				tr2.addElement(td3);
				tr2.addElement(td4);
	
				TR tr3 = new TR();
				TD td5 = new TD();
				TD td6 = new TD();
				Input input3 = new Input(Input.TEXT, "pass3", "");
				td5.addElement(new StringElement("Password = a9z1ez"));
				td6.addElement(input3);
				td6.addElement(new StringElement("hours"));
				tr3.addElement(td5);
				tr3.addElement(td6);
	
				TR tr4 = new TR();
				TD td7 = new TD();
				TD td8 = new TD();
				Input input4 = new Input(Input.TEXT, "pass4", "");
				td7.addElement(new StringElement("Password = aB8fEz"));
				td8.addElement(input4);
				td8.addElement(new StringElement("days"));
				tr4.addElement(td7);
				tr4.addElement(td8);
	
				TR tr5 = new TR();
				TD td9 = new TD();
				TD td10 = new TD();
				Input input5 = new Input(Input.TEXT, "pass5", "");
				td9.addElement(new StringElement("Password = z8!E?7"));
				td10.addElement(input5);
				td10.addElement(new StringElement("days"));
				tr5.addElement(td9);
				tr5.addElement(td10);
				table.addElement(tr1);
				table.addElement(tr2);
				table.addElement(tr3);
				table.addElement(tr4);
				table.addElement(tr5);
				ec.addElement(table);
				ec.addElement(new BR());
				ec.addElement(new BR());
				Div div = new Div();
				div.addAttribute("align", "center");
				Element b = ECSFactory.makeButton("Go!");
				div.addElement(b);
				ec.addElement(div);
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}


		return (ec);
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Copy the passwords into the code checker.");
		return hints;
	}

	/**
	 * Gets the ranking attribute of the HelloScreen object
	 * 
	 * @return The ranking value
	 */
	private final static Integer DEFAULT_RANKING = new Integer(6);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	protected Category getDefaultCategory()
	{
		return Category.AUTHENTICATION;
	}

	public String getInstructions(WebSession s)
	{
		String instructions = "The Accounts of your Webapplication are only as save as the passwords. "
				+ "For this exercise, your job is to test several passwords on <a href=\"https://www.cnlab.ch/codecheck\" target=\"_blank\">https://www.cnlab.ch/codecheck</a>. "
				+ " You must test all 5 passwords at the same time...<br>"
				+ "<b> On your applications you should set good password requirements! </b>";
		return (instructions);
	}

	/**
	 * Gets the title attribute of the HelloScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Password Strength");
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by: Reto Lippuner, Marcel Wirth", new StringElement(""));
	}
}
