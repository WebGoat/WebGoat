
package org.owasp.webgoat.util;

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
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 */
public class ExecResults
{

	/**
	 * Description of the Field
	 */
	public final static int BADRETURNCODE = 2;

	/**
	 * Description of the Field
	 */
	public final static int THROWABLE = 1;

	private String myCommand;

	private boolean myError = false;

	private int myErrorType = 0;

	private String myErrors = null;

	private String myInput;

	private boolean myInterrupted = false;

	private String myOutput = null;

	private int myReturnCode = 0;

	private int mySuccessCode;

	private Throwable myThrowable = null;

	private int myTimeout;

	/**
	 * Constructor for the ExecResults object
	 * 
	 * @param command
	 *            Description of the Parameter
	 * @param input
	 *            Description of the Parameter
	 * @param successCode
	 *            Description of the Parameter
	 * @param timeout
	 *            Description of the Parameter
	 */
	public ExecResults(String command, String input, int successCode, int timeout)
	{
		myCommand = command.trim();
		myInput = input.trim();
		mySuccessCode = successCode;
		myTimeout = timeout;
	}

	/**
	 * Description of the Method
	 * 
	 * @param haystack
	 *            Description of the Parameter
	 * @param needle
	 *            Description of the Parameter
	 * @param fromIndex
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private boolean contains(String haystack, String needle, int fromIndex)
	{
		return (haystack.trim().toLowerCase().indexOf(needle.trim().toLowerCase(), fromIndex) != -1);
	}

	/**
	 * Description of the Method
	 * 
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public boolean errorsContains(String value)
	{
		return (errorsContains(value, 0));
	}

	/**
	 * Description of the Method
	 * 
	 * @param value
	 *            Description of the Parameter
	 * @param fromIndex
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public boolean errorsContains(String value, int fromIndex)
	{
		return (contains(myErrors, value, fromIndex));
	}

	/**
	 * Gets the error attribute of the ExecResults object
	 * 
	 * @return The error value
	 */
	public boolean getError()
	{
		return (myError);
	}

	/**
	 * Gets the errorMessage attribute of the ExecResults object
	 * 
	 * @return The errorMessage value
	 */
	public String getErrorMessage()
	{
		switch (getErrorType())
		{
			case THROWABLE:
				return ("Exception: " + myThrowable.getMessage());

			case BADRETURNCODE:
				return ("Bad return code (expected " + mySuccessCode + ")");

			default:
				return ("Unknown error");
		}
	}

	/**
	 * Gets the errorType attribute of the ExecResults object
	 * 
	 * @return The errorType value
	 */
	public int getErrorType()
	{
		return (myErrorType);
	}

	/**
	 * Gets the errors attribute of the ExecResults object
	 * 
	 * @return The errors value
	 */
	public String getErrors()
	{
		return (myErrors);
	}

	/**
	 * Gets the interrupted attribute of the ExecResults object
	 * 
	 * @return The interrupted value
	 */
	public boolean getInterrupted()
	{
		return (myInterrupted);
	}

	/**
	 * Gets the output attribute of the ExecResults object
	 * 
	 * @return The output value
	 */
	public String getOutput()
	{
		return (myOutput);
	}

	/**
	 * Gets the returnCode attribute of the ExecResults object
	 * 
	 * @return The returnCode value
	 */
	public int getReturnCode()
	{
		return (myReturnCode);
	}

	/**
	 * Gets the throwable attribute of the ExecResults object
	 * 
	 * @return The throwable value
	 */
	public Throwable getThrowable()
	{
		return (myThrowable);
	}

	/**
	 * Description of the Method
	 * 
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public boolean outputContains(String value)
	{
		return (outputContains(value, 0));
	}

	/**
	 * Description of the Method
	 * 
	 * @param value
	 *            Description of the Parameter
	 * @param fromIndex
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public boolean outputContains(String value, int fromIndex)
	{
		return (contains(myOutput, value, fromIndex));
	}

	/**
	 * Sets the error attribute of the ExecResults object
	 * 
	 * @param value
	 *            The new error value
	 */
	public void setError(int value)
	{
		myError = true;
		myErrorType = value;
	}

	/**
	 * Sets the errors attribute of the ExecResults object
	 * 
	 * @param errors
	 *            The new errors value
	 */
	public void setErrors(String errors)
	{
		myErrors = errors.trim();
	}

	/**
	 * Sets the interrupted attribute of the ExecResults object
	 */
	public void setInterrupted()
	{
		myInterrupted = true;
	}

	/**
	 * Sets the output attribute of the ExecResults object
	 * 
	 * @param value
	 *            The new output value
	 */
	public void setOutput(String value)
	{
		myOutput = value.trim();
	}

	/**
	 * Sets the returnCode attribute of the ExecResults object
	 * 
	 * @param value
	 *            The new returnCode value
	 */
	public void setReturnCode(int value)
	{
		myReturnCode = value;
	}

	/**
	 * Sets the throwable attribute of the ExecResults object
	 * 
	 * @param value
	 *            The new throwable value
	 */
	public void setThrowable(Throwable value)
	{
		setError(THROWABLE);
		myThrowable = value;
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public String toString()
	{
		String sep = System.getProperty("line.separator");
		StringBuffer value = new StringBuffer();
		value.append("ExecResults for \'" + myCommand + "\'" + sep);

		if ((myInput != null) && !myInput.equals(""))
		{
			value.append(sep + "Input..." + sep + myInput + sep);
		}

		if ((myOutput != null) && !myOutput.equals(""))
		{
			value.append(sep + "Output..." + sep + myOutput + sep);
		}

		if ((myErrors != null) && !myErrors.equals(""))
		{
			value.append(sep + "Errors..." + sep + myErrors + sep);
		}

		value.append(sep);

		if (myInterrupted)
		{
			value.append("Command timed out after " + (myTimeout / 1000) + " seconds " + sep);
		}

		value.append("Returncode: " + myReturnCode + sep);

		if (myError)
		{
			value.append(getErrorMessage() + sep);
		}

		return (value.toString());
	}
}
