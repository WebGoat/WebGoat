
package org.owasp.webgoat.lessons.ClientSideFiltering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.Select;
import org.apache.ecs.html.Style;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.jsp.jsp_include;
import org.apache.ecs.xhtml.style;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.SequentialLessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;


public class ClientSideFiltering extends SequentialLessonAdapter
{

	private final static String ANSWER = "answer";

	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	protected Element createMainContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		try
		{

			ec.addElement(new Script().setSrc("javascript/clientSideFiltering.js"));

			Input input = new Input(Input.HIDDEN, "userID", 102);

			input.setID("userID");

			ec.addElement(input);

			style sty = new style();
			sty.addElement("#lesson_wrapper {height: 435px;width: 500px;}"
					+ "#lesson_header {background-image: url(lessons/Ajax/images/lesson1_header.jpg);"
					+ "width: 490px;padding-right: 10px;padding-top: 60px;background-repeat: no-repeat;}"
					+ ".lesson_workspace {background-image: url(lessons/Ajax/images/lesson1_workspace.jpg);"
					+ "width: 489px;height: 325px;padding-left: 10px;padding-top: 10px;background-repeat: no-repeat;}");

			ec.addElement(sty);

			Div wrapperDiv = new Div();
			wrapperDiv.setID("lesson_wrapper");

			Div headerDiv = new Div();
			headerDiv.setID("lesson_header");

			Div workspaceDiv = new Div();
			workspaceDiv.setClass("lesson_workspace");

			wrapperDiv.addElement(headerDiv);
			wrapperDiv.addElement(workspaceDiv);

			ec.addElement(wrapperDiv);

			workspaceDiv.addElement(new BR());
			workspaceDiv.addElement(new BR());

			workspaceDiv.addElement(new P().addElement("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Select user:"));

			workspaceDiv.addElement(createDropDown());

			workspaceDiv.addElement(new P());

			Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

			t.setID("hiddenEmployeeRecords");
			t.setStyle("display: none");

			workspaceDiv.addElement(t);

			t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

			TR tr = new TR();
			tr.addElement(new TD().addElement("UserID"));
			tr.addElement(new TD().addElement("First Name"));
			tr.addElement(new TD().addElement("Last Name"));
			tr.addElement(new TD().addElement("SSN"));
			tr.addElement(new TD().addElement("Salary"));
			t.addElement(tr);
			tr = new TR();
			tr.setID("employeeRecord");
			t.addElement(tr);

			workspaceDiv.addElement(t);

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	/**
	 * Gets the category attribute of the RoleBasedAccessControl object
	 * 
	 * @return The category value
	 */

	protected ElementContainer doStage1(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		StringBuffer answerString = null;
		int answer = 0;

		try
		{
			answerString = new StringBuffer(s.getParser().getStringParameter(ANSWER, ""));
			answer = Integer.parseInt(answerString.toString());
		} catch (NumberFormatException e)
		{

			// e.printStackTrace();
		}

		if (answer == 450000)
		{

			getLessonTracker(s).setStage(2);
			s.setMessage("Stage 1 completed.");

			// Redirect user to Stage2 content.
			ec.addElement(doStage2(s));
		}
		else
		{
			ec.addElement(stage1Content(s));
		}

		return ec;

	}

	protected Element doStage2(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		/**
		 * They pass iff:
		 * 
		 * 1. If the DOMXSS.js file contains the lines "escapeHTML(name)"
		 */
		String file = s.getWebResource("lessons/Ajax/clientSideFiltering.jsp");
		String content = getFileContent(file);

		if (content.indexOf("[Managers/Manager/text()") != -1)
		{
			makeSuccess(s);
			ec.addElement(stage2Content(s));
		}
		else
		{
			ec.addElement(stage2Content(s));
		}

		return ec;
	}

	protected ElementContainer stage1Content(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		try
		{

			ec.addElement(createMainContent(s));

			Table t1 = new Table().setCellSpacing(0).setCellPadding(2);

			if (s.isColor())
			{
				t1.setBorder(1);
			}

			TR tr = new TR();
			tr.addElement(new TD().addElement("What is Neville Bartholomew's salary? "));
			tr.addElement(new TD(new Input(Input.TEXT, ANSWER, "")));
			Element b = ECSFactory.makeButton("Submit Answer");
			tr.addElement(new TD(b).setAlign("LEFT"));
			t1.addElement(tr);

			ec.addElement(t1);

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return ec;
	}

	protected ElementContainer stage2Content(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		try
		{

			ec.addElement(createMainContent(s));

			ec.addElement(new BR());
			ec.addElement(new BR());

			Table t1 = new Table().setCellSpacing(0).setCellPadding(2);

			if (s.isColor())
			{
				t1.setBorder(1);
			}

			TR tr = new TR();
			/*
			 * tr.addElement(new TD() .addElement("Press 'Submit' when you believe you have
			 * completed the lesson."));
			 */
			Element b = ECSFactory.makeButton("Click here when you believe you have completed the lesson.");
			tr.addElement(new TD(b).setAlign("CENTER"));
			t1.addElement(tr);

			ec.addElement(t1);

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return ec;
	}

	protected Select createDropDown()
	{
		Select select = new Select("UserSelect");

		select.setID("UserSelect");

		org.apache.ecs.html.Option option = new org.apache.ecs.html.Option("Choose Employee", "0", "Choose Employee");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Larry Stooge", "101", "Larry Stooge");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Curly Stooge", "103", "Curly Stooge");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Eric Walker", "104", "Eric Walker");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Tom Cat", "105", "Tom Cat");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Jerry Mouse", "106", "Jerry Mouse");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("David Giambi", "107", "David Giambi");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Bruce McGuirre", "108", "Bruce McGuirre");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Sean Livingston", "109", "Sean Livingston");

		select.addElement(option);

		option = new org.apache.ecs.html.Option("Joanne McDougal", "110", "Joanne McDougal");

		select.addElement(option);

		select.setOnChange("selectUser()");

		select.setOnFocus("fetchUserData()");

		return select;

	}

