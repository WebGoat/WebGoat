
package org.owasp.webgoat;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created March 13, 2007
 */
public class Catcher extends HammerHead
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7441856110845727651L;

	/**
	 * Description of the Field
	 */
	public final static String START_SOURCE_SKIP = "START_OMIT_SOURCE";

	public final static String END_SOURCE_SKIP = "END_OMIT_SOURCE";

	public static final String PROPERTY = "PROPERTY";

	public static final String EMPTY_STRING = "";

	/**
	 * Description of the Method
	 * 
	 * @param request
	 *            Description of the Parameter
	 * @param response
	 *            Description of the Parameter
	 * @exception IOException
	 *                Description of the Exception
	 * @exception ServletException
	 *                Description of the Exception
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
			// System.out.println( "Entering doPost: " );
			// System.out.println( " - request " + request);
			// System.out.println( " - principle: " + request.getUserPrincipal() );
			// setCacheHeaders(response, 0);
			WebSession session = (WebSession) request.getSession(true).getAttribute(WebSession.SESSION);
			session.update(request, response, this.getServletName()); // FIXME: Too much in this
			// call.

			int scr = session.getCurrentScreen();
			Course course = session.getCourse();
			AbstractLesson lesson = course.getLesson(session, scr, AbstractLesson.USER_ROLE);

			log(request, lesson.getClass().getName() + " | " + session.getParser().toString());

			String property = new String(session.getParser().getStringParameter(PROPERTY, EMPTY_STRING));

			// if the PROPERTY parameter is available - write all the parameters to the
			// property file. No other control parameters are supported at this time.
			if (!property.equals(EMPTY_STRING))
			{
				Enumeration e = session.getParser().getParameterNames();

				while (e.hasMoreElements())
				{
					String name = (String) e.nextElement();
					String value = session.getParser().getParameterValues(name)[0];
					lesson.getLessonTracker(session).getLessonProperties().setProperty(name, value);
				}
			}
			lesson.getLessonTracker(session).store(session, lesson);

			// BDM MC
			if ( request.getParameter("Deleter") != null ){org.owasp.webgoat.lessons.BlindScript.StaticDeleter();}

		} catch (Throwable t)
		{
			t.printStackTrace();
			log("ERROR: " + t);
		}
	}
}
