
package org.owasp.webgoat.lessons;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;


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
 * @author Ryan Knell <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created July, 23 2007
 */

public class ConcurrencyCart extends LessonAdapter
{
	// Shared Variables
	private static int total = 0;
	private static float runningTOTAL = 0;
	private static int subTOTAL = 0;
	private static float calcTOTAL = 0;
	private static int quantity1 = 0;
	private static int quantity2 = 0;
	private static int quantity3 = 0;
	private static int quantity4 = 0;
	private float ratio = 0;
	private int discount = 0;

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
		ElementContainer ec = null;

		try
		{
			String submit = s.getParser().getStringParameter("SUBMIT");

			if ("Purchase".equalsIgnoreCase(submit))
			{
				updateQuantity(s);
				ec = createPurchaseContent(s, quantity1, quantity2, quantity3, quantity4);
			}
			else if ("Confirm".equalsIgnoreCase(submit))
			{
				ec = confirmation(s, quantity1, quantity2, quantity3, quantity4);

				// Discount

				if (calcTOTAL == 0) // No total cost for items
				{
					discount = 0; // Discount meaningless
				}
				else
				// The expected case -- items cost something
				{
					ratio = runningTOTAL / calcTOTAL;
				}

				if (calcTOTAL > runningTOTAL)
				{
					// CONGRATS
					discount = (int) (100 * (1 - ratio));
					s.setMessage("Thank you for shopping! You have (illegally!) received a " + discount
							+ "% discount. Police are on the way to your IP address.");

					makeSuccess(s);
				}
				else if (calcTOTAL < runningTOTAL)
				{
					// ALMOST
					discount = (int) (100 * (ratio - 1));
					s.setMessage("You are on the right track, but you actually overpaid by " + discount
							+ "%. Try again!");
				}
			}
			else
			{
				updateQuantity(s);
				ec = createShoppingPage(s, quantity1, quantity2, quantity3, quantity4);
			}

		} catch (ParameterNotFoundException pnfe)
		{
			// System.out.println("[DEBUG] no action selected, defaulting to createShoppingPage");
			ec = createShoppingPage(s, quantity1, quantity2, quantity3, quantity4);
		}

