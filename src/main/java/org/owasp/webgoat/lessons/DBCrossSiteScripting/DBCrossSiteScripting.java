
package org.owasp.webgoat.lessons.DBCrossSiteScripting;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DeleteProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.EditProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.FindProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.ListStaff;
import org.owasp.webgoat.lessons.GoatHillsFinancial.Login;
import org.owasp.webgoat.lessons.GoatHillsFinancial.Logout;
import org.owasp.webgoat.lessons.GoatHillsFinancial.SearchStaff;
import org.owasp.webgoat.lessons.GoatHillsFinancial.ViewProfile;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


/**
 * /*******************************************************************************
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
 */
public class DBCrossSiteScripting extends GoatHillsFinancial
{
	private final static Integer DEFAULT_RANKING = new Integer(100);

	public final static String STAGE1 = "Stored XSS";

	public final static String STAGE2 = "Block Stored XSS using DB Input Validation";

	protected void registerActions(String className)
	{
		registerAction(new ListStaff(this, className, LISTSTAFF_ACTION));
		registerAction(new SearchStaff(this, className, SEARCHSTAFF_ACTION));
		registerAction(new ViewProfile(this, className, VIEWPROFILE_ACTION));
		registerAction(new EditProfile(this, className, EDITPROFILE_ACTION));
		registerAction(new EditProfile(this, className, CREATEPROFILE_ACTION));

		// These actions are special in that they chain to other actions.
		registerAction(new Login(this, className, LOGIN_ACTION, getAction(LISTSTAFF_ACTION)));
		registerAction(new Logout(this, className, LOGOUT_ACTION, getAction(LOGIN_ACTION)));
		registerAction(new FindProfile(this, className, FINDPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
		registerAction(new UpdateProfile(this, className, UPDATEPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
		registerAction(new DeleteProfile(this, className, DELETEPROFILE_ACTION, getAction(LISTSTAFF_ACTION)));
	}

	/**
	 * Gets the category attribute of the CrossSiteScripting object
	 * 
	 * @return The category value
	 */
	public Category getDefaultCategory()
	{
		return Category.XSS;
	}

	/**
	 * Gets the hints attribute of the DirectoryScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();

		// Stage 1
		hints.add("You can put HTML tags in form input fields.");
		hints.add("Bury a SCRIPT tag in the field to attack anyone who reads it.");
		hints
				.add("Enter this: &lt;script language=\"javascript\" type=\"text/javascript\"&gt;alert(\"Ha Ha Ha\");&lt;/script&gt; in message fields.");
		hints.add("Enter this: &lt;script&gtalert(\"document.cookie\");&lt;/script&gt; in message fields.");

		// Stage 2
		hints.add("Many scripts rely on the use of special characters such as: &lt;");
		hints
				.add("Allowing only a certain set of characters (positive filtering) is preferred to blocking a set of characters (negative filtering).");
		hints.add("Oracle 10 supports a regular expression matching function : REGEXP_LIKE(text, pattern).");

		return hints;
	}

	/**
	 * Gets the instructions attribute of the ParameterInjection object
	 * 
	 * @return The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "";

		if (!getLessonTracker(s).getCompleted())
		{
			String stage = getStage(s);
			if (STAGE1.equals(stage))
			{
				instructions = "Stage 1: Execute a Stored Cross Site Scripting (XSS) attack.<br><br>"
						+ "<b><font color=blue> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br/><br/>"
						+ "As 'Tom', execute a Stored XSS attack against the Street field on the Edit Profile page.  "
						+ "Verify that 'Jerry' is affected by the attack. "
						+ "A sample JavaScript snippet you can use is: &lt;SCRIPT&gt;alert('bang!');&lt;/SCRIPT&gt;.";
			}
			else if (STAGE2.equals(stage))
			{
				instructions = "Stage 2: Block Stored XSS using Input Validation.<br>"
						+ "Implement a fix in the stored procedure to prevent the stored XSS from being written to the database. ";
				if (getWebgoatContext().getDatabaseDriver().contains("jtds"))
					instructions += "Use the provided user-defined function RegexMatch to test the data against a pattern. ";
				instructions += "A sample regular expression pattern: ^[a-zA-Z0-9,\\. ]{0,80}$ "
						+ "Repeat stage 1 as 'Eric' with 'David' as the manager.  Verify that 'David' is not affected by the attack.";
			}
		}

		return instructions;

	}

	@Override
	public String[] getStages()
	{
		if (getWebgoatContext().isCodingExercises()) return new String[] { STAGE1, STAGE2 };
		return new String[] { STAGE1 };
	}

	public void handleRequest(WebSession s)
	{
		if (s.getLessonSession(this) == null) s.openLessonSession(this);

		String requestedActionName = null;
		try
		{
			requestedActionName = s.getParser().getStringParameter("action");
		} catch (ParameterNotFoundException pnfe)
		{
			// Let them eat login page.
			requestedActionName = LOGIN_ACTION;
		}

		if (requestedActionName != null)
		{
			try
			{
				LessonAction action = getAction(requestedActionName);

				if (action != null)
				{
					if (!action.requiresAuthentication() || action.isAuthenticated(s))
					{
						action.handleRequest(s);
						// setCurrentAction(s, action.getNextPage(s));
					}
				}
				else
				{
					setCurrentAction(s, ERROR_ACTION);
				}
			} catch (ParameterNotFoundException pnfe)
			{
				// System.out.println("Missing parameter");
				pnfe.printStackTrace();
				setCurrentAction(s, ERROR_ACTION);
			} catch (ValidationException ve)
			{
				// System.out.println("Validation failed");
				ve.printStackTrace();
				setCurrentAction(s, ERROR_ACTION);
			} catch (UnauthenticatedException ue)
			{
				s.setMessage("Login failed");
				// System.out.println("Authentication failure");
				ue.printStackTrace();
			} catch (UnauthorizedException ue2)
			{
				s.setMessage("You are not authorized to perform this function");
				// System.out.println("Authorization failure");
				ue2.printStackTrace();
			} catch (Exception e)
			{
				// All other errors send the user to the generic error page
				// System.out.println("handleRequest() error");
				e.printStackTrace();
				setCurrentAction(s, ERROR_ACTION);
			}
		}

		// All this does for this lesson is ensure that a non-null content exists.
		setContent(new ElementContainer());
	}

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the CrossSiteScripting object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return "LAB: DB Cross Site Scripting (XSS)";
	}

	@Override
	protected boolean getDefaultHidden()
	{
		String driver = getWebgoatContext().getDatabaseDriver();
		boolean hidden = !(driver.contains("oracle") || driver.contains("jtds"));
		return hidden;
	}

}