
package org.owasp.webgoat.session;

import java.io.Serializable;


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
 */
public class Employee implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1901957360367218399L;

	public final static String EMPLOYEE_ROLE = "employee";

	public final static String MANAGER_ROLE = "manager";

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
	public Employee()
	{
	}

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

	public String getAddress1()
	{
		return address1;
	}

	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}

	public String getAddress2()
	{
		return address2;
	}

	public void setAddress2(String address2)
	{
		this.address2 = address2;
	}

	public String getCcn()
	{
		return ccn;
	}

	public void setCcn(String ccn)
	{
		this.ccn = ccn;
	}

	public int getCcnLimit()
	{
		return ccnLimit;
	}

	public void setCcnLimit(int ccnLimit)
	{
		this.ccnLimit = ccnLimit;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getPhoneNumber()
	{
		return phone;
	}

	public void setPhoneNumber(String phone)
	{
		this.phone = phone;
	}

	public int getSalary()
	{
		return salary;
	}

	public void setSalary(int salary)
	{
		this.salary = salary;
	}

	public String getSsn()
	{
		return ssn;
	}

	public void setSsn(String ssn)
	{
		this.ssn = ssn;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return this.title;
	}

	public int getManager()
	{
		return this.manager;
	}

	public String getDisciplinaryActionDate()
	{
		return this.disciplinaryActionDate;
	}

	public String getDisciplinaryActionNotes()
	{
		return this.disciplinaryActionNotes;
	}

	public String getPersonalDescription()
	{
		return this.personalDescription;
	}
}
