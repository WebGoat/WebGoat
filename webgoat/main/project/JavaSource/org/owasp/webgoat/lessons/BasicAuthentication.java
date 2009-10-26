
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
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
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class BasicAuthentication extends SequentialLessonAdapter
{
	private static final String EMPTY_STRING = "";

	private static final String WEBGOAT_BASIC = "webgoat_basic";

	private static final String AUTHORIZATION = "Authorization";

	private static final String ORIGINAL_AUTH = "Original_Auth";

	private static final String ORIGINAL_USER = "Original.user";

	private static final String BASIC = "basic";

	private static final String JSESSIONID = "JSESSIONID";

	private final static String HEADER_NAME = "header";

	private final static String HEADER_VALUE = "value";

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

	protected Element doStage1(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		String headerName = null;
		String headerValue = null;
		try
		{
			headerName = new String(s.getParser().getStringParameter(HEADER_NAME, EMPTY_STRING));
			headerValue = new String(s.getParser().getStringParameter(HEADER_VALUE, EMPTY_STRING));

			// <START_OMIT_SOURCE>
			// FIXME: This won;t work for CBT, we need to use the UserTracker
			// Authorization: Basic Z3Vlc3Q6Z3Vlc3Q=
			if (headerName.equalsIgnoreCase(AUTHORIZATION)
					&& (headerValue.equals("guest:guest") || headerValue.equals("webgoat:webgoat")))
			{
				getLessonTracker(s).setStage(2);
				return doStage2(s);
			}
			else
			{
				if (headerName.length() > 0 && !headerName.equalsIgnoreCase(AUTHORIZATION))
				{
					s.setMessage(WebGoatI18N.get("BasicAuthHeaderNameIncorrect"));
				}
				if (headerValue.length() > 0
						&& !(headerValue.equals("guest:guest") || headerValue.equals("webgoat:webgoat")))
				{
					s.setMessage(WebGoatI18N.get("BasicAuthHeaderValueIncorrect"));

				}
			}
			// <END_OMIT_SOURCE>

			Table t = new Table(0).setCellSpacing(0).setCellPadding(0).setBorder(0);
			if (s.isColor())
			{
				t.setBorder(1);
			}

			TR row1 = new TR();
			TR row2 = new TR();
			row1.addElement(new TD(new StringElement(WebGoatI18N.get("BasicAuthenticationWhatIsNameOfHeader"))));
			row2.addElement(new TD(new StringElement(WebGoatI18N.get("BasicAuthenticationWhatIsDecodedValueOfHeader"))));

			row1.addElement(new TD(new Input(Input.TEXT, HEADER_NAME, headerName.toString())));
			row2.addElement(new TD(new Input(Input.TEXT, HEADER_VALUE, headerValue.toString())));

			t.addElement(row1);
			t.addElement(row2);

			ec.addElement(t);
			ec.addElement(new P());

			Element b = ECSFactory.makeButton(WebGoatI18N.get("Submit"));
			ec.addElement(b);

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	protected Element doStage2(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			if (s.getRequest().isUserInRole(WEBGOAT_BASIC))
			{
				String originalUser = getLessonTracker(s).getLessonProperties()
						.getProperty(ORIGINAL_USER, EMPTY_STRING);
				getLessonTracker(s, originalUser).setCompleted(true);
				getLessonTracker(s, originalUser).setStage(1);
				getLessonTracker(s, originalUser).store(s, this);
				makeSuccess(s);
				s.setMessage(WebGoatI18N.get("BasicAuthenticiationGreenStars1")+ originalUser + WebGoatI18N.get("BasicAuthenticationGreenStars2"));
				return ec;
			}
			else
			{
				// If we are still in the ORIGINAL_USER role see if the Basic Auth header has been
				// manipulated
				String originalAuth = getLessonTracker(s).getLessonProperties()
						.getProperty(ORIGINAL_AUTH, EMPTY_STRING);
				String originalSessionId = getLessonTracker(s).getLessonProperties()
						.getProperty(JSESSIONID, s.getCookie(JSESSIONID));

				// store the original user info in the BASIC properties files
				if (originalSessionId.equals(s.getCookie(JSESSIONID)))
				{
					// Store the original user name in the "basic" user properties file. We need to
					// use
					// the original user to access the correct properties file to update status.
					// store the initial auth header
					getLessonTracker(s).getLessonProperties().setProperty(JSESSIONID, originalSessionId);
					getLessonTracker(s).getLessonProperties().setProperty(ORIGINAL_AUTH, s.getHeader(AUTHORIZATION));
					getLessonTracker(s, BASIC).getLessonProperties().setProperty(ORIGINAL_USER, s.getUserName());
					getLessonTracker(s, BASIC).setStage(2);
					getLessonTracker(s, BASIC).store(s, this, BASIC);
				}

				s.setMessage(WebGoatI18N.get("BasicAuthenticationStage1Completed"));

				// If the auth header is different but still the original user - tell the user
				// that the original cookie was posted bak and basic auth uses the cookie before the
				// authorization token
				if (!originalAuth.equals("") && !originalAuth.equals(s.getHeader(AUTHORIZATION)))
				{
					ec
							.addElement(WebGoatI18N.get("BasicAuthenticationAlmostThere1")
									+ AUTHORIZATION
									+ WebGoatI18N.get("BasicAuthenticationAlmostThere2")
									+ s.getUserName()
									+ WebGoatI18N.get("BasicAuthenticationAlmostThere3"));
				}
				else if (!originalSessionId.equals(s.getCookie(JSESSIONID)))
				{
					ec
							.addElement(WebGoatI18N.get("BasicAuthenticationReallyClose"));
									
				}
				else
				{
					ec.addElement(WebGoatI18N.get("BasicAuthenticationUseTheHints"));
				}

			}

		} catch (Exception e)
		{
			s.setMessage(WebGoatI18N.get("ErrorGenerating") + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	/**
	 * Gets the category attribute of the ForgotPassword object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{

		return Category.AUTHENTICATION;
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		// int stage = getLessonTracker(session, BASIC).getStage();

		// switch ( stage )
		// {
		// case 1:
		hints.add(WebGoatI18N.get("BasicAuthenticationHint1"));
		hints.add(WebGoatI18N.get("BasicAuthenticationHint2"));
		hints.add(WebGoatI18N.get("BasicAuthenticationHint3"));
		hints.add(WebGoatI18N.get("BasicAuthenticationHint4"));
		
		// break;
		// case 2:
		hints.add(WebGoatI18N.get("BasicAuthenticationHint5"));
		hints.add(WebGoatI18N.get("BasicAuthenticationHint6"));
		hints.add(WebGoatI18N.get("BasicAuthenticationHint7"));
		hints.add(WebGoatI18N.get("BasicAuthenticationHint8"));
		hints.add(WebGoatI18N.get("BasicAuthenticationHint9"));
		
		// break;
		// }

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(100);

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
		return ("Basic Authentication");
	}

}
