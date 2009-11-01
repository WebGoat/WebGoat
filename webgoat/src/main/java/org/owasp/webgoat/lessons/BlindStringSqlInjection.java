package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Chuck Willis <a href="http://www.securityfoundry.com">Chuck's web
 *         site</a> (this lesson is heavily based on Bruce Mayhews' SQL
 *         Injection lesson
 * @created January 14, 2005
 */
public class BlindStringSqlInjection extends LessonAdapter
{

	public final static A MANDIANT_LOGO = new A().setHref("http://www.mandiant.com").addElement(new IMG("images/logos/mandiant.png").setAlt("MANDIANT").setBorder(0).setHspace(0).setVspace(0));
	
    private final static String ACCT_NUM = "account_number";

    private final static String TARGET_CC_NUM = "4321432143214321";

    /**
     * Description of the Method
     * 
     * @param s
     *                Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
	ElementContainer ec = new ElementContainer();

	try
	{
	    Connection connection = DatabaseUtilities.getConnection(s);

	    ec.addElement(new P().addElement("Enter your Account Number: "));

	    String accountNumber = s.getParser().getRawParameter(ACCT_NUM, "101");
	    Input input = new Input(Input.TEXT, ACCT_NUM, accountNumber.toString());
	    ec.addElement(input);

	    Element b = ECSFactory.makeButton("Go!");
	    ec.addElement(b);

	    String query = "SELECT * FROM user_data WHERE userid = " + accountNumber;
	    String answer_query;
//	    if (runningOnWindows())
//	    {
//		answer_query = "SELECT TOP 1 first_name FROM user_data WHERE userid = "
//			+ TARGET_CC_NUM;
//	    } else
//	    {
		answer_query = "SELECT name FROM pins WHERE cc_number = '" + TARGET_CC_NUM +"'";
//	    }

	    try
	    {
		Statement answer_statement = connection.createStatement(
			ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet answer_results = answer_statement.executeQuery(answer_query);
		answer_results.first();
		System.out.println("Account: " + accountNumber );
		System.out.println("Answer : " + answer_results.getString(1));
		if (accountNumber.toString().equals(answer_results.getString(1)))
		{
		    makeSuccess(s);
		} else
		{

		    Statement statement = connection.createStatement(
			    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		    ResultSet results = statement.executeQuery(query);

		    if ((results != null) && (results.first() == true))
		    {
			ec.addElement(new P().addElement("Account number is valid"));
		    } else
		    {
			ec.addElement(new P().addElement("Invalid account number"));
		    }
		}
	    }
	    catch (SQLException sqle)
	    {
		ec.addElement(new P().addElement("An error occurred, please try again."));
		
		// comment out two lines below
		ec.addElement(new P().addElement(sqle.getMessage()));
		sqle.printStackTrace();
	    
	    }
	}
	catch (Exception e)
	{
	    s.setMessage("Error generating " + this.getClass().getName());
	    e.printStackTrace();
	}

	return (ec);
    }

    /**
     * Gets the category attribute of the SqlInjection object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
	return Category.INJECTION;
    }

    /**
     * Gets the credits attribute of the AbstractLesson object
     * 
     * @return The credits value
     */
    public Element getCredits()
    {
    	return super.getCustomCredits("Created by Chuck Willis&nbsp;", MANDIANT_LOGO);
    }

    /**
     * 
     * Determines the OS that WebGoat is running on. Needed because different DB
     * backends are used on the different OSes (Access on Windows, InstantDB on
     * others)
     * 
     * @return true if running on Windows, false otherwise
     */
//    private boolean runningOnWindows()
//    {
//	String os = System.getProperty("os.name", "Windows");
//	if (os.toLowerCase().indexOf("window") != -1)
//	{
//	    return true;
//	} else
//	{
//	    return false;
//	}
//    }

    /**
     * Gets the hints attribute of the DatabaseFieldScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
	List<String> hints = new ArrayList<String>();
//	if (runningOnWindows())
//	{
	    hints
		    .add("Compound SQL statements can be made by joining multiple tests with keywords like AND and OR. "
			    + "Create a SQL statement that you can use as a true/false test and then "
			    + "select the first character of the target element and do a start narrowing "
			    + "down the character using > and <"
			    + "<br><br>The backend database is HSQLDB.  Keep that in mind if you research SQL functions "
			    + "on the Internet since different databases use some different functions and syntax."
			    
			    + "<br><br>You can find more about HSQLDB's SQL Syntax at "
			    + "<a href='http://hsqldb.org/doc/guide/ch09.html'>http://hsqldb.org/doc/guide/ch09.html</a>.  "
			    + "Pay attention to the section titled \"String built-in Functions / Stored Procedures\".");

	    hints
		    .add("The application is taking your input and inserting it at the end of a pre-formed SQL command. "
			    + "You will need to make use of the following SQL contstructs: "
			    
			    + "<br><br>AND - combine the logic of the normal query with a boolean result"		
			    + "<br><br>( and ) - group subexpressions so they evaluate properly"
			    + "<br><br>SELECT - make a subquery for your target data and get a string "
			    + "<br><br>SUBSTRING(string, start, length) - returns a "
			    + "substring of string starting at the start character and going for length characters "
			    + "<br><br>&gt and = and &lt - once you have a character's value, compare it to a choosen one"
			    + "<br><br>You can find more about HSQLDB's SQL Syntax at "
			    + "<a href='http://hsqldb.org/doc/guide/ch09.html'>http://hsqldb.org/doc/guide/ch09.html</a>");
			
	    hints.add("This is the code for the query being built and issued by WebGoat:<br><br> "
			    + "\"SELECT * FROM user_data WHERE userid = \" + accountNumber ");
	    
	    hints
	    	.add("Here is an example for another table:"
	    		+ "<br><br>In the table <i>user_data</i>, is the first character of the <i>first_name</i> for the record with a <i>cc_number</i> of "
			    + "<i>333498703333</i>"
			    + " greater than 'M'? "
			    + "<br><br>101 AND (SUBSTRING((SELECT first_name FROM user_data WHERE cc_number='"
			    + "333498703333"
			    + "'), 1, 1) &gt 'M' ); "
			    + "<br><br>If you get back that account number is valid, then yes.  If get back that the number is "
			    + "invalid then answer is no.");
	    hints
		    .add("Partial Solution for First Character:"
		    	+ "<br><br>Is the first character of the <i>name</i> of the record with a <i>cc_number</i> of <i>"
			    + TARGET_CC_NUM
			    + "</i> less than 'M'? "
			    + "<br><br>101 AND (SUBSTRING((SELECT name FROM pins WHERE cc_number='"
			    + TARGET_CC_NUM
			    + "'), 1, 1) &lt 'M' ); "
			    + "<br><br>If you get back that account number is valid, then yes.  If get back that the number is "
			    + "invalid then answer is no.");
	    hints
		    .add("Another Part of Solution for First Character:"
		    	+ "<br><br>Is the first character of the <i>name</i> of the record with a <i>cc_number</i> of <i>"
			    + TARGET_CC_NUM
			    + "</i> less than 'H'? "
			    + "<br><br>101 AND (SUBSTRING((SELECT name FROM pins WHERE cc_number='"
			    + TARGET_CC_NUM
			    + "'), 1, 1) &lt 'H' ); "
			    + "<br><br>If you get back that account number is valid, then yes.  If get back that the number is "
			    + "invalid then answer is no.");
	    hints
	    	.add("Partial Solution for Second Character:"
	    		+ "<br><br>Is the second character of the <i>name</i> of the record with a <i>cc_number</i> of <i>"
			    + TARGET_CC_NUM
			    + "</i> greater than 'k'? "
			    + "<br><br>101 AND (SUBSTRING((SELECT name FROM pins WHERE cc_number='"
			    + TARGET_CC_NUM
			    + "'), 2, 1) &gt 'k' ); "
			    + "<br><br>If you get back that account number is valid, then yes.  If get back that the number is "
			    + "invalid then answer is no.");
	    

	return hints;
    }

    /**
     * Gets the instructions attribute of the SqlInjection object
     * 
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
	String instructions = "The form below allows a user to enter an account number and determine if "
		+ "it is valid or not.  Use this form to develop a true / false test check other entries in the database.  "
		+ "<br><br>Reference Ascii Values: 'A' = 65   'Z' = 90   'a' = 97   'z' = 122 "
		+ "<br><br>The goal is to find the value of "
		+ "the field <b>name</b> in table <b>pins</b> for the row with the <b>cc_number</b> of <b>"
		+ TARGET_CC_NUM
		+ "</b>.  The field is of type varchar, which is a string." 
		+ "<br><br>Put the discovered name in the form to pass the lesson.  Only the discovered name "
		+ "should be put into the form field, paying close attention to the spelling and capitalization.";

	return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(95);

    protected Integer getDefaultRanking()
    {
	return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the DatabaseFieldScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
	return ("Blind String SQL Injection");
    }

    /**
     * Constructor for the DatabaseFieldScreen object
     * 
     * @param s
     *                Description of the Parameter
     */
    public void handleRequest(WebSession s)
    {
	try
	{
	    super.handleRequest(s);
	}
	catch (Exception e)
	{
	    System.out.println("Exception caught: " + e);
	    e.printStackTrace(System.out);
	}
    }
}
