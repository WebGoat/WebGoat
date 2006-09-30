package org.owasp.webgoat.session;

import java.io.Serializable;

public class EmployeeStub implements Serializable
{
	private int id;
	private String firstName;
	private String lastName;
	private String role;
	
	public EmployeeStub(int id, String firstName, String lastName)
	{
		this(id, firstName, lastName, Employee.EMPLOYEE_ROLE);
	}

	public EmployeeStub(int id, String firstName, String lastName, String role)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public int getId()
	{
		return id;
	}

	public String getLastName()
	{
		return lastName;
	}
	
	public String getRole()
	{
		return role;
	}
}
