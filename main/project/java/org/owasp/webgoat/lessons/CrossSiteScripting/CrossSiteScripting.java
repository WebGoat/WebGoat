
package org.owasp.webgoat.lessons.CrossSiteScripting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Body;
import org.apache.ecs.html.Head;
import org.apache.ecs.html.Html;
import org.apache.ecs.html.Title;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.GoatHillsFinancial.DeleteProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.GoatHillsFinancial.ListStaff;
import org.owasp.webgoat.lessons.GoatHillsFinancial.Login;
import org.owasp.webgoat.lessons.GoatHillsFinancial.Logout;
import org.owasp.webgoat.lessons.GoatHillsFinancial.SearchStaff;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;


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
public class CrossSiteScripting extends GoatHillsFinancial
{
	private final static Integer DEFAULT_RANKING = new Integer(100);

	public final static String STAGE1 = "Stored XSS";

	public final static String STAGE2 = "Block Stored XSS using Input Validation";

	public final static String STAGE3 = "Stored XSS Revisited";

	public final static String STAGE4 = "Block Stored XSS using Output Encoding";

	public final static String STAGE5 = "Reflected XSS";

	public final static String STAGE6 = "Block Reflected XSS";

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

	public String getLessonSolutionFileName(WebSession s)
	{
		String solutionFileName = null;
		String stage = getStage(s);
		solutionFileName = "/lesson_solutions/Lab XSS/Lab " + stage + ".html";
		return solutionFileName;
	}

	@Override
	public String getSolution(WebSession s)
	{
		String src = null;

		try
		{
			// System.out.println("Solution: " + getLessonSolutionFileName(s));
			src = readFromFile(new BufferedReader(new FileReader(s.getWebResource(getLessonSolutionFileName(s)))),
								false);
		} catch (IOException e)
		{
			s.setMessage("Could not find the solution file");
			src = ("Could not find the solution file");
		}

		return src;
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
		hints.add("Stage1: You can put HTML tags in form input fields.");
		hints.add("Stage1: Bury a SCRIPT tag in the field to attack anyone who reads it.");
		hints
				.add("Stage1: Enter this: &lt;script language=\"javascript\" type=\"text/javascript\"&gt;alert(\"Ha Ha Ha\");&lt;/script&gt; in message fields.");
		hints.add("Stage1: Enter this: &lt;script&gt;alert(\"document.cookie\");&lt;/script&gt; in message fields.");

		// Stage 2
		hints.add("Stage2: Many scripts rely on the use of special characters such as: &lt;");
		hints
				.add("Stage2: Allowing only a certain set of characters (positive filtering) is preferred to blocking a set of characters (negative filtering).");
		hints.add("Stage2: The java.util.regex package is useful for filtering string values.");

		// Stage 3
		hints
				.add("Stage3: Browsers recognize and decode HTML entity encoded content after parsing and interpretting HTML tags.");
		hints.add("Stage3: An HTML entity encoder is provided in the ParameterParser class.");

		// Stage 4
		hints
				.add("Stage4: Examine content served in response to form submissions looking for data taken from the form.");
		hints.add("Stage4: There is a class called HtmlEncoder in org.owasp.webgoat.util");
		// Stage 5
		hints
				.add("Stage5: Validate early.  Consider: out.println(\"Order for \" + request.getParameter(\"product\") + \" being processed...\");");

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
				instructions = "Stage 1: Execute a Stored Cross Site Scripting (XSS) attack.<br>"
						+ "As 'Tom', execute a Stored XSS attack against the Street field on the Edit Profile page.  "
						+ "Verify that 'Jerry' is affected by the attack. <br/>The passwords for the accounts are the lower-case " 
						+ "versions of their given names (e.g. the password for Tom Cat is \"tom\").";
			}
			else if (STAGE2.equals(stage))
			{
				instructions = "Stage 2: Block Stored XSS using Input Validation.<br><br>"
						+ "<b><font color=blue> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br/><br/>"
						+ "Implement a fix to block the stored XSS before it can be written to the database. "
						+ "Repeat stage 1 as 'Eric' with 'David' as the manager.  Verify that 'David' is not affected by the attack.";
			}
			else if (STAGE3.equals(stage))
			{
				instructions = "Stage 3: Execute a previously Stored Cross Site Scripting (XSS) attack.<br>"
						+ "The 'Bruce' employee profile is pre-loaded with a stored XSS attack. "
						+ "Verify that 'David' is affected by the attack even though the fix from stage 2 is in place.";
			}
			else if (STAGE4.equals(stage))
			{
				instructions = "Stage 4: Block Stored XSS using Output Encoding.<br><br>"
						+ "<b><font color=blue> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br/><br/>"
						+ "Implement a fix to block XSS after it is read from the database. "
						+ "Repeat stage 3. Verify that 'David' is not affected by Bruce's profile attack.";
			}
			else if (STAGE5.equals(stage))
			{
				instructions = "Stage 5: Execute a Reflected XSS attack.<br>"
						+ "Use a vulnerability on the Search Staff page to craft a URL containing a reflected XSS attack.  "
						+ "Verify that another employee using the link is affected by the attack.";
			}
			else if (STAGE6.equals(stage))
			{
				instructions = "Stage 6: Block Reflected XSS using Input Validation.<br><br>"
						+ "<b><font color=blue> THIS LESSON ONLY WORKS WITH THE DEVELOPER VERSION OF WEBGOAT</font></b><br/><br/>"
						+ "Implement a fix to block this reflected XSS attack. "
						+ "Repeat step 5.  Verify that the attack URL is no longer effective.";
			}
		}

		return instructions;

	}

	@Override
	public String[] getStages()
	{
		if (getWebgoatContext().isCodingExercises())
			return new String[] { STAGE1, STAGE2, STAGE3, STAGE4, STAGE5, STAGE6 };
		return new String[] { STAGE1, STAGE3, STAGE5 };
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
		return "LAB: Cross Site Scripting";
	}

	public String htmlEncode(WebSession s, String text)
	{
		if (STAGE4.equals(getStage(s)) && text.indexOf("<script>") > -1 && text.indexOf("alert") > -1
				&& text.indexOf("</script>") > -1)
		{
			setStageComplete(s, STAGE4);
			s.setMessage("Welcome to stage 5 -- exploiting the data layer");
		}

		return HtmlEncoder.encode(text);
	}

}
