
package org.owasp.webgoat.lessons;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;


public class ClientSideValidation extends SequentialLessonAdapter
{

	/**
	 * Description of the Method
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	private boolean stage1FirstVisit = true;

	private boolean stage2FirstVisit = true;

	protected Element createContent(WebSession s)
	{
		return super.createStagedContent(s);
	}

	protected Element doStage1(WebSession s)
	{
		return evalStage1(s);
	}

	protected Element doStage2(WebSession s)
	{
		return stage2Content(s);
	}

	protected Element evalStage1(WebSession s)
	{

		ElementContainer ec = new ElementContainer();

		String param1 = s.getParser().getRawParameter("field1", "");

		// test success

		if (param1.equalsIgnoreCase("platinum") || param1.equalsIgnoreCase("gold") || param1.equalsIgnoreCase("silver")
				|| param1.equalsIgnoreCase("bronze") || param1.equalsIgnoreCase("pressone")
				|| param1.equalsIgnoreCase("presstwo"))
		{
			getLessonTracker(s).setStage(2);
			// s.resetHintCount();
			s.setMessage("Stage 1 completed.");

			// Redirect user to Stage2 content.
			ec.addElement(doStage2(s));

		}
		else
		{
			if (!stage1FirstVisit)
			{
				s.setMessage("Keep looking for the coupon code.");
			}
			stage1FirstVisit = false;

			ec.addElement(stage1Content(s));
		}

		return ec;

	}

	protected Element stage1Content(WebSession s)
	{

		ElementContainer ec = new ElementContainer();

		try
		{

			ec.addElement(new Script().setSrc("javascript/clientSideValidation.js"));

			ec.addElement(new HR().setWidth("90%"));
			ec.addElement(new Center().addElement(new H1().addElement("Shopping Cart")));

			ec.addElement(createQtyTable(s));

			ec.addElement(createTotalTable(s));
			ec.addElement(new BR());
			ec.addElement(new HR().setWidth("90%"));

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		return (ec);
	}

	protected Element stage2Content(WebSession s)
	{

		ElementContainer ec = new ElementContainer();

		try
		{

			ec.addElement(new Script().setSrc("javascript/clientSideValidation.js"));

			ec.addElement(new HR().setWidth("90%"));
			ec.addElement(new Center().addElement(new H1().addElement("Shopping Cart")));

			ec.addElement(createQtyTable(s));

			ec.addElement(createTotalTable(s));
			ec.addElement(new BR());
			ec.addElement(new HR().setWidth("90%"));

			// test success
			DecimalFormat money = new DecimalFormat("$0.00");

			String grandTotalString = s.getParser().getStringParameter("GRANDTOT", "0");

			float grandTotal = 1;

			try
			{
				grandTotal = money.parse(grandTotalString).floatValue();
			} catch (java.text.ParseException e)
			{
				try
				{
					grandTotal = Float.parseFloat(grandTotalString);
				} catch (java.lang.NumberFormatException e1)
				{
					// eat exception, do not update grandTotal
				}
			}

			if (getTotalQty(s) > 0 && grandTotal == 0 && !stage2FirstVisit)
			{
				makeSuccess(s);
			}
			else
			{

				if (!stage2FirstVisit)
				{
					s.setMessage("Your order isn't free yet.");
				}
				stage2FirstVisit = false;
			}

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		return (ec);
	}

	protected ElementContainer createTotalTable(WebSession s)
	{

		ElementContainer ec = new ElementContainer();

		String param1 = s.getParser().getRawParameter("field1", "");
		String param2 = HtmlEncoder.encode(s.getParser().getRawParameter("field2", "4128 3214 0002 1999"));

		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		ec.addElement(new BR());

		TR tr = new TR();
		tr.addElement(new TD().addElement("Total before coupon is applied:"));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "SUBTOT", s.getParser()
													.getStringParameter("SUBTOT", "$0.00")).setReadOnly(true)
													.setStyle("border:0px;")).setAlign("right"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("Total to be charged to your credit card:"));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "GRANDTOT", s.getParser()
													.getStringParameter("GRANDTOT", "$0.00")).setReadOnly(true)
													.setStyle("border:0px;")).setAlign("right"));
		t.addElement(tr);

		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("Enter your credit card number:"));
		tr.addElement(new TD().addElement(new Input(Input.TEXT, "field2", param2)));
		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("Enter your coupon code:"));

		Input input = new Input(Input.TEXT, "field1", param1);
		input.setOnKeyUp("isValidCoupon(field1.value)");
		tr.addElement(new TD().addElement(input));
		t.addElement(tr);

		Element b = ECSFactory.makeButton("Purchase");
		tr = new TR();
		tr.addElement(new TD().addElement(b).setColSpan(2).setAlign("center"));
		t.addElement(tr);
		ec.addElement(t);

		return ec;

	}

	protected int getTotalQty(WebSession s)
	{

		int quantity = 0;

		quantity += s.getParser().getFloatParameter("QTY1", 0.0f);
		quantity += s.getParser().getFloatParameter("QTY2", 0.0f);
		quantity += s.getParser().getFloatParameter("QTY3", 0.0f);
		quantity += s.getParser().getFloatParameter("QTY4", 0.0f);

		return quantity;
	}

	protected ElementContainer createQtyTable(WebSession s)
	{

		ElementContainer ec = new ElementContainer();
		Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

		if (s.isColor())
		{
			t.setBorder(1);
		}

		TR tr = new TR();
		tr.addElement(new TH().addElement("Shopping Cart Items -- To Buy Now").setWidth("70%"));
		tr.addElement(new TH().addElement("Price").setWidth("10%"));
		tr.addElement(new TH().addElement("Quantity").setWidth("10%"));
		tr.addElement(new TH().addElement("Total").setWidth("10%"));
		t.addElement(tr);

		tr = new TR();
		tr.addElement(new TD().addElement("Studio RTA - Laptop/Reading Cart with Tilting Surface - Cherry "));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "PRC1", s.getParser().getStringParameter("PRC1",
																											"$69.99"))
													.setSize(10).setReadOnly(true).setStyle("border:0px;"))
				.setAlign("right"));

		Input input = new Input(Input.TEXT, "QTY1", s.getParser().getStringParameter("QTY1", "0"));

		input.setOnKeyUp("updateTotals();");
		input.setOnLoad("updateTotals();");
		input.setSize(10);

		tr.addElement(new TD().addElement(input).setAlign("right"));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "TOT1", s.getParser().getStringParameter("TOT1",
																											"$0.00"))
													.setSize(10).setReadOnly(true).setStyle("border:0px;"))
				.setAlign("right"));

		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("Dynex - Traditional Notebook Case"));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "PRC2", s.getParser().getStringParameter("PRC2",
																											"$27.99"))
													.setSize(10).setReadOnly(true).setStyle("border:0px;"))
				.setAlign("right"));

		input = new Input(Input.TEXT, "QTY2", s.getParser().getStringParameter("QTY2", "0"));

		input.setOnKeyUp("updateTotals();");
		input.setSize(10);
		tr.addElement(new TD().addElement(input).setAlign("right"));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "TOT2", s.getParser().getStringParameter("TOT2",
																											"$0.00"))
													.setSize(10).setReadOnly(true).setStyle("border:0px;"))
				.setAlign("right"));

		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("Hewlett-Packard - Pavilion Notebook with Intel® Centrino™"));

		tr.addElement(new TD()
				.addElement(
							new Input(Input.TEXT, "PRC3", s.getParser().getStringParameter("PRC3", "$1599.99"))
									.setSize(10).setReadOnly(true).setStyle("border:0px;")).setAlign("right"));

		input = new Input(Input.TEXT, "QTY3", s.getParser().getStringParameter("QTY3", "0"));

		input.setOnKeyUp("updateTotals();");
		input.setSize(10);
		tr.addElement(new TD().addElement(input).setAlign("right"));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "TOT3", s.getParser().getStringParameter("TOT3",
																											"$0.00"))
													.setSize(10).setReadOnly(true).setStyle("border:0px;"))
				.setAlign("right"));

		t.addElement(tr);
		tr = new TR();
		tr.addElement(new TD().addElement("3 - Year Performance Service Plan $1000 and Over "));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "PRC4", s.getParser().getStringParameter("PRC4",
																											"$299.99"))
													.setSize(10).setReadOnly(true).setStyle("border:0px;"))
				.setAlign("right"));

		input = new Input(Input.TEXT, "QTY4", s.getParser().getStringParameter("QTY4", "0"));

		input.setOnKeyUp("updateTotals();");
		input.setSize(10);
		tr.addElement(new TD().addElement(input).setAlign("right"));

		tr.addElement(new TD().addElement(
											new Input(Input.TEXT, "TOT4", s.getParser().getStringParameter("TOT4",
																											"$0.00"))
													.setSize(10).setReadOnly(true).setStyle("border:0px;"))
				.setAlign("right"));

		t.addElement(tr);
		ec.addElement(t);
		return ec;
	}

	protected Category getDefaultCategory()
	{
		return Category.AJAX_SECURITY;
	}

	/**
	 * Gets the hints attribute of the AccessControlScreen object
	 * 
	 * @return The hints value
	 */

	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();

		hints.add("Use Firebug to examine the JavaScript.");

		hints.add("Using Firebug, you can add breakpoints in the JavaScript.");

		hints.add("Use Firebug to find the array of encrypted coupon codes, and "
				+ "step through the JavaScript to see the decrypted values.");

		hints.add("You can use Firebug to inspect (and modify) the HTML.");

		hints.add("Use Firebug to remove the 'readonly' attribute from the input next to "
				+ "'The total charged to your credit card:' and set the value to 0.");

		return hints;

	}

	/**
	 * Gets the instructions attribute of the WeakAccessControl object
	 * 
	 * @return The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "";

		if (getLessonTracker(s).getStage() == 1)
		{
			instructions = "STAGE 1:\tFor this exercise, your mission is to discover a coupon code to receive an unintended discount.";
		}
		else if (getLessonTracker(s).getStage() == 2)
		{
			instructions = "STAGE 2:\tNow, try to get your entire order for free.";
		}
		return (instructions);
	}

	private final static Integer DEFAULT_RANKING = new Integer(120);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the AccessControlScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return "Insecure Client Storage";
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}
