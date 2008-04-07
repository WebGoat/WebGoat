
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.B;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;
import org.owasp.webgoat.session.WebSession;


public class SessionFixation extends SequentialLessonAdapter
{

	private String LoggedInUser = "";

	private final String mailTo = "jane.plane@owasp.org";
	private final String mailFrom = "admin@webgoatfinancial.com";
	private final String mailTitel = "Check your account";
	private final String MAILCONTENTNAME = "mailname";

	private final static String USER = "user";
	private final static String PASSWORD = "pass";

	/**
	 * Creates Staged WebContent
	 * 
	 * @param s
	 */
	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	/**
	 * Gets the category attribute of the RoleBasedAccessControl object
	 * 
	 * @return The category value
	 */
	protected ElementContainer doStage1(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		String mailContent = s.getParser().getStringParameter(MAILCONTENTNAME, "");
		if (mailContent.contains("SSID"))
		{

			// ec.addElement(mailContent);
			return ec;
		}

		ec.addElement(createStage1Content(s));
		return ec;

	}

	private Element createStage1Content(WebSession s)
	{

		String link = getLink();
		String mailText = "Dear MS. Plane <br><br>" + "During the last week we had a few problems with our servers. "
				+ "A lot of people complained that there account details are wrong. "
				+ "That is why we kindly ask you to use following link to verify your "
				+ "data:<br><br><center><a href=" + link + "&XXXX=YYYYYYYY> WebGoat Financial</a></center><br><br>"
				+ "We are sorry for the caused inconvenience and thank you for your colaboration.<br><br>"
				+ "Your WebGoat Financial Team";

		ElementContainer ec = new ElementContainer();
		Table table = new Table();
		TR tr1 = new TR();
		TD td1 = new TD();
		TD td2 = new TD();

		TR tr2 = new TR();
		TD td3 = new TD();
		TD td4 = new TD();

		TR tr3 = new TR();
		TD td5 = new TD();
		TD td6 = new TD();

		TR tr4 = new TR();
		TD td7 = new TD();
		td7.addAttribute("colspan", 2);

		TR tr5 = new TR();
		TD td8 = new TD();
		td8.addAttribute("colspan", 2);
		td8.addAttribute("align", "center");

		table.addElement(tr1);
		table.addElement(tr2);
		table.addElement(tr3);
		table.addElement(tr4);
		table.addElement(tr5);

		tr1.addElement(td1);
		tr1.addElement(td2);
		tr2.addElement(td3);
		tr2.addElement(td4);
		tr3.addElement(td5);
		tr3.addElement(td6);
		tr4.addElement(td7);
		tr5.addElement(td8);

		ec.addElement(table);

		B b = new B();
		b.addElement("MailTo: ");
		td1.addElement(b);
		td2.addElement(mailTo);

		b = new B();
		b.addElement("MailFrom: ");
		td3.addElement(b);
		td4.addElement(mailFrom);

		b = new B();
		b.addElement("Title: ");
		td5.addElement(b);
		Input titleField = new Input();
		titleField.setValue(mailTitel);
		titleField.addAttribute("size", 30);
		td6.addElement(titleField);

		TextArea mailContent = new TextArea();
		mailContent.addAttribute("cols", 50);
		mailContent.addAttribute("rows", 10);
		mailContent.addElement(mailText);
		mailContent.setName(MAILCONTENTNAME);
		td7.addElement(mailContent);

		td8.addElement(new Input(Input.SUBMIT, "SendMail", "Send Mail"));

		return ec;
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

		// try
		// {
		//			
		//			
		// style sty = new style();
		//
		// sty.addElement("#lesson_wrapper {height: 435px;width: 500px;}#lesson_header
		// {background-image: url(lessons/DBSQLInjection/images/lesson1_header.jpg);width:
		// 490px;padding-right: 10px;padding-top: 60px;background-repeat:
		// no-repeat;}.lesson_workspace {background-image:
		// url(lessons/DBSQLInjection/images/lesson1_workspace.jpg);width: 489px;height:
		// 325px;padding-left: 10px;padding-top: 10px;background-repeat: no-repeat;} .lesson_text
		// {height: 240px;width: 460px;padding-top: 5px;} #lesson_buttons_bottom {height:
		// 20px;width: 460px;} #lesson_b_b_left {width: 300px;float: left;} #lesson_b_b_right input
		// {width: 100px;float: right;} .lesson_title_box {height: 20px;width: 420px;padding-left:
		// 30px;} .lesson_workspace { } .lesson_txt_10 {font-family: Arial, Helvetica,
		// sans-serif;font-size: 10px;} .lesson_text_db {color: #0066FF} #lesson_login
		// {background-image: url(lessons/DBSQLInjection/images/lesson1_loginWindow.jpg);height:
		// 124px;width: 311px;background-repeat: no-repeat;padding-top: 30px;margin-left:
		// 80px;margin-top: 50px;text-align: center;} #lesson_login_txt {font-family: Arial,
		// Helvetica, sans-serif;font-size: 12px;text-align: center;} #lesson_search
		// {background-image: url(lessons/DBSQLInjection/images/lesson1_SearchWindow.jpg);height:
		// 124px;width: 311px;background-repeat: no-repeat;padding-top: 30px;margin-left:
		// 80px;margin-top: 50px;text-align: center;}");
		// ec.addElement(sty);
		//
		// Div wrapperDiv = new Div();
		// wrapperDiv.setID("lesson_wrapper");
		//
		// Div headerDiv = new Div();
		// headerDiv.setID("lesson_header");
		//
		// Div workspaceDiv = new Div();
		// workspaceDiv.setClass("lesson_workspace");
		//
		// wrapperDiv.addElement(headerDiv);
		// wrapperDiv.addElement(workspaceDiv);
		//
		// ec.addElement(wrapperDiv);
		//
		// workspaceDiv.addElement(createWorkspaceContent(s));
		//
		// } catch (Exception e)
		// {
		// s.setMessage("Error generating " + this.getClass().getName());
		// e.printStackTrace();
		// }

		return (ec);
	}