	protected Category getDefaultCategory()
	{
		return Category.AJAX_SECURITY;
	}

	/**
	 * Gets the hints attribute of the RoleBasedAccessControl object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();

		hints
				.add("Stage 1: The information displayed when an employee is choosen from the drop down menu is stored on the client side.");

		hints.add("Stage 1: Use Firebug to find where the information is stored on the client side.");

		hints
				.add("Stage 1: Examine the hidden table to see if there is anyone listed who is not in the drop down menu.");

		hints.add("Stage 1: Look in the last row of the hidden table.");

		hints
				.add("Stage 1: You can access the server directly <a href = \"/WebGoat/lessons/Ajax/clientSideFiltering.jsp?userId=102\">here </a>"
						+ "to see what results are being returned");

		hints.add("Stage 2: The server uses an XPath query agasinst an XML database.");

		hints.add("Stage 2: The query currently returns all of the contents of the database.");

		hints
				.add("Stage 2: The query should only return the information of employees who are managed by Moe Stooge, who's userID is 102");

		hints.add("Stage 2: Try using a filter operator.");

		hints.add("Stage 2: Your filter operator should look something like: [Managers/Manager/text()=");

		return hints;

	}

	public String getInstructions(WebSession s)
	{
		String instructions = "";

		if (getLessonTracker(s).getStage() == 1)
		{
			instructions = "STAGE 1:\tYou are Moe Stooge, CSO of Goat Hills Financial. "
					+ "You have access to everyone in the company's information, except the CEO, "
					+ "Neville Bartholomew.  Or at least you shouldn't have access to the CEO's information."
					+ "  For this exercise, "
					+ "examine the contents of the page to see what extra information you can find.";
		}
		else if (getLessonTracker(s).getStage() == 2)
		{
			instructions = "STAGE 2:\tNow, fix the problem.  Modify the server to only return "
					+ "results that Moe Stooge is allowed to see.";
		}
		return (instructions);
	}

	private final static Integer DEFAULT_RANKING = new Integer(10);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the resources attribute of the RoleBasedAccessControl object
	 * 
	 * @param rl
	 *            Description of the Parameter
	 * @return The resources value
	 */

	/**
	 * Gets the role attribute of the RoleBasedAccessControl object
	 * 
	 * @param user
	 *            Description of the Parameter
	 * @return The role value
	 */

	/**
	 * Gets the title attribute of the AccessControlScreen object
	 * 
	 * @return The title value
	 */

	public String getTitle()
	{
		return ("LAB: Client Side Filtering");
	}

	private String getFileContent(String content)
	{
		BufferedReader is = null;
		StringBuffer sb = new StringBuffer();

		try
		{
			is = new BufferedReader(new FileReader(new File(content)));
			String s = null;

			while ((s = is.readLine()) != null)
			{
				sb.append(s);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				} catch (IOException ioe)
				{

				}
			}
		}

		return sb.toString();
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}

}
