
package org.owasp.webgoat.lessons.SQLInjection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DeleteProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.EditProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.FindProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.Logout;
import org.owasp.webgoat.lessons.GoatHillsFinancial.SearchStaff;
import org.owasp.webgoat.lessons.GoatHillsFinancial.UpdateProfile;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
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
 */
public class SQLInjection extends GoatHillsFinancial
{
	private final static Integer DEFAULT_RANKING = new Integer(75);

	public final static int PRIZE_EMPLOYEE_ID = 112;

	public final static String PRIZE_EMPLOYEE_NAME = "Neville Bartholomew";

	public final static String STAGE1 = "String SQL Injection";

	public final static String STAGE2 = "Parameterized Query #1";

	public final static String STAGE3 = "Numeric SQL Injection";

	public final static String STAGE4 = "Parameterized Query #2";

	public void registerActions(String className)
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
		return Category.INJECTION;
	}

	/**
	 * Gets the hints attribute of the DirectoryScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("The application is taking your input and inserting it at the end of a pre-formed SQL command.");
		hints.add("This is the code for the query being built and issued by WebGoat:<br><br> "
				+ "\"SELECT * FROM employee WHERE userid = \" + userId + \" and password = \" + password");
		hints.add("Compound SQL statements can be made by joining multiple tests with keywords like AND and OR.  "
				+ "Try appending a SQL statement that always resolves to true");

		// Stage 1
		hints.add("You may need to use WebScarab to remove a field length limit to fit your attack.");
		hints.add("Try entering a password of [ smith' OR '1' = '1 ].");

		// Stage 2
		hints
				.add("Many of WebGoat's database queries are already parameterized.  Search the project for PreparedStatement.");

		// Stage 3
		hints.add("Try entering an employee_id of [ 101 or 1=1 order by salary desc ].");

		// Stage 4

		return hints;
	}

	@Override
	public String[] getStages()
	{
		if (getWebgoatContext().isCodingExercises()) return new String[] { STAGE1, STAGE2, STAGE3, STAGE4 };
		return new String[] { STAGE1, STAGE3 };
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
				instructions = "Stage 1: Use String SQL Injection to bypass authentication. "
						+ "Use SQL injection to log in as the boss ('Neville') without using the correct password.  "
						+ "Verify that Neville's profile can be viewed and that all functions are available (including Search, Create, and Delete).";
			}
			else if (STAGE2.equals(stage))
			{
				instructions = "Stage 2: Block SQL Injection using a Parameterized Query.<br><br>"
						+ "<b><font color=blue> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br><br>"
						+ "Implement a fix to block SQL injection into the fields in question on the Login page. "
						+ "Repeat stage 1.  Verify that the attack is no longer effective.";
			}
			else if (STAGE3.equals(stage))
			{
				instructions = "Stage 3: Execute SQL Injection to bypass authorization.<br>"
						+ "As regular employee 'Larry', use SQL injection into a parameter of the View function "
						+ "(from the List Staff page) to view the profile of the boss ('Neville').";
			}
			else if (STAGE4.equals(stage))
			{
				instructions = "Stage 4: Block SQL Injection using a Parameterized Query.<br><br>"
						+ "<b><font color=blue> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br><br>"
						+ "Implement a fix to block SQL injection into the relevant parameter. "
						+ "Repeat stage 3.  Verify that access to Neville's profile is properly blocked.";
			}
		}

		return instructions;
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
					// System.out.println("CrossSiteScripting.handleRequest() dispatching to: " +
					// action.getActionName());
					if (!action.requiresAuthentication() || action.isAuthenticated(s))
					{
						action.handleRequest(s);
						// setCurrentAction(s, action.getNextPage(s));
					}
				}
				else
					setCurrentAction(s, ERROR_ACTION);
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
		return "LAB: SQL Injection";
	}

	@Override
	public String getSolution(WebSession s)
	{
		String src = null;

		try
		{
			src = readFromFile(new BufferedReader(new FileReader(s.getWebResource(getLessonSolutionFileName(s)))),
								false);
		} catch (IOException e)
		{
			s.setMessage("Could not find the solution file");
			src = ("Could not find the solution file");
		}
		return src;
	}

	public String getLessonSolutionFileName(WebSession s)
	{
		String solutionFileName = null;
		String stage = getStage(s);
		solutionFileName = "/lesson_solutions/Lab SQL Injection/Lab " + stage + ".html";
		return solutionFileName;
	}
}