	// /**
	// * Creation of the content of the workspace
	// * @param s
	// * @return Element
	// */
	// private Element createWorkspaceContent(WebSession s)
	// {
	//
	//
	// ElementContainer ec = new ElementContainer();
	//
	// return ec;
	// }

	// /**
	// * Create content for logging in
	// * @param ec
	// */
	// private void createLogInContent(ElementContainer ec, String errorMessage) {
	// Div loginDiv = new Div();
	// loginDiv.setID("lesson_login");
	//		
	// Table table = new Table();
	// //table.setStyle(tableStyle);
	// table.addAttribute("align='center'", 0);
	// TR tr1 = new TR();
	// TD td1 = new TD();
	// TD td2 = new TD();
	// td1.addElement(new StringElement("Enter your name: "));
	// td2.addElement(new Input(Input.TEXT, USER));
	// tr1.addElement(td1);
	// tr1.addElement(td2);
	//
	// TR tr2 = new TR();
	// TD td3 = new TD();
	// TD td4 = new TD();
	// td3.addElement(new StringElement("Enter your password: "));
	// td4.addElement(new Input(Input.PASSWORD, PASSWORD));
	// tr2.addElement(td3);
	// tr2.addElement(td4);
	//
	//
	// TR tr3 = new TR();
	// TD td5 = new TD();
	// td5.setColSpan(2);
	// td5.setAlign("center");
	//
	// td5.addElement(new Input(Input.SUBMIT, "Submit", "Submit"));
	// tr3.addElement(td5);
	//
	// table.addElement(tr1);
	// table.addElement(tr2);
	// table.addElement(tr3);
	// loginDiv.addElement(table);
	// ec.addElement(loginDiv);
	//		
	// H2 errorTag = new H2(errorMessage);
	// errorTag.addAttribute("align", "center");
	// errorTag.addAttribute("class", "info");
	// ec.addElement(errorTag);
	//		
	//
	// }

