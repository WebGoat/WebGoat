
package org.owasp.webgoat.lessons;

import org.owasp.webgoat.session.WebSession;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.BR;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;


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
 * @author Sherif Koussa <a href="http://www.macadamian.com">Macadamian Technologies.</a>
 * @created December 25, 2006
 */

public class JSONInjection extends LessonAdapter
{

	private final static Integer DEFAULT_RANKING = new Integer(30);

	private final static String TRAVEL_FROM = "travelFrom";

	private final static String TRAVEL_TO = "travelTo";

	private final static IMG MAC_LOGO = new IMG("images/logos/macadamian.gif").setAlt("Macadamian Technologies")
			.setBorder(0).setHspace(0).setVspace(0);

	public void handleRequest(WebSession s)
	{

		try
		{
			if (s.getParser().getRawParameter("from", "").equals("ajax"))
			{
				String lineSep = System.getProperty("line.separator");
				String jsonStr = "{" + lineSep + "\"From\": \"Boston\"," + lineSep + "\"To\": \"Seattle\", " + lineSep
						+ "\"flights\": [" + lineSep
						+ "{\"stops\": \"0\", \"transit\" : \"N/A\", \"price\": \"$600\"}," + lineSep
						+ "{\"stops\": \"2\", \"transit\" : \"Newark,Chicago\", \"price\": \"$300\"} " + lineSep + "]"
						+ lineSep + "}";
				s.getResponse().setContentType("text/html");
				s.getResponse().setHeader("Cache-Control", "no-cache");
				PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
				out.print(jsonStr);
				out.flush();
				out.close();
				return;
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		Form form = new Form(getFormAction(), Form.POST).setName("form").setEncType("");
		form.setOnSubmit("return check();");

		form.addElement(createContent(s));

		setContent(form);

	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Current WebSession
	 */

	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		String lineSep = System.getProperty("line.separator");
		String script = "<script>"
				+ lineSep
				+ "function getFlights() {"
				+ lineSep
				+ "var fromField = document.getElementById('"
				+ TRAVEL_FROM
				+ "');"
				+ lineSep
				+ "if (fromField.value.length < 3 || fromField.value!='BOS') { return; }"
				+ lineSep
				+ "var toField = document.getElementById('"
				+ TRAVEL_TO
				+ "');"
				+ lineSep
				+ "if (toField.value.length < 3 || toField.value!='SEA') { return; }"
				+ lineSep
				+ "var url = '"
				+ getLink()
				+ "&from=ajax&"
				+ TRAVEL_FROM
				+ "=' + encodeURIComponent(fromField.value) +"
				+ "'&"
				+ TRAVEL_TO
				+ "=' + encodeURIComponent(toField.value);"
				+ lineSep
				+ "if (typeof XMLHttpRequest != 'undefined') {"
				+ lineSep
				+ "req = new XMLHttpRequest();"
				+ lineSep
				+ "} else if (window.ActiveXObject) {"
				+ lineSep
				+ "req = new ActiveXObject('Microsoft.XMLHTTP');"
				+ lineSep
				+ "   }"
				+ lineSep
				+ "   req.open('GET', url, true);"
				+ lineSep
				+ "   req.onreadystatechange = callback;"
				+ lineSep
				+ "   req.send(null);"
				+ lineSep
				+ "}"
				+ lineSep
				+ "function callback() {"
				+ lineSep
				+ "    if (req.readyState == 4) { "
				+ lineSep
				+ "        if (req.status == 200) { "
				+ lineSep
				+ "                   var card = eval('(' + req.responseText + ')');"
				+ lineSep
				+ "			 var flightsDiv = document.getElementById('flightsDiv');"
				+ lineSep
				+ "				flightsDiv.innerHTML = '';"
				+ lineSep
				+ "				var strHTML='';"
				+ lineSep
				+ "				strHTML = '<tr><td>&nbsp;</td><td>No of Stops</td>';"
				+ lineSep
				+ "				strHTML = strHTML + '<td>Stops</td><td>Prices</td></tr>';"
				+ lineSep
				+ "			 for(var i=0; i<card.flights.length; i++){"
				+ lineSep
				+ "				var node = card.flights[i];"
				+ lineSep
				+ "				strHTML = strHTML + '<tr><td><input name=\"radio'+i+'\" type=\"radio\" id=\"radio'+i+'\"></td><td>';"
				+ lineSep
				+ "			    strHTML = strHTML + card.flights[i].stops + '</td><td>';"
				+ lineSep
				+ "			    strHTML = strHTML + card.flights[i].transit + '</td><td>';"
				+ lineSep
				+ "			    strHTML = strHTML + '<div name=\"priceID'+i+'\" id=\"priceID'+i+'\">' + card.flights[i].price + '</div></td></tr>';"
				+ lineSep
				+ "			 }"
				+ lineSep
				+ "				strHTML = '<table border=\"1\">' + strHTML + '</table>';"
				+ lineSep
				+ "               flightsDiv.innerHTML = strHTML;"
				+ lineSep
				+ "        }}}"
				+ lineSep
				+

				"function check(){"
				+ lineSep
				+ " if ( document.getElementById('radio0').checked  )"
				+ lineSep
				+ " { document.getElementById('price2Submit').value = document.getElementById('priceID0').innerHTML; return true;}"
				+ lineSep
				+ " else if ( document.getElementById('radio1').checked  )"
				+ lineSep
				+ " { document.getElementById('price2Submit').value = document.getElementById('priceID1').innerHTML; return true;}"
				+ lineSep + " else " + lineSep + " { alert('Please choose one flight'); return false;}" + lineSep + "}"
				+ lineSep + "</script>" + lineSep;
		ec.addElement(new StringElement(script));
		Table t1 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("90%").setAlign("center");

		TR tr = new TR();

		tr.addElement(new TD("From: "));
		Input in = new Input(Input.TEXT, TRAVEL_FROM, "");
		in.addAttribute("onkeyup", "getFlights();");
		in.addAttribute("id", TRAVEL_FROM);
		tr.addElement(new TD(in));

		t1.addElement(tr);

		tr = new TR();
		tr.addElement(new TD("To: "));
		in = new Input(Input.TEXT, TRAVEL_TO, "");
		in.addAttribute("onkeyup", "getFlights();");
		in.addAttribute("id", TRAVEL_TO);
		tr.addElement(new TD(in));

		t1.addElement(tr);
		ec.addElement(t1);

		ec.addElement(new BR());
		ec.addElement(new BR());
		Div div = new Div();
		div.addAttribute("name", "flightsDiv");
		div.addAttribute("id", "flightsDiv");
		ec.addElement(div);

		Input b = new Input();
		b.setType(Input.SUBMIT);
		b.setValue("Submit");
		b.setName("SUBMIT");
		ec.addElement(b);

		Input price2Submit = new Input();
		price2Submit.setType(Input.HIDDEN);
		price2Submit.setName("price2Submit");
		price2Submit.setValue("");
		price2Submit.addAttribute("id", "price2Submit");
		ec.addElement(price2Submit);
		if (s.getParser().getRawParameter("radio0", "").equals("on"))
		{
			String price = s.getParser().getRawParameter("price2Submit", "");
			price = price.replace("$", "");
			if (Integer.parseInt(price) < 600)
			{
				makeSuccess(s);
			}
			else
			{
				s.setMessage("You are close, try to set the price for the non-stop flight to be less than $600");
			}
		}
		return ec;
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by Sherif Koussa", MAC_LOGO);
	}

	protected Category getDefaultCategory()
	{
		return Category.AJAX_SECURITY;
	}

	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("JSON stands for JavaScript Object Notation.");
		hints.add("JSON is a way of representing data just like XML.");
		hints.add("The JSON payload is easily interceptable.");
		hints.add("Intercept the reply, change the $600 to $25.");
		return hints;

	}

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the HelloScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("JSON Injection");
	}

}
