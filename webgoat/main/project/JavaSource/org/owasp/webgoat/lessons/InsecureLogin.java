
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Option;
import org.apache.ecs.html.Select;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.xhtml.style;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;


public class InsecureLogin extends SequentialLessonAdapter
{

	private final static String USER = "clear_user";
	private final static String PASSWORD = "clear_pass";
	private final static String ANSWER = "clear_answer";
	private final static String YESNO = "yesno";
	private final static String PROTOCOL = "protocol";

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	@Override
	protected Element doStage1(WebSession s) throws Exception
	{
		String answer = s.getParser().getStringParameter(ANSWER, "");
		if (answer.equals("sniffy"))
		{
			s.setMessage("You completed Stage 1!");
			getLessonTracker(s).setStage(2);
		}
		return createMainContent(s);
	}

	@Override
	protected Element doStage2(WebSession s) throws Exception
	{
		String protocol = s.getParser().getStringParameter(PROTOCOL, "");
		String yesno = s.getParser().getStringParameter(YESNO, "");

		if (yesno.equals("No") && protocol.equals("TLS"))
		{
			makeSuccess(s);
		}

		return createMainContent(s);
	}

	/**
	 * Creation of the main content
	 * 
	 * @param s
	 * @return Element
	 */
	protected Element createMainContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			style sty = new style();

			sty
					.addElement("#lesson_wrapper {height: 435px;width: 500px;}#lesson_header {background-image: url(lessons/DBSQLInjection/images/lesson1_header.jpg);width: 490px;padding-right: 10px;padding-top: 60px;background-repeat: no-repeat;}.lesson_workspace {background-image: url(lessons/DBSQLInjection/images/lesson1_workspace.jpg);width: 489px;height: 325px;padding-left: 10px;padding-top: 10px;background-repeat: no-repeat;}		.lesson_text {height: 240px;width: 460px;padding-top: 5px;}			#lesson_buttons_bottom {height: 20px;width: 460px;}			#lesson_b_b_left {width: 300px;float: left;}			#lesson_b_b_right input {width: 100px;float: right;}			.lesson_title_box {height: 20px;width: 420px;padding-left: 30px;}			.lesson_workspace { }			.lesson_txt_10 {font-family: Arial, Helvetica, sans-serif;font-size: 10px;}			.lesson_text_db {color: #0066FF}			#lesson_login {background-image: url(lessons/DBSQLInjection/images/lesson1_loginWindow.jpg);height: 124px;width: 311px;background-repeat: no-repeat;padding-top: 30px;margin-left: 80px;margin-top: 50px;text-align: center;}			#lesson_login_txt {font-family: Arial, Helvetica, sans-serif;font-size: 12px;text-align: center;}			#lesson_search {background-image: url(lessons/DBSQLInjection/images/lesson1_SearchWindow.jpg);height: 124px;width: 311px;background-repeat: no-repeat;padding-top: 30px;margin-left: 80px;margin-top: 50px;text-align: center;}");
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

