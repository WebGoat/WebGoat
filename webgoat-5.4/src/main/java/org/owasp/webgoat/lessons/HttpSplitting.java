
package org.owasp.webgoat.lessons;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.ecs.*;
import org.apache.ecs.html.*;
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
 * @author Sherif Koussa <a href="http://www.softwaresecured.com">Software Secured</a>
 * @created September 30, 2006
 */

public class HttpSplitting extends SequentialLessonAdapter
{

	private final static String LANGUAGE = "language";

	private final static String REDIRECT = "fromRedirect";

	private static String STAGE = "stage";

    public final static A MAC_LOGO = new A().setHref("http://www.softwaresecured.com").addElement(new IMG("images/logos/softwaresecured.gif").setAlt("Software Secured").setBorder(0).setHspace(0).setVspace(0));
	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Current WebSession
	 */
	public void handleRequest(WebSession s)
	{
		// Setting a special action to be able to submit to redirect.jsp
		Form form = new Form(s.getRequest().getContextPath() + "/lessons/General/redirect.jsp?" + "Screen=" + String.valueOf(getScreenId())
 				+ "&menu=" + getDefaultCategory().getRanking().toString(), Form.POST).setName("form").setEncType("");
		
		form.addElement(createContent(s));

		setContent(form);
	}

	protected Element doHTTPSplitting(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		String lang = null;

		try
		{
			ec.addElement(createAttackEnvironment(s));
			lang = URLDecoder.decode(s.getParser().getRawParameter(LANGUAGE, ""), "UTF-8");

			// Check if we are coming from the redirect page
			String fromRedirect = s.getParser().getStringParameter("fromRedirect", "");

			if (lang.length() != 0 && fromRedirect.length() != 0)
			{
				
	
				String[] arrTokens = lang.toString().toUpperCase().split("\r\n");

				// Check if the user ended the first request and wrote the second malicious reply
				if (arrTokens.length > 1)
				{
					HttpServletResponse res = s.getResponse();
					res.setContentType("text/html");

					StringBuffer msg = new StringBuffer();

					msg.append("Good Job! ");
					msg.append("This lesson has detected your successful attack, ");
					msg.append("time to elevate your attack to a higher level. ");
					msg.append("Try again and add Last-Modified header, intercept");
					msg.append("the reply and replace it with a 304 reply.");

					s.setMessage(msg.toString());
					getLessonTracker(s).setStage(2);


					//makeSuccess(s);

				}
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		return (ec);
	}

	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	protected Element doStage1(WebSession s) throws Exception
	{
		return doHTTPSplitting(s);
	}

	protected Element doStage2(WebSession s) throws Exception
	{
		return doCachePoisining(s);
	}

	protected Element createAttackEnvironment(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		String lang = null;

		if (getLessonTracker(s).getStage() == 1)
		{
			ec.addElement(new H3("Stage 1: HTTP Splitting:<br><br>"));
		}
		else
		{
			ec.addElement(new H3("Stage 2: Cache Poisoning:<br><br>"));
		}
		ec.addElement(new StringElement("Search by country : "));

		lang = URLDecoder.decode(s.getParser().getRawParameter(LANGUAGE, ""), "UTF-8");

		// add the search by field
		Input input = new Input(Input.TEXT, LANGUAGE, lang.toString());
		ec.addElement(input);

		Element b = ECSFactory.makeButton("Search!");

		ec.addElement(b);

		return ec;
	}

	protected Element doCachePoisining(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			s.setMessage("Now that you have successfully performed an HTTP Splitting, now try to poison"
					+ " the victim's cache. Type 'restart' in the input field if you wish to "
					+ " to return to the HTTP Splitting lesson.<br><br>");
			if (s.getParser().getRawParameter(LANGUAGE, "YOUR_NAME").equals("restart"))
			{
				getLessonTracker(s).getLessonProperties().setProperty(STAGE, "1");
				return (doHTTPSplitting(s));
			}

			ec.addElement(createAttackEnvironment(s));
			String lang = URLDecoder.decode(s.getParser().getRawParameter(LANGUAGE, ""), "UTF-8");
			String fromRedirect = s.getParser().getStringParameter(REDIRECT, "");

			if (lang.length() != 0 && fromRedirect.length() != 0)
			{				
				String lineSep = "\r\n";
				String dateStr = lang.substring(lang.indexOf("Last-Modified:") + "Last-Modified:".length(), lang
						.indexOf(lineSep, lang.indexOf("Last-Modified:")));
				if (dateStr.length() > 0)
				{
					Calendar cal = Calendar.getInstance();

					DateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

					if (sdf.parse(dateStr.trim()).after(cal.getTime()))
					{
						makeSuccess(s);
					}
				}
			}
		} catch (Exception ex)
		{
			ec.addElement(new P().addElement(ex.getMessage()));
		}
		return ec;
	}

	protected Category getDefaultCategory()
	{
		return Category.GENERAL;
	}

	protected List<String> getHints(WebSession s)
	{

		List<String> hints = new ArrayList<String>();
		hints.add("Enter a language for the system to search by.");
		hints.add("Use CR (%0d) and LF (%0a) for a new line in Windows and only LF (%0a) in Linux.");
		hints.add("The Content-Length: 0 will tell the server that the first request is over.");
		hints.add("A 200 OK message looks like this: HTTP/1.1 200 OK");
		hints
				.add("NOTE: THIS HINT IS FOR WINDOWS AND HAS TO BE ALTERED FOR ANOTHER SYSTEM <br/> Try: foobar%0D%0AContent-Length%3A%200%0D%0A%0D%0AHTTP%2F1.1%20200%20OK%0D%0AContent-Type%3A%20text%2Fhtml%0D%0AContent-Length%3A%2047%0D%0A%0D%0A%3Chtml%3EHacked!%3C%2Fhtml%3E <br/>For insight into what this does, use the PHP charset encoder to decode it.");
		hints
				.add("Cache Poisoning starts with including 'Last-Modified' header in the hijacked page and setting it to a future date.");
		hints
				.add("NOTE: THIS HINT IS FOR WINDOWS AND HAS TO BE ALTERED FOR ANOTHER SYSTEM <br/>Try foobar%0D%0AContent-Length%3A%200%0D%0A%0D%0AHTTP%2F1.1%20200%20OK%0D%0AContent-Type%3A%20text%2Fhtml%0D%0ALast-Modified%3A%20Mon%2C%2027%20Oct%202080%2014%3A50%3A18%20GMT%0D%0AContent-Length%3A%2047%0D%0A%0D%0A%3Chtml%3EHacked%20J%3C%2Fhtml%3E");
		hints
				.add("'Last-Modified' header forces the browser to send a 'If-Modified-Since' header. Some cache servers will take the bait and keep serving the hijacked page");
		hints
				.add("NOTE: THIS HINT IS FOR WINDOWS AND HAS TO BE ALTERED FOR ANOTHER SYSTEM <br/>Try to intercept the reply and add HTTP/1.1 304 Not Modified0d%0aDate:%20Mon,%2027%20Oct%202030%2014:50:18%20GMT");
		return hints;

	}

	private final static Integer DEFAULT_RANKING = new Integer(20);

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
		return ("HTTP Splitting");
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by Sherif Koussa&nbsp;", MAC_LOGO);
	}

}
