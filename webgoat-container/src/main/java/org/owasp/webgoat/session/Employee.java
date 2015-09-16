
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
public class Employee implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1901957360367218399L;

	/** Constant <code>EMPLOYEE_ROLE="employee"</code> */
	public final static String EMPLOYEE_ROLE = "employee";

	/** Constant <code>MANAGER_ROLE="manager"</code> */
	public final static String MANAGER_ROLE = "manager";

	/** Constant <code>HR_ROLE="hr"</code> */
	public final static String HR_ROLE = "hr";

	private int id;

	private String firstName;

	private String lastName;

	private String title;

	private String ssn;

	private String phone;

	private String address1;

	private String address2;

	private int manager;

	private String startDate;

	private int salary;

	private String ccn;

	private int ccnLimit;

	private String disciplinaryActionDate;

	private String disciplinaryActionNotes;

	private String personalDescription;

	// FIXME: To be deleted
	/**
	 * <p>Constructor for Employee.</p>
	 */
	public Employee()
	{
	}

	/**
	 * <p>Constructor for Employee.</p>
	 *
	 * @param id a int.
	 * @param firstName a {@link java.lang.String} object.
	 * @param lastName a {@link java.lang.String} object.
	 * @param ssn a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param phone a {@link java.lang.String} object.
	 * @param address1 a {@link java.lang.String} object.
	 * @param address2 a {@link java.lang.String} object.
	 * @param manager a int.
	 * @param startDate a {@link java.lang.String} object.
	 * @param salary a int.
	 * @param ccn a {@link java.lang.String} object.
	 * @param ccnLimit a int.
	 * @param disciplinaryActionDate a {@link java.lang.String} object.
	 * @param disciplinaryActionNotes a {@link java.lang.String} object.
	 * @param personalDescription a {@link java.lang.String} object.
	 */
	public Employee(int id, String firstName, String lastName, String ssn, String title, String phone, String address1,
			String address2, int manager, String startDate, int salary, String ccn, int ccnLimit,
			String disciplinaryActionDate, String disciplinaryActionNotes, String personalDescription)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.title = title;
		this.phone = phone;
		this.address1 = address1;
		this.address2 = address2;
		this.manager = manager;
		this.startDate = startDate;
		this.salary = salary;
		this.ccn = ccn;
		this.ccnLimit = ccnLimit;
		this.disciplinaryActionDate = disciplinaryActionDate;
		this.disciplinaryActionNotes = disciplinaryActionNotes;
		this.personalDescription = personalDescription;
	}

	/**
	 * <p>Getter for the field <code>address1</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAddress1()
	{
		return address1;
	}

	/**
	 * <p>Setter for the field <code>address1</code>.</p>
	 *
	 * @param address1 a {@link java.lang.String} object.
	 */
	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}

	/**
	 * <p>Getter for the field <code>address2</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAddress2()
	{
		return address2;
	}

	/**
	 * <p>Setter for the field <code>address2</code>.</p>
	 *
	 * @param address2 a {@link java.lang.String} object.
	 */
	public void setAddress2(String address2)
	{
		this.address2 = address2;
	}

	/**
	 * <p>Getter for the field <code>ccn</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCcn()
	{
		return ccn;
	}

	/**
	 * <p>Setter for the field <code>ccn</code>.</p>
	 *
	 * @param ccn a {@link java.lang.String} object.
	 */
	public void setCcn(String ccn)
	{
		this.ccn = ccn;
	}

	/**
	 * <p>Getter for the field <code>ccnLimit</code>.</p>
	 *
	 * @return a int.
	 */
	public int getCcnLimit()
	{
		return ccnLimit;
	}

	/**
	 * <p>Setter for the field <code>ccnLimit</code>.</p>
	 *
	 * @param ccnLimit a int.
	 */
	public void setCcnLimit(int ccnLimit)
	{
		this.ccnLimit = ccnLimit;
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
	 * <p>Setter for the field <code>firstName</code>.</p>
	 *
	 * @param firstName a {@link java.lang.String} object.
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
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
	 * <p>Setter for the field <code>lastName</code>.</p>
	 *
	 * @param lastName a {@link java.lang.String} object.
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * <p>getPhoneNumber.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPhoneNumber()
	{
		return phone;
	}

	/**
	 * <p>setPhoneNumber.</p>
	 *
	 * @param phone a {@link java.lang.String} object.
	 */
	public void setPhoneNumber(String phone)
	{
		this.phone = phone;
	}

	/**
	 * <p>Getter for the field <code>salary</code>.</p>
	 *
	 * @return a int.
	 */
	public int getSalary()
	{
		return salary;
	}

	/**
	 * <p>Setter for the field <code>salary</code>.</p>
	 *
	 * @param salary a int.
	 */
	public void setSalary(int salary)
	{
		this.salary = salary;
	}

	/**
	 * <p>Getter for the field <code>ssn</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSsn()
	{
		return ssn;
	}

	/**
	 * <p>Setter for the field <code>ssn</code>.</p>
	 *
	 * @param ssn a {@link java.lang.String} object.
	 */
	public void setSsn(String ssn)
	{
		this.ssn = ssn;
	}

	/**
	 * <p>Getter for the field <code>startDate</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * <p>Setter for the field <code>startDate</code>.</p>
	 *
	 * @param startDate a {@link java.lang.String} object.
	 */
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
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
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param id a int.
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * <p>Getter for the field <code>manager</code>.</p>
	 *
	 * @return a int.
	 */
	public int getManager()
	{
		return this.manager;
	}

	/**
	 * <p>Getter for the field <code>disciplinaryActionDate</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDisciplinaryActionDate()
	{
		return this.disciplinaryActionDate;
	}

	/**
	 * <p>Getter for the field <code>disciplinaryActionNotes</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDisciplinaryActionNotes()
	{
		return this.disciplinaryActionNotes;
	}

	/**
	 * <p>Getter for the field <code>personalDescription</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPersonalDescription()
	{
		return this.personalDescription;
	}
}
