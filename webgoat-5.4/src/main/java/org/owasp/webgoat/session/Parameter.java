
package org.owasp.webgoat.session;

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
public class Parameter implements Comparable
{

	String name;

	String value;

	public Parameter(String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public String getValue()
	{
		return value;
	}

	// @Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Parameter)
		{
			Parameter other = (Parameter) obj;
			return (name.equals(other.getName()) && value.equals(other.getValue()));
		}
		return false;
	}

	// @Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	// @Override
	public String toString()
	{
		return (name + "=" + value);
	}

	public int compareTo(Object o)
	{
		return toString().compareTo(o.toString());
	}
}
