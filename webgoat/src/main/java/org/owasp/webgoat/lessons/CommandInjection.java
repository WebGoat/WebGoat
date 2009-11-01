
package org.owasp.webgoat.lessons;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.P;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.Exec;
import org.owasp.webgoat.util.ExecResults;
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
public class CommandInjection extends LessonAdapter
{
	private final static String HELP_FILE = "HelpFile";

	private String osName = System.getProperty("os.name");

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
		boolean illegalCommand = getWebgoatContext().isDefuseOSCommands();
		try
		{
			String helpFile = s.getParser().getRawParameter(HELP_FILE, "BasicAuthentication.help");
			if (getWebgoatContext().isDefuseOSCommands()
					&& (helpFile.indexOf('&') != -1 || helpFile.indexOf(';') != -1))
			{
				int index = helpFile.indexOf('&');
				if (index == -1)
				{
					index = helpFile.indexOf(';');
				}
				index = index + 1;
				int helpFileLen = helpFile.length() - 1; // subtract 1 for the closing quote
				System.out.println(WebGoatI18N.get("Command")+" = [" + helpFile.substring(index, helpFileLen).trim().toLowerCase() + "]");
				if ((osName.indexOf("Windows") != -1 && (helpFile.substring(index, helpFileLen).trim().toLowerCase()
						.equals("netstat -a")
						|| helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("dir")
						|| helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("ls")
						|| helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("ifconfig") || helpFile
						.substring(index, helpFileLen).trim().toLowerCase().equals("ipconfig")))
						|| (helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("netstat -a #")
								|| helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("dir #")
								|| helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("ls #")
								|| helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("ls -l #")
								|| helpFile.substring(index, helpFileLen).trim().toLowerCase().equals("ifconfig #") || helpFile
								.substring(index, helpFileLen).trim().toLowerCase().equals("ipconfig #")))
				{
					illegalCommand = false;
				}
				else
				{
					s.setMessage(WebGoatI18N.get("CommandInjectionRightTrack1"));
							
				}
			}

			if (getWebgoatContext().isDefuseOSCommands() && helpFile.indexOf('&') == -1 && helpFile.indexOf(';') == -1)
			{
				if (helpFile.length() > 0)
				{
					if (upDirCount(helpFile) <= 3)
					{
						// FIXME: This value isn't used. What is the goal here?
						s.getContext().getRealPath("/");
						illegalCommand = false;
					}
					else
					{
						s.setMessage(WebGoatI18N.get("CommandInjectionRightTrack2"));
					}
				}
				else
				{
					// No Command entered.
					illegalCommand = false;
				}
			}
			File safeDir = new File(s.getContext().getRealPath("/lesson_plans/English"));

			ec.addElement(new StringElement(WebGoatI18N.get("YouAreCurrentlyViewing")+"<b>"
					+ (helpFile.toString().length() == 0 ? "&lt;"+WebGoatI18N.get("SelectFileFromListBelow")+"&gt;" : helpFile.toString())
					+ "</b>"));

			if (!illegalCommand)
			{
				String results;
				String fileData = null;
				helpFile = helpFile.replaceAll("\\.help", "\\.html");

				if (osName.indexOf("Windows") != -1)
				{
					// Add quotes around the filename to avoid having special characters in DOS
					// filenames
					results = exec(s, "cmd.exe /c dir /b \"" + safeDir.getPath() + "\"");
					fileData = exec(s, "cmd.exe /c type \"" + new File(safeDir, helpFile).getPath() + "\"");

				}
				else
				{
					String[] cmd1 = { "/bin/sh", "-c", "ls \"" + safeDir.getPath() + "\"" };
					results = exec(s, cmd1);
					String[] cmd2 = { "/bin/sh", "-c", "cat \"" + new File(safeDir, helpFile).getPath() + "\"" };
					fileData = exec(s, cmd2);
				}

				ec.addElement(new P().addElement(WebGoatI18N.get("SelectLessonPlanToView")));
				ec.addElement(ECSFactory.makePulldown(HELP_FILE, parseResults(results.replaceAll("(?s)\\.html",
																									"\\.help"))));
				// ec.addElement( results );
				Element b = ECSFactory.makeButton(WebGoatI18N.get("View"));
				ec.addElement(b);
				// Strip out some of the extra html from the "help" file
				ec.addElement(new BR());
				ec.addElement(new BR());
				ec.addElement(new HR().setWidth("90%"));
				ec.addElement(new StringElement(fileData.replaceAll(System.getProperty("line.separator"), "<br>")
						.replaceAll("(?s)<!DOCTYPE.*/head>", "").replaceAll("<br><br>", "<br>")
						.replaceAll("<br>\\s<br>", "<br>")));

			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	private String parseResults(String results)
	{
		results.replaceAll("(?s).*Output...\\s", "").replaceAll("(?s)Returncode.*", "");
		StringTokenizer st = new StringTokenizer(results, "\n");
		StringBuffer modified = new StringBuffer();

		while (st.hasMoreTokens())
		{
			String s = (String) st.nextToken().trim();

			if (s.length() > 0 && s.endsWith(".help"))
			{
				modified.append(s + "\n");
			}
		}

		return modified.toString();
	}

	public static int upDirCount(String fileName)
	{
		int count = 0;
		// check for "." = %2d
		// we wouldn't want anyone bypassing the check by useing encoding :)
		// FIXME: I don't think hex endoing will work here.
		fileName = fileName.replaceAll("%2d", ".");
		int startIndex = fileName.indexOf("..");
		while (startIndex != -1)
		{
			count++;
			startIndex = fileName.indexOf("..", startIndex + 1);
		}
		return count;
	}

	/**
	 * Description of the Method
	 * 
	 * @param command
	 *            Description of the Parameter
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String exec(WebSession s, String command)
	{
		System.out.println("Executing OS command: " + command);
		ExecResults er = Exec.execSimple(command);
		if ((command.indexOf("&") != -1 || command.indexOf(";") != -1) && !er.getError())
		{
			makeSuccess(s);
		}

		return (er.toString());
	}

	/**
	 * Description of the Method
	 * 
	 * @param command
	 *            Description of the Parameter
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String exec(WebSession s, String[] command)
	{
		System.out.println("Executing OS command: " + Arrays.asList(command));
		ExecResults er = Exec.execSimple(command);
		if (!er.getError())
		{
			makeSuccess(s);
		}

		return (er.toString());
	}

	/**
	 * Gets the category attribute of the CommandInjection object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
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
		hints.add(WebGoatI18N.get("CommandInjectionHint1"));
		hints.add(WebGoatI18N.get("CommandInjectionHint2"));
		hints.add(WebGoatI18N.get("CommandInjectionHint3"));
		hints.add(WebGoatI18N.get("CommandInjectionHint4"));

		return hints;
	}


	private final static Integer DEFAULT_RANKING = new Integer(40);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the DirectoryScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return "Command Injection";
	}
}
