package org.owasp.webgoat.session;

public class ValidationException extends Exception
{
	public ValidationException()
	{
		super();
	}
	
	public ValidationException(String message)
	{
		super(message);
	}
}
