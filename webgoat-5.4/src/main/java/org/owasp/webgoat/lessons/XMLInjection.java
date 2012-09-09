
package org.owasp.webgoat.lessons;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
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
public class XMLInjection extends LessonAdapter
{

	private final static Integer DEFAULT_RANKING = new Integer(20);

	private final static String ACCOUNTID = "accountID";

	public static HashMap<Integer, Reward> rewardsMap = new HashMap<Integer, Reward>();

    public final static A MAC_LOGO = new A().setHref("http://www.softwaresecured.com").addElement(new IMG("images/logos/softwaresecured.gif").setAlt("Software Secured").setBorder(0).setHspace(0).setVspace(0));
    
	protected static HashMap<Integer, Reward> init()
	{
		Reward r = new Reward();

		r.setName("WebGoat t-shirt");
		r.setPoints(50);
		rewardsMap.put(1001, r);

		r = new Reward();
		r.setName("WebGoat Secure Kettle");
		r.setPoints(30);
		rewardsMap.put(1002, r);

		r = new Reward();
		r.setName("WebGoat Mug");
		r.setPoints(20);
		rewardsMap.put(1003, r);

		r = new Reward();
		r.setName("WebGoat Core Duo Laptop");
		r.setPoints(2000);
		rewardsMap.put(1004, r);

		r = new Reward();
		r.setName("WebGoat Hawaii Cruise");
		r.setPoints(3000);
		rewardsMap.put(1005, r);

		return rewardsMap;
	}

