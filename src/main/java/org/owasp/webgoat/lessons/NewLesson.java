
package org.owasp.webgoat.lessons;

import org.apache.ecs.Element;
import org.apache.ecs.StringElement;
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
 * @created October 28, 2003
 */
public class NewLesson extends LessonAdapter
{
	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element createContent(WebSession s)
	{
		return super.createContent(s);
		// makeSuccess(s);
		// ec.addElement(new StringElement("Welcome to the WebGoat hall of fame !!"));
		// return (ec);
	}

	/**
	 * Gets the category attribute of the NEW_LESSON object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{
		return Category.INTRODUCTION;
	}

	private final static Integer DEFAULT_RANKING = new Integer(85);

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
		return ("How to create a Lesson");
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by: Your name goes here!", new StringElement(""));
	}

}
