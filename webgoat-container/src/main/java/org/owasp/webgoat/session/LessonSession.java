
package org.owasp.webgoat.session;

/**
 *************************************************************************************************
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
 * Represents a virtual session for a lesson. Lesson-specific session data may be stored here.
 *
 * @author David Anderson <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @since January 19, 2006
 * @version $Id: $Id
 */
public class LessonSession
{

	private boolean isAuthenticated = false;

	private String currentLessonScreen;

	/**
	 * <p>setAuthenticated.</p>
	 *
	 * @param isAuthenticated a boolean.
	 */
	public void setAuthenticated(boolean isAuthenticated)
	{
		this.isAuthenticated = isAuthenticated;
	}

	/**
	 * <p>isAuthenticated.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isAuthenticated()
	{
		return this.isAuthenticated;
	}

	/**
	 * <p>Setter for the field <code>currentLessonScreen</code>.</p>
	 *
	 * @param currentLessonScreen a {@link java.lang.String} object.
	 */
	public void setCurrentLessonScreen(String currentLessonScreen)
	{
		this.currentLessonScreen = currentLessonScreen;
	}

	/**
	 * <p>Getter for the field <code>currentLessonScreen</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCurrentLessonScreen()
	{
		return this.currentLessonScreen;
	}

}
