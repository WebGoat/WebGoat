
package org.owasp.webgoat.util;

import java.util.Locale;


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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for
 * free software projects.
 *
 * @version $Id: $Id
 * @author dm
 */
public interface LabelManager
{

	/**
	 * <p>setLocale.</p>
	 *
	 * @param locale a {@link java.util.Locale} object.
	 */
	public void setLocale(Locale locale);

	/**
	 * <p>get.</p>
	 *
	 * @param labelKey a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String get(String labelKey);

}