			String user = s.getParser().getStringParameter(USER, "");
			String password = s.getParser().getStringParameter(PASSWORD, "");
			if (!(user + password).equals("") && correctLogin(user, password, s))
			{
				workspaceDiv.addElement(createSuccessfulLoginContent(s, user));
			}
			else
			{
				workspaceDiv.addElement(createLogInContent());
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	/**
	 * Create content for logging in
	 * 
	 * @param ec
	 */
	private Element createLogInContent()
	{
		ElementContainer ec = new ElementContainer();
		Div loginDiv = new Div();
		loginDiv.setID("lesson_login");

		Table table = new Table();
		table.addAttribute("align='center'", 0);
		TR tr1 = new TR();
		TD td1 = new TD();
		TD td2 = new TD();
		td1.addElement(new StringElement("Enter your name: "));
		td2.addElement(new Input(Input.TEXT, USER).setValue("Jack").setReadOnly(true));
		tr1.addElement(td1);
		tr1.addElement(td2);

		TR tr2 = new TR();
		TD td3 = new TD();
		TD td4 = new TD();
		td3.addElement(new StringElement("Enter your password: "));
		td4.addElement(new Input(Input.PASSWORD, PASSWORD).setValue("sniffy").setReadOnly(true));
		tr2.addElement(td3);
		tr2.addElement(td4);

		TR tr3 = new TR();
		TD td5 = new TD();
		td5.setColSpan(2);
		td5.setAlign("center");

		td5.addElement(new Input(Input.SUBMIT, "Submit", "Submit"));
		tr3.addElement(td5);

		table.addElement(tr1);
		table.addElement(tr2);
		table.addElement(tr3);
		loginDiv.addElement(table);
		ec.addElement(loginDiv);
		return ec;

	}

	/**
	 * Gets the category attribute of the ForgotPassword object
	 * 
	 * @return The category value
	 */
	protected Category getDefaultCategory()
	{

		return Category.INSECURE_COMMUNICATION;
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();

		hints.add("Stage 1: Use a sniffer to record " + "the traffic");
		hints.add("Stage 1: What Protocol does the request use?");
		hints.add("Stage 1: What kind of request is started when " + "you click on the button?");
		hints.add("Stage 1: Take a closer look at the HTTP Post request in " + "your sniffer");
		hints.add("Stage 1: The password field has the name clear_pass");

		return hints;
	}

	private final static Integer DEFAULT_RANKING = new Integer(100);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the HelloScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Insecure Login");
	}

	@Override
	public String getInstructions(WebSession s)
	{
		int stage = getLessonTracker(s).getStage();
		String instructions = "";
		instructions = "<b>For this lesson you need to " + "have a server client setup. Please refer to the"
				+ "Tomcat Configuration in the Introduction section.</b><br><br> Stage" + stage + ": ";
		if (stage == 1)
		{
			instructions += "In this stage you have to sniff the "
					+ "password. And answer the question after the login.";
		}
		if (stage == 2)
		{
			instructions += "Now you have to change to a secure " + "connection. The URL should start with https:// "
					+ "If your browser is complaining about the certificate just "
					+ "ignore it. Sniff again the traffic and answer the" + " questions";
		}
		return instructions;
	}