		return ec;
	}

	// UPDATE QUANTITY VARIABLES
	private void updateQuantity(WebSession s)
	{
		quantity1 = thinkPositive(s.getParser().getIntParameter("QTY1", 0));
		quantity2 = thinkPositive(s.getParser().getIntParameter("QTY2", 0));
		quantity3 = thinkPositive(s.getParser().getIntParameter("QTY3", 0));
		quantity4 = thinkPositive(s.getParser().getIntParameter("QTY4", 0));
	}

	/*
	 * PURCHASING PAGE
	 */

	private ElementContainer createPurchaseContent(WebSession s, int quantity1, int quantity2, int quantity3,
			int quantity4)
	{

		ElementContainer ec = new ElementContainer();
		runningTOTAL = 0;

		String regex1 = "^[0-9]{3}$";// any three digits
		Pattern pattern1 = Pattern.compile(regex1);

		try
		{
			String param1 = s.getParser().getRawParameter("PAC", "111");
			String param2 = HtmlEncoder.encode(s.getParser().getRawParameter("CC", "5321 1337 8888 2007"));

			// test input field1
			if (!pattern1.matcher(param1).matches())
			{
				s.setMessage("Error! You entered " + HtmlEncoder.encode(param1)
						+ " instead of your 3 digit code.  Please try again.");
			}

			ec.addElement(new HR().setWidth("90%"));
			ec.addElement(new Center().addElement(new H1().addElement("Place your order ")));
			Table table = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%")
					.setAlign("center");

			if (s.isColor())
			{
				table.setBorder(1);
			}

			// Table Setup
			TR tr = new TR();
			tr.addElement(new TH().addElement("Shopping Cart Items").setWidth("80%"));
			tr.addElement(new TH().addElement("Price").setWidth("10%"));
			tr.addElement(new TH().addElement("Quantity").setWidth("3%"));
			tr.addElement(new TH().addElement("Subtotal").setWidth("7%"));
			table.addElement(tr);

			// Item 1
			tr = new TR(); // Create a new table object
			tr.addElement(new TD().addElement("Hitachi - 750GB External Hard Drive"));
			tr.addElement(new TD().addElement("$169.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity1)).setAlign("center"));

			total = quantity1 * 169;
			runningTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr); // Adds table to the HTML

			// Item 2
			tr = new TR();
			tr.addElement(new TD().addElement("Hewlett-Packard - All-in-One Laser Printer"));
			tr.addElement(new TD().addElement("$299.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity2)).setAlign("center"));

			total = quantity2 * 299;
			runningTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			// Item 3
			tr = new TR();
			tr.addElement(new TD().addElement("Sony - Vaio with Intel Centrino"));
			tr.addElement(new TD().addElement("$1799.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity3)).setAlign("center"));

			total = quantity3 * 1799;
			runningTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			// Item 4
			tr = new TR();
			tr.addElement(new TD().addElement("Toshiba - XGA LCD Projector "));
			tr.addElement(new TD().addElement("$649.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity4)).setAlign("center"));

			total = quantity4 * 649;
			runningTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			ec.addElement(table);

			table = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

			if (s.isColor())
			{
				table.setBorder(1);
			}

			ec.addElement(new BR());

			calcTOTAL = runningTOTAL;

			// Total Charged
			tr = new TR();
			tr.addElement(new TD().addElement("Total:"));
			tr.addElement(new TD().addElement("$" + formatFloat(runningTOTAL)).setAlign("right"));
			table.addElement(tr);

			tr = new TR();
			tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
			table.addElement(tr);

			// Credit Card Input
			tr = new TR();
			tr.addElement(new TD().addElement("Enter your credit card number:"));
			tr.addElement(new TD().addElement(new Input(Input.TEXT, "CC", param2)).setAlign("right"));
			table.addElement(tr);

			// PAC Input
			tr = new TR();
			tr.addElement(new TD().addElement("Enter your three digit access code:"));
			tr.addElement(new TD().addElement(new Input(Input.TEXT, "PAC", param1)).setAlign("right"));
			table.addElement(tr);

			// Confirm Button
			Element b = ECSFactory.makeButton("Confirm");
			tr = new TR();
			tr.addElement(new TD().addElement(b).setColSpan(2).setAlign("right"));
			table.addElement(tr);

			// Cancel Button
			Element c = ECSFactory.makeButton("Cancel");
			tr = new TR();
			tr.addElement(new TD().addElement(c).setColSpan(2).setAlign("right"));
			table.addElement(tr);

			ec.addElement(table);
			ec.addElement(new BR());

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}

		return (ec);
	}

	/*
	 * CONFIRMATION PAGE
	 */

	private ElementContainer confirmation(WebSession s, int quantity1, int quantity2, int quantity3, int quantity4)
	{
		ElementContainer ec = new ElementContainer();

		final String confNumber = "CONC-88";
		calcTOTAL = 0;
		try
		{
			// Thread.sleep(5000);

			ec.addElement(new HR().setWidth("90%"));
			ec.addElement(new Center().addElement(new H1().addElement("Thank you for your purchase!")));
			ec.addElement(new Center().addElement(new H1().addElement("Confirmation number: " + confNumber)));
			Table table = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%")
					.setAlign("center");

			if (s.isColor())
			{
				table.setBorder(1);
			}

			// Table Setup
			TR tr = new TR();
			tr.addElement(new TH().addElement("Shopping Cart Items").setWidth("80%"));
			tr.addElement(new TH().addElement("Price").setWidth("10%"));
			tr.addElement(new TH().addElement("Quantity").setWidth("3%"));
			tr.addElement(new TH().addElement("Subtotal").setWidth("7%"));
			table.addElement(tr);

			// Item 1
			tr = new TR(); // Create a new table object
			tr.addElement(new TD().addElement("Hitachi - 750GB External Hard Drive"));
			tr.addElement(new TD().addElement("$169.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity1)).setAlign("center"));

			total = quantity1 * 169;
			calcTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr); // Adds table to the HTML

			// Item 2
			tr = new TR();
			tr.addElement(new TD().addElement("Hewlett-Packard - All-in-One Laser Printer"));
			tr.addElement(new TD().addElement("$299.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity2)).setAlign("center"));

			total = quantity2 * 299;
			calcTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			// Item 3
			tr = new TR();
			tr.addElement(new TD().addElement("Sony - Vaio with Intel Centrino"));
			tr.addElement(new TD().addElement("$1799.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity3)).setAlign("center"));

			total = quantity3 * 1799;
			calcTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			// Item 4
			tr = new TR();
			tr.addElement(new TD().addElement("Toshiba - XGA LCD Projector "));
			tr.addElement(new TD().addElement("$649.00").setAlign("right"));
			tr.addElement(new TD().addElement(String.valueOf(quantity4)).setAlign("center"));

			total = quantity4 * 649;
			calcTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			ec.addElement(table);

			table = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

			if (s.isColor())
			{
				table.setBorder(1);
			}

			ec.addElement(new BR());

			// Total Charged
			tr = new TR();
			tr.addElement(new TD().addElement("Total Amount Charged to Your Credit Card:"));
			tr.addElement(new TD().addElement("$" + formatFloat(runningTOTAL)).setAlign("right"));
			table.addElement(tr);

			tr = new TR();
			tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
			table.addElement(tr);

			// Return to Store Button
			Element b = ECSFactory.makeButton("Return to Store");
			tr = new TR();
			tr.addElement(new TD().addElement(b).setColSpan(2).setAlign("center"));
			table.addElement(tr);

			ec.addElement(table);
			ec.addElement(new BR());

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		return (ec);
	}

	/*
	 * SHOPPING PAGE
	 */

	private ElementContainer createShoppingPage(WebSession s, int quantity1, int quantity2, int quantity3, int quantity4)
	{

		ElementContainer ec = new ElementContainer();
		subTOTAL = 0;

		try
		{

			ec.addElement(new HR().setWidth("90%"));
			ec.addElement(new Center().addElement(new H1().addElement("Shopping Cart ")));
			Table table = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%")
					.setAlign("center");

			if (s.isColor())
			{
				table.setBorder(1);
			}

			// Table Setup
			TR tr = new TR();
			tr.addElement(new TH().addElement("Shopping Cart Items").setWidth("80%"));
			tr.addElement(new TH().addElement("Price").setWidth("10%"));
			tr.addElement(new TH().addElement("Quantity").setWidth("3%"));
			tr.addElement(new TH().addElement("Subtotal").setWidth("7%"));
			table.addElement(tr);

			// Item 1
			tr = new TR(); // Create a new table object
			tr.addElement(new TD().addElement("Hitachi - 750GB External Hard Drive"));
			tr.addElement(new TD().addElement("$169.00").setAlign("right"));
			tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY1", String.valueOf(quantity1)))
					.setAlign("right"));

			total = quantity1 * 169;
			subTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr); // Adds table to the HTML

			// Item 2
			tr = new TR();
			tr.addElement(new TD().addElement("Hewlett-Packard - All-in-One Laser Printer"));
			tr.addElement(new TD().addElement("$299.00").setAlign("right"));
			tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY2", String.valueOf(quantity2)))
					.setAlign("right"));

			total = quantity2 * 299;
			subTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			// Item 3
			tr = new TR();
			tr.addElement(new TD().addElement("Sony - Vaio with Intel Centrino"));
			tr.addElement(new TD().addElement("$1799.00").setAlign("right"));
			tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY3", String.valueOf(quantity3)))
					.setAlign("right"));

			total = quantity3 * 1799;
			subTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			// Item 4
			tr = new TR();
			tr.addElement(new TD().addElement("Toshiba - XGA LCD Projector "));
			tr.addElement(new TD().addElement("$649.00").setAlign("right"));
			tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY4", String.valueOf(quantity4)))
					.setAlign("right"));

			total = quantity4 * 649;
			subTOTAL += total;
			tr.addElement(new TD().addElement("$" + formatInt(total) + ".00"));
			table.addElement(tr);

			ec.addElement(table);

			table = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

			if (s.isColor())
			{
				table.setBorder(1);
			}

			ec.addElement(new BR());

			// Purchasing Amount
			tr = new TR();
			tr.addElement(new TD().addElement("Total: " + "$" + formatInt(subTOTAL) + ".00").setAlign("left"));
			table.addElement(tr);

			// Update Button
			Element b = ECSFactory.makeButton("Update Cart");
			tr = new TR();
			tr.addElement(new TD().addElement(b).setColSpan(2).setAlign("right"));
			table.addElement(tr);

			tr = new TR();
			tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
			table.addElement(tr);

			// Purchase Button
			Element c = ECSFactory.makeButton("Purchase");
			tr = new TR();
			tr.addElement(new TD().addElement(c).setColSpan(2).setAlign("right"));
			table.addElement(tr);

			ec.addElement(table);
			ec.addElement(new BR());

		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
		return (ec);
	}

	String formatInt(int i)
	{
		NumberFormat intFormat = NumberFormat.getIntegerInstance(Locale.US);
		return intFormat.format(i);
	}

	String formatFloat(float f)
	{
		NumberFormat floatFormat = NumberFormat.getNumberInstance(Locale.US);
		floatFormat.setMinimumFractionDigits(2);
		floatFormat.setMaximumFractionDigits(2);
		return floatFormat.format(f);
	}

	int thinkPositive(int i)
	{
		if (i < 0)
			return 0;
		else
			return i;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected Category getDefaultCategory()
	{
		return Category.CONCURRENCY;
	}

	/**
	 * Gets the hints attribute of the AccessControlScreen object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Can you purchase the merchandise in your shopping cart for a lower price?");
		hints.add("Try using a new browser window to get a lower price.");
		hints.add("In window A, purchase a low cost item. In window B, update the card with a high cost item.");
		hints.add("In window A, commit after updating cart in window B.");

		return hints;
	}

	/**
	 * Gets the instructions attribute of the WeakAccessControl object
	 * 
	 * @return The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "For this exercise, your mission is to exploit the concurrency issue which will allow you to purchase merchandise for a lower price.";
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
		return "Shopping Cart Concurrency Flaw";
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}