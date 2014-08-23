
package org.owasp.webgoat.lessons;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.WebGoatI18N;


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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class HiddenFieldTampering extends LessonAdapter
{
	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	private final static String PRICE = "Price";

	private final static String PRICE_TV = "2999.99";

	private final static String PRICE_TV_HACKED = "9.99";

	String regex = "^" + PRICE_TV + "$"; // obviously the "." will match any char - any
	// interesting exploit!
	Pattern pattern1 = Pattern.compile(regex);
	String lineSep = System.getProperty("line.separator");
	String script = "<SCRIPT>" + lineSep + "regex=/" + regex + "/;" + "function validate() { " + lineSep
			+ "if (!regex.test(document.form." + PRICE + ".value)) {alert('Data tampering is disallowed'); "
			+ " document.form." + PRICE + ".value = " + PRICE_TV + ";}" + lineSep + "else document.form.submit();"
			+ lineSep + "} " + lineSep + "</SCRIPT>" + lineSep;

	/**
	 * Constructor for the HiddenFieldScreen object
	 */
	public HiddenFieldTampering()
	{
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
		ec.addElement(new StringElement(script));
		float quantity;
		float total;
		String price = PRICE_TV;
		DecimalFormat money = new DecimalFormat("$0.00");
		try
		{
			price = s.getParser().getRawParameter(PRICE, PRICE_TV);
			quantity = s.getParser().getFloatParameter("QTY", 1.0f);
			total = quantity * Float.parseFloat(price);
		} catch (Exception e)
		{
			s.setMessage(WebGoatI18N.get("Invaild data") + this.getClass().getName());
			price = PRICE_TV;
			quantity = 1.0f;
			total = quantity * Float.parseFloat(PRICE_TV);

		}

		if (price.equals(PRICE_TV))
		{
			ec.addElement(new Center().addElement(new H1().addElement(WebGoatI18N.get("ShoppingCart"))));
			ec.addElement(new BR());
			Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

			if (s.isColor())
			{
				t.setBorder(1);
			}

			TR tr = new TR();
			tr.addElement(new TH().addElement(WebGoatI18N.get("ShoppingCartItems")).setWidth("80%"));
			tr.addElement(new TH().addElement(WebGoatI18N.get("Price")).setWidth("10%"));
			tr.addElement(new TH().addElement(WebGoatI18N.get("Quantity")).setWidth("3%"));
			tr.addElement(new TH().addElement(WebGoatI18N.get("Total")).setWidth("7%"));
			t.addElement(tr);

			tr = new TR();
			tr.addElement(new TD().addElement("56 inch HDTV (model KTV-551)"));
			tr.addElement(new TD().addElement(PRICE_TV).setAlign("right"));
			tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY", 1).setSize(6)).setAlign("right"));
			tr.addElement(new TD().addElement(money.format(total)));
			t.addElement(tr);

			ec.addElement(t);

			t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

			if (s.isColor())
			{
				t.setBorder(1);
			}

			ec.addElement(new BR());
			tr = new TR();
			tr.addElement(new TD().addElement(WebGoatI18N.get("TotalChargedCreditCard")+":"));
			tr.addElement(new TD().addElement(money.format(total)));
			tr.addElement(new TD().addElement(ECSFactory.makeButton(WebGoatI18N.get("UpdateCart"))));
			tr.addElement(new TD().addElement(ECSFactory.makeButton(WebGoatI18N.get("Purchase"), "validate()")));
			t.addElement(tr);

			ec.addElement(t);

			Input input = new Input(Input.HIDDEN, PRICE, PRICE_TV);
			ec.addElement(input);
			ec.addElement(new BR());

		}
		else
		{
			if (!price.toString().equals(PRICE_TV))
			{
				makeSuccess(s);
			}

			ec.addElement(new P().addElement(WebGoatI18N.get("TotalPriceIs")+":"));
			ec.addElement(new B("$" + total));
			ec.addElement(new BR());
			ec.addElement(new P().addElement(WebGoatI18N.get("ThisAmountCharged")));
		}

		return (ec);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected Category getDefaultCategory()
	{
		return Category.PARAMETER_TAMPERING;
	}

	/**
	 * Gets the hints attribute of the HiddenFieldScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		
		hints.add(WebGoatI18N.get("HiddenFieldTamperingHint1"));
		hints.add(WebGoatI18N.get("HiddenFieldTamperingHint2"));
		hints.add(WebGoatI18N.get("HiddenFieldTamperingHint3")+ PRICE_TV +WebGoatI18N.get("HiddenFieldTamperingHint32") + PRICE_TV_HACKED );
		
		return hints;
	}

	

	private final static Integer DEFAULT_RANKING = new Integer(50);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the HiddenFieldScreen object
	 * 
	 * @return The title value
	 */
	public String getTitle()
	{
		return ("Exploit Hidden Fields");
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}
