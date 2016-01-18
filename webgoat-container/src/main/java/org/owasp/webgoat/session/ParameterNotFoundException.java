
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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @version $Id: $Id
 */
public class ParameterNotFoundException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3286112913299408382L;

	/**
	 * Constructs a new ParameterNotFoundException with no detail message.
	 */
	public ParameterNotFoundException()
	{
		super();
	}

	/**
	 * Constructs a new ParameterNotFoundException with the specified detail message.
	 *
	 * @param s
	 *            the detail message
	 */
	public ParameterNotFoundException(String s)
	{
		super(s);
	}
}