	// /**
	// * Create content after a successful login
	// * @param s
	// * @param ec
	// */
	// private void createSuccessfulLoginContent(WebSession s,
	// ElementContainer ec) {
	//		
	// String userDataStyle = "margin-top:50px;";
	//
	// Div userDataDiv = new Div();
	// userDataDiv.setStyle(userDataStyle);
	// userDataDiv.addAttribute("align", "center");
	// Table table = new Table();
	// table.addAttribute("cellspacing", 10);
	// table.addAttribute("cellpadding", 5);
	//
	// table.addAttribute("align", "center");
	// TR tr1 =new TR();
	// TR tr2 = new TR();
	// TR tr3 = new TR();
	// TR tr4 = new TR();
	// tr1.addElement(new TD("<b>Firstname:</b>"));
	// tr1.addElement(new TD(LoggedInUser));
	//		
	// try
	// {
	// ResultSet results = getUser(LoggedInUser, s);
	// results.first();
	//			
	// tr2.addElement(new TD("<b>Lastname:</b>"));
	// tr2.addElement(new TD(results.getString("last_name")));
	//			
	// tr3.addElement(new TD("<b>Credit Card Type:</b>"));
	// tr3.addElement(new TD(results.getString("cc_type")));
	//			
	// tr4.addElement(new TD("<b>Credit Card Number:</b>"));
	// tr4.addElement(new TD(results.getString("cc_number")));
	//
	//			
	// }
	//		
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// table.addElement(tr1);
	// table.addElement(tr2);
	// table.addElement(tr3);
	// table.addElement(tr4);
	//
	// userDataDiv.addElement(table);
	// ec.addElement(userDataDiv);
	// ec.addElement(createLogoutLink());
	// }

	// /**
	// * Create a link for logging out
	// * @return Element
	// */
	// private Element createLogoutLink()
	// {
	// A logoutLink = new A();
	// logoutLink.addAttribute("href", getLink() + "&logout=true");
	// logoutLink.addElement("Logout");
	//		
	// String logoutStyle = "margin-right:50px; mrgin-top:30px";
	// Div logoutDiv = new Div();
	// logoutDiv.addAttribute("align", "right");
	// logoutDiv.addElement(logoutLink);
	// logoutDiv.setStyle(logoutStyle);
	//		
	// return logoutDiv;
	// }

	// /**
	// * Get a user by its name
	// * @param user
	// * @param s
	// * @return ResultSet containing the user
	// */
	// private ResultSet getUser(String user, WebSession s)
	// {
	// try {
	// Connection connection = DatabaseUtilities.getConnection(s);
	// String query = "SELECT * FROM user_data_tan WHERE first_name = ? ";
	// PreparedStatement prepStatement =connection.prepareStatement(query,
	// ResultSet.TYPE_SCROLL_INSENSITIVE,
	// ResultSet.CONCUR_READ_ONLY);
	// prepStatement.setString(1, user);
	//
	//
	// ResultSet results = prepStatement.executeQuery();
	//
	// return results;
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	//		
	// }

	// /**
	// * See if the password and corresponding user is valid
	// * @param userName
	// * @param password
	// * @param s
	// * @return true if the password was correct
	// */
	// private boolean correctLogin(String userName, String password, WebSession s)
	// {
	// try {
	// Connection connection = DatabaseUtilities.getConnection(s);
	// String query = "SELECT * FROM user_data_tan WHERE first_name = ? AND password = ?";
	// PreparedStatement prepStatement =connection.prepareStatement(query,
	// ResultSet.TYPE_SCROLL_INSENSITIVE,
	// ResultSet.CONCUR_READ_ONLY);
	// prepStatement.setString(1, userName);
	// prepStatement.setString(2, password);
	//
	// ResultSet results = prepStatement.executeQuery();
	//
	// if ((results != null) && (results.first() == true))
	// {
	//
	// return true;
	//
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return false;
	//
	// }

	/**
	 * Get the category
	 * 
	 * @return the category
	 */
	protected Category getDefaultCategory()
	{
		return Category.SESSION_MANAGEMENT;
	}

	/**
	 * Gets the hints attribute of the RoleBasedAccessControl object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();

		hints.add("Stage 1: Just do a regular login");
		hints.add("Stage 2: How does the server know which TAN has to be used");
		hints.add("Stage 2: Maybe taking a look at the source code helps");
		hints.add("Stage 2: Watch out for hidden fields");
		hints.add("Stage 2: Manipulate the hidden field 'hidden_tan'");

		return hints;

	}

	/**
	 * Get the instructions for the user
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "Stub";

		return (instructions);
	}

	private final static Integer DEFAULT_RANKING = new Integer(222);

	/**
	 * Get the ranking for the hirarchy of lessons
	 */
	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Get the title of the Lesson
	 */
	public String getTitle()
	{
		return ("Session Fixation");
	}

}