	/**
	 * See if the password and corresponding user is valid
	 * 
	 * @param userName
	 * @param password
	 * @param s
	 * @return true if the password was correct
	 */
	private boolean correctLogin(String userName, String password, WebSession s)
	{
		Connection connection = null;
		try
		{
			connection = DatabaseUtilities.getConnection(s);
			String query = "SELECT * FROM user_data_tan WHERE first_name = ? AND password = ?";
			PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																			ResultSet.CONCUR_READ_ONLY);
			prepStatement.setString(1, userName);
			prepStatement.setString(2, password);

			ResultSet results = prepStatement.executeQuery();

			if ((results != null) && (results.first() == true)) {

			return true;

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (connection != null)
				{
					connection.close();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return false;

	}

	/**
	 * Create content after a successful login
	 * 
	 * @param s
	 * @param ec
	 */
	private Element createSuccessfulLoginContent(WebSession s, String user)
	{
		ElementContainer ec = new ElementContainer();

		String userDataStyle = "margin-top:50px;";

		Div userDataDiv = new Div();
		userDataDiv.setStyle(userDataStyle);
		userDataDiv.addAttribute("align", "center");
		Table table = new Table();
		table.addAttribute("cellspacing", 10);
		table.addAttribute("cellpadding", 5);

		table.addAttribute("align", "center");
		TR tr1 = new TR();
		TR tr2 = new TR();
		TR tr3 = new TR();
		TR tr4 = new TR();
		tr1.addElement(new TD("<b>Firstname:</b>"));
		tr1.addElement(new TD(user));

		try
		{
			ResultSet results = getUser(user, s);
			results.first();

			tr2.addElement(new TD("<b>Lastname:</b>"));
			tr2.addElement(new TD(results.getString("last_name")));

			tr3.addElement(new TD("<b>Credit Card Type:</b>"));
			tr3.addElement(new TD(results.getString("cc_type")));

			tr4.addElement(new TD("<b>Credit Card Number:</b>"));
			tr4.addElement(new TD(results.getString("cc_number")));

		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		table.addElement(tr1);
		table.addElement(tr2);
		table.addElement(tr3);
		table.addElement(tr4);

		userDataDiv.addElement(table);
		ec.addElement(userDataDiv);
		ec.addElement(createLogoutLink());

		int stage = getLessonTracker(s).getStage();
		if (stage == 1)
		{
			ec.addElement(createPlaintextQuestionContent());
		}
		else if (stage == 2)
		{
			ec.addElement(createSSLQuestionContent());
		}

		return ec;
	}

	private Element createPlaintextQuestionContent()
	{
		ElementContainer ec = new ElementContainer();
		Div div = new Div();
		div.addAttribute("align", "center");
		div.addElement(new BR());
		div.addElement(new BR());
		div.addElement("What was the password?");
		div.addElement(new Input(Input.TEXT, ANSWER));
		div.addElement(new Input(Input.SUBMIT, "Submit", "Submit"));
		ec.addElement(div);
		return ec;
	}

	private Element createSSLQuestionContent()
	{
		ElementContainer ec = new ElementContainer();
		Table selectTable = new Table();
		TR tr1 = new TR();
		TD td1 = new TD();
		TD td2 = new TD();
		TR tr2 = new TR();
		TD td3 = new TD();
		TD td4 = new TD();
		tr1.addElement(td1);
		tr1.addElement(td2);
		tr2.addElement(td3);
		tr2.addElement(td4);
		selectTable.addElement(tr1);
		selectTable.addElement(tr2);

		Div div = new Div();
		div.addAttribute("align", "center");
		ec.addElement(new BR());
		ec.addElement(new BR());

		td1.addElement("Is the password still transmited in plaintext?");
		Select yesNoSelect = new Select();
		yesNoSelect.setName(YESNO);
		Option yesOption = new Option();
		yesOption.addElement("Yes");
		Option noOption = new Option();
		noOption.addElement("No");
		yesNoSelect.addElement(yesOption);
		yesNoSelect.addElement(noOption);
		td2.addElement(yesNoSelect);

		td3.addElement("Which protocol is used for the transmission?");
		Select protocolSelect = new Select();
		protocolSelect.setName(PROTOCOL);
		Option httpOption = new Option();
		httpOption.addElement("HTTP");
		Option tcpOption = new Option();
		tcpOption.addElement("UDP");
		Option ipsecOption = new Option();
		ipsecOption.addElement("IPSEC");
		Option msnmsOption = new Option();
		msnmsOption.addElement("MSNMS");
		Option tlsOption = new Option();
		tlsOption.addElement("TLS");
		protocolSelect.addElement(httpOption);
		protocolSelect.addElement(ipsecOption);
		protocolSelect.addElement(msnmsOption);
		protocolSelect.addElement(tcpOption);
		protocolSelect.addElement(tlsOption);
		td4.addElement(protocolSelect);

		div.addElement(selectTable);

		div.addElement(new Input(Input.SUBMIT, "Submit", "Submit"));
		ec.addElement(div);
		return ec;
	}

	/**
	 * Get a user by its name
	 * 
	 * @param user
	 * @param s
	 * @return ResultSet containing the user
	 */
	private ResultSet getUser(String user, WebSession s)
	{
		Connection connection = null;
		try
		{
			connection = DatabaseUtilities.getConnection(s);
			String query = "SELECT * FROM user_data_tan WHERE first_name = ? ";
			PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
																			ResultSet.CONCUR_READ_ONLY);
			prepStatement.setString(1, user);

			ResultSet results = prepStatement.executeQuery();

			return results;

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (connection != null)
				{
					connection.close();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * Create a link for logging out
	 * 
	 * @return Element
	 */
	private Element createLogoutLink()
	{
		A logoutLink = new A();
		logoutLink.addAttribute("href", getLink() + "&logout=true");
		logoutLink.addElement("Logout");

		String logoutStyle = "margin-right:50px; mrgin-top:30px";
		Div logoutDiv = new Div();
		logoutDiv.addAttribute("align", "right");
		logoutDiv.addElement(logoutLink);
		logoutDiv.setStyle(logoutStyle);

		return logoutDiv;
	}

	public Element getCredits()
	{
		return super.getCustomCredits("Created by: Reto Lippuner, Marcel Wirth", new StringElement(""));
	}

}
