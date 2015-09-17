
package org.owasp.webgoat.session;

import java.io.Serializable;


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
 * For details, please see http://webgoat.github.io
 *
 * @version $Id: $Id
 */
public class EmployeeStub implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7109162877797765632L;

	private int id;

	private String firstName;

	private String lastName;

	private String role;

	/**
	 * <p>Constructor for EmployeeStub.</p>
	 *
	 * @param id a int.
	 * @param firstName a {@link java.lang.String} object.
	 * @param lastName a {@link java.lang.String} object.
	 */
	public EmployeeStub(int id, String firstName, String lastName)
	{
		this(id, firstName, lastName, Employee.EMPLOYEE_ROLE);
	}

	/**
	 * <p>Constructor for EmployeeStub.</p>
	 *
	 * @param id a int.
	 * @param firstName a {@link java.lang.String} object.
	 * @param lastName a {@link java.lang.String} object.
	 * @param role a {@link java.lang.String} object.
	 */
	public EmployeeStub(int id, String firstName, String lastName, String role)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}

	/**
	 * <p>Getter for the field <code>firstName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a int.
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * <p>Getter for the field <code>lastName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * <p>Getter for the field <code>role</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRole()
	{
		return role;
	}
}
