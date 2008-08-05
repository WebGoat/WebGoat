
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.TextArea;
import org.apache.ecs.xhtml.button;
import org.owasp.webgoat.session.WebSession;


public class SameOriginPolicyProtection extends LessonAdapter
{
	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

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

			ec.addElement(new Script().setSrc("javascript/sameOrigin.js"));

			Input hiddenWGStatus = new Input(Input.HIDDEN, "hiddenWGStatus", 0);
			hiddenWGStatus.setID("hiddenWGStatus");
			ec.addElement(hiddenWGStatus);

			Input hiddenGoogleStatus = new Input(Input.HIDDEN, "hiddenGoogleStatus", 0);
			hiddenGoogleStatus.setID("hiddenGoogleStatus");
			ec.addElement(hiddenGoogleStatus);

			ec.addElement(new StringElement("Enter a URL: "));
			ec.addElement(new BR());

			TextArea urlArea = new TextArea();
			urlArea.setID("requestedURL");
			urlArea.setRows(1);
			urlArea.setCols(60);
			urlArea.setWrap("SOFT");
			ec.addElement(urlArea);

			button b = new button();
			b.setValue("Go!");
			b.setType(button.button);
			b.setName("Go!");
			b.setOnClick("submitXHR();");
			b.addElement("Go!");
			ec.addElement(b);

			ec.addElement(new BR());
			ec.addElement(new BR());

			H3 reponseTitle = new H3("Response: ");
			reponseTitle.setID("responseTitle");

			ec.addElement(reponseTitle);
			// ec.addElement(new BR());

			TextArea ta = new TextArea();
			ta.setName("responseArea");
			ta.setID("responseArea");
			ta.setCols(60);
			ta.setRows(4);
			ec.addElement(ta);
			ec.addElement(new BR());

			String webGoatURL = "lessons/Ajax/sameOrigin.jsp";
			String googleURL = "http://www.google.com/search?q=aspect+security";

			ec.addElement(new BR());

			A webGoat = new A();
			webGoat.setHref("javascript:populate(\"" + webGoatURL + "\")");
			webGoat.addElement("Click here to try a Same Origin request:<BR/> " + webGoatURL);
			ec.addElement(webGoat);

			ec.addElement(new BR());
			ec.addElement(new BR());

			A google = new A();
			google.setHref("javascript:populate(\"" + googleURL + "\")");
			google.addElement("Click here to try a Different Origin request:<BR/> " + googleURL);
			ec.addElement(google);

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		int hiddenWGStatusInt = s.getParser().getIntParameter("hiddenWGStatus", 0);
		int hiddenGoogleStatusInt = s.getParser().getIntParameter("hiddenGoogleStatus", 0);

		// System.out.println("hiddenWGStatus:" + hiddenWGStatusInt);
		// System.out.println("hiddenGoogleStatusInt:" + hiddenGoogleStatusInt);

		if (hiddenWGStatusInt == 1 && hiddenGoogleStatusInt == 1)
		{
			makeSuccess(s);
		}

		return (ec);
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Enter a URL to see if it is allowed.");
		hints.add("Click both of the links below to complete the lesson");

		return hints;
	}

	/**
	 * Gets the ranking attribute of the HelloScreen object
	 * 
	 * @return The ranking value
	 */
	private final static Integer DEFAULT_RANKING = new Integer(10);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	protected Category getDefaultCategory()
	{
		return Category.AJAX_SECURITY;
	}

	/**
	 * Gets the title attribute of the HelloScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Same Origin Policy Protection");
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}

	public String getInstructions(WebSession s)
	{
		String instructions = "This exercise demonstrates the "
				+ "Same Origin Policy Protection.  XHR requests can only be passed back to "
				+ " the originating server.  Attempts to pass data to a non-originating server " + " will fail.";

		return (instructions);
	}
}