	public void handleRequest(WebSession s)
	{

		try
		{
			if (s.getParser().getRawParameter("from", "").equals("ajax"))
			{
				if (s.getParser().getRawParameter(ACCOUNTID, "").equals("836239"))
				{
					String lineSep = System.getProperty("line.separator");
					String xmlStr = "<root>" + lineSep + "<reward>WebGoat Mug 20 Pts</reward>" + lineSep
							+ "<reward>WebGoat t-shirt 50 Pts</reward>" + lineSep
							+ "<reward>WebGoat Secure Kettle 30 Pts</reward>" + lineSep + "</root>";
					s.getResponse().setContentType("text/xml");
					s.getResponse().setHeader("Cache-Control", "no-cache");
					PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
					out.print(xmlStr);
					out.flush();
					out.close();
					return;
				}
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		Form form = new Form(getFormAction(), Form.POST).setName("form").setEncType("");

		form.addElement(createContent(s));

		setContent(form);

	}

	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		boolean isDone = false;
		init();

		if (s.getParser().getRawParameter("done", "").equals("yes"))
		{
			isDone = true;
		}
		String lineSep = System.getProperty("line.separator");
		String script = "<script>" + lineSep + "function getRewards() {" + lineSep
				+ "var accountIDField = document.getElementById('" + ACCOUNTID + "');" + lineSep
				+ "if (accountIDField.value.length < 6 ) { return; }" + lineSep + "var url = '" + getLink()
				+ "&from=ajax&" + ACCOUNTID + "=' + encodeURIComponent(accountIDField.value);" + lineSep
				+ "if (typeof XMLHttpRequest != 'undefined') {" + lineSep + "req = new XMLHttpRequest();" + lineSep
				+ "} else if (window.ActiveXObject) {" + lineSep + "req = new ActiveXObject('Microsoft.XMLHTTP');"
				+ lineSep + "   }" + lineSep + "   req.open('GET', url, true);" + lineSep
				+ "   req.onreadystatechange = callback;" + lineSep + "   req.send(null);" + lineSep + "}"
				+ lineSep
				+ "function callback() {"
				+ lineSep
				+ "    if (req.readyState == 4) { "
				+ lineSep
				+ "        if (req.status == 200) { "
				+ lineSep
				+ "            var rewards = req.responseXML.getElementsByTagName('reward');"
				+ lineSep
				+ "			 var rewardsDiv = document.getElementById('rewardsDiv');"
				+ lineSep
				+ "				rewardsDiv.innerHTML = '';"
				+ lineSep
				+ "				var strHTML='';"
				+ lineSep
				+ "				strHTML = '<tr><td>&nbsp;</td><td><b>Rewards</b></td></tr>';"
				+ lineSep
				+ "			 for(var i=0; i< rewards.length; i++){"
				// + lineSep
				// + " var node = rewards.childNodes[i+1];"
				+ lineSep
				+ "				strHTML = strHTML + '<tr><td><input name=\"check' + (i+1001) +'\" type=\"checkbox\"></td><td>';"
				+ lineSep + "			    strHTML = strHTML + rewards[i].firstChild.nodeValue + '</td></tr>';" + lineSep
				+ "			 }" + lineSep + "				strHTML = '<table>' + strHTML + '</table>';" + lineSep
				+ "				strHTML = 'Your account balance is now 100 points<br><br>' + strHTML;" + lineSep
				+ "               rewardsDiv.innerHTML = strHTML;" + lineSep + "        }}}" + lineSep + "</script>"
				+ lineSep;

		if (!isDone)
		{
			ec.addElement(new StringElement(script));
		}
		ec.addElement(new BR().addElement(new H1().addElement("Welcome to WebGoat-Miles Reward Miles Program.")));
		ec.addElement(new BR());

		ec.addElement(new BR().addElement(new H3().addElement("Rewards available through the program:")));
		ec.addElement(new BR());
		Table t2 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("90%").setAlign("center");
		TR trRewards = null;

		for (int i = 1001; i < 1001 + rewardsMap.size(); i++)
		{
			trRewards = new TR();
			Reward r = (Reward) rewardsMap.get(i);
			trRewards.addElement(new TD("-" + r.getName()));
			trRewards.addElement(new TD(r.getPoints() + " Pts"));
			t2.addElement(trRewards);
		}

		ec.addElement(t2);

		ec.addElement(new BR());

		ec.addElement(new H3().addElement("Redeem your points:"));
		ec.addElement(new BR());

		Table t1 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("90%").setAlign("center");

		TR tr = new TR();

		tr.addElement(new TD("Please enter your account ID:"));

		Input input1 = new Input(Input.TEXT, ACCOUNTID, "");
		input1.addAttribute("onkeyup", "getRewards();");
		input1.addAttribute("id", ACCOUNTID);
		tr.addElement(new TD(input1));
		t1.addElement(tr);

		ec.addElement(t1);
		ec.addElement(new BR());
		ec.addElement(new BR());
		ec.addElement(new BR());

		Div div = new Div();
		div.addAttribute("name", "rewardsDiv");
		div.addAttribute("id", "rewardsDiv");
		ec.addElement(div);

		Input b = new Input();
		b.setType(Input.SUBMIT);
		b.setValue("Submit");
		b.setName("SUBMIT");
		ec.addElement(b);

		if (s.getParser().getRawParameter("SUBMIT", "") != "")
		{
			if (s.getParser().getRawParameter("check1004", "") != "")
			{
				makeSuccess(s);
			}
			else
			{
				StringBuffer shipment = new StringBuffer();
				for (int i = 1001; i < 1001 + rewardsMap.size(); i++)
				{

					if (s.getParser().getRawParameter("check" + i, "") != "")
					{
						shipment.append(((Reward) rewardsMap.get(i)).getName() + "<br>");
					}
				}
				shipment.insert(0, "<br><br><b>The following items will be shipped to your address:</b><br>");
				ec.addElement(new StringElement(shipment.toString()));
			}

		}

		return ec;
	}

	protected Element makeSuccess(WebSession s)
	{
		getLessonTracker(s).setCompleted(true);

		s.setMessage("Congratulations. You have successfully completed this lesson.");

		return (null);
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by Sherif Koussa&nbsp;", MAC_LOGO);
	}

	protected Category getDefaultCategory()
	{

		return Category.AJAX_SECURITY;
	}

	protected Integer getDefaultRanking()
	{

		return DEFAULT_RANKING;
	}

	protected List<String> getHints(WebSession s)
	{

		List<String> hints = new ArrayList<String>();
		hints.add("This page is using XMLHTTP to comunicate with the server.");
		hints.add("Try to intercept the reply and check the reply.");
		hints.add("Intercept the reply and try to inject some XML to add more rewards to yourself.");
		return hints;
	}

	public String getTitle()
	{
		return "XML Injection";
	}

	static class Reward
	{

		private String name;

		private int points;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public int getPoints()
		{
			return points;
		}

		public void setPoints(int points)
		{
			this.points = points;
		}

	}
}
