
package org.owasp.webgoat.session;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Label;
import org.apache.ecs.html.Option;
import org.apache.ecs.html.P;
import org.apache.ecs.html.Select;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.U;


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
 * @author Jeff Williams (jeff.williams@aspectsecurity.com)
 * @created October 29, 2003
 */

public class ECSFactory
{

	/**
	 * Description of the Field
	 */

	public final static String ON = "On";

	/**
	 * Description of the Field
	 */

	public final static String PASSWORD = "Password";

	/**
	 * Don't let anyone instantiate this class
	 */

	private ECSFactory()
	{
	}

	/**
	 * Description of the Method
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makeBox(String name, String value)
	{

		Input i = new Input(Input.CHECKBOX, name, ON);

		i.setChecked(value.equals(ON));

		return (i);
	}

	/**
	 * Description of the Method
	 * 
	 * @param text
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makeButton(String text)
	{

		Input b = new Input();

		b.setType(Input.SUBMIT);
		b.setValue(text);
		b.setName(Input.SUBMIT);

		return (b);
	}

	public static Element makeButton(String text, String onClickFunction)
	{

		Input b = (Input) makeButton(text);
		b.setOnClick(onClickFunction);

		return (b);
	}

	/**
	 * Description of the Method
	 * 
	 * @param labeltext
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @param e
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static TR makeField(String labeltext, String value, Element e)
	{

		TD left = new TD().setAlign("right");

		Label label = new Label().addElement(labeltext);

		left.addElement(label);

		TD right = new TD().setAlign("left");

		right.addElement(e);

		TR row = new TR();

		row.addElement(left);

		row.addElement(right);

		return (row);
	}

	/**
	 * Description of the Method
	 * 
	 * @param labeltext
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @param size
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static TR makeField(String labeltext, String name, String value, int size)
	{

		Input field = new Input().setName(name).setValue(value).setSize(size).setMaxlength(size);

		// double check in case someone means to make a * starred out password field

		if (name.equals(PASSWORD))
		{

			field.setType(Input.PASSWORD);

		}

		return (makeField(labeltext, value, field));
	}

	/**
	 * Description of the Method
	 * 
	 * @param label
	 *            Description of the Parameter
	 * @param type
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @param alignment
	 *            Description of the Parameter
	 * @param selected
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makeInput(String label, String type, String name, boolean value, boolean selected,
			String alignment)
	{

		return makeInput(label, type, name, new Boolean(value).toString(), selected, alignment);
	}

	/**
	 * Description of the Method
	 * 
	 * @param label
	 *            Description of the Parameter
	 * @param type
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makeInput(String label, String type, String name, String value)
	{

		return makeInput(label, type, name, value, new Boolean(value).booleanValue(), "RIGHT");
	}

	/**
	 * Description of the Method
	 * 
	 * @param label
	 *            Description of the Parameter
	 * @param type
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @param alignment
	 *            Description of the Parameter
	 * @param selected
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makeInput(String label, String type, String name, String value, boolean selected,
			String alignment)
	{

		ElementContainer ec = new ElementContainer();

		if (!alignment.equalsIgnoreCase("LEFT"))
		{

			ec.addElement(new StringElement(label));

		}

		Input input = new Input(type, name, value);

		ec.addElement(input);

		if (alignment.equalsIgnoreCase("LEFT"))
		{

			ec.addElement(new StringElement(label));

		}

		if (type.equalsIgnoreCase("CHECKBOX"))
		{

			input.setChecked(selected);

		}

		return (ec);
	}

	/**
	 * Description of the Method
	 * 
	 * @param text
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static A makeLink(String text, String name, String value)
	{

		String href = "attack?" + name;

		if (value.length() > 0)
		{

			href = href + "=" + value;

		}

		A a = new A(href);

		a.addElement(new U().addElement(text));

		a.addAttribute("style", "cursor:hand");

		return (a);
	}

	/**
	 * Description of the Method
	 * 
	 * @param text
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static A makeLink(String text, String name, int value)
	{

		return (makeLink(text, name, Integer.toString(value)));
	}

	/**
	 * Description of the Method
	 * 
	 * @param text
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static A makeLink(String text, String name, boolean value)
	{

		return (makeLink(text, name, new Boolean(value).toString()));
	}

	/**
	 * Description of the Method
	 * 
	 * @param text
	 *            Description of the Parameter
	 * @param clickAction
	 *            Description of the Parameter
	 * @param type
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Input makeOnClickInput(String text, String clickAction, String type)
	{

		Input b = new Input();

		b.setType(type);

		b.setValue(text);

		b.setOnClick(clickAction);

		return (b);
	}

	/**
	 * Description of the Method
	 * 
	 * @param labeltext
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @param e
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static TR makeOption(String labeltext, String value, Element e)
	{

		TD left = new TD().setAlign("left").setWidth("10%");

		left.addElement(e);

		TD right = new TD().setAlign("right");

		Label label = new Label().addElement(labeltext);

		right.addElement(label);

		TR row = new TR();

		row.addElement(right);

		row.addElement(left);

		return (row);
	}

	/**
	 * Description of the Method
	 * 
	 * @param label
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Option makeOption(String label, boolean value)
	{

		Option option = new Option(label, new Boolean(value).toString());

		option.setSelected(value);

		return option;
	}

	/**
	 * Description of the Method
	 * 
	 * @param line
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	private static org.apache.ecs.html.Option makeOption(String line)
	{

		StringTokenizer st = new StringTokenizer(line, "|");

		org.apache.ecs.html.Option o = new org.apache.ecs.html.Option();

		String token = "";

		if (st.hasMoreTokens())
		{

			token = st.nextToken();

		}

		o.addElement(token);

		return (o);
	}

	/**
	 * Description of the Method
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param options
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makePulldown(String name, List<String> options)
	{

		Select s = new Select(name);

		s.addElement(options.toArray(new String[options.size()]));

		return (s);
	}

	/**
	 * Description of the Method
	 * 
	 * @param results
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makePulldown(String name, String results)
	{

		Select select = new Select(name);

		StringTokenizer st = new StringTokenizer(results, "\n");

		if (!st.hasMoreTokens()) {

		return (new StringElement("")); }

		while (st.hasMoreTokens())
		{

			String line = st.nextToken();

			select.addElement(makeOption(line));

		}

		select.addElement("-------------------------");

		return (select);
	}

	/**
	 * Description of the Method
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param list
	 *            Description of the Parameter
	 * @param selected
	 *            Description of the Parameter
	 * @param rowsShowing
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Select makePulldown(String name, Object[] list, String selected, int rowsShowing)
	{

		Select select = new Select(name);

		for (int loop = 0; loop < list.length; loop++)
		{

			String value = list[loop].toString();

			org.apache.ecs.html.Option o = new org.apache.ecs.html.Option(value, value, value);

			if (value.equals(selected))
			{

				o.setSelected(true);

			}

			select.addElement(o);

		}

		select.setSize(rowsShowing);

		return select;
	}

	/**
	 * Default size of 1 for rows showing in select box.
	 * 
	 * @param diffNames
	 *            Description of the Parameter
	 * @param select
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param options
	 *            Description of the Parameter
	 * @param list
	 *            Description of the Parameter
	 * @param selected
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makeSelect(boolean diffNames, Select select, String name, Vector<Option> options,
			String[] list, String selected)
	{

		return makeSelect(diffNames, select, name, options, list, selected, 1);
	}

	/**
	 * Description of the Method
	 * 
	 * @param diffNames
	 *            Description of the Parameter
	 * @param select
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param options
	 *            Description of the Parameter
	 * @param list
	 *            Description of the Parameter
	 * @param selected
	 *            Description of the Parameter
	 * @param rowsShowing
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Select makeSelect(boolean diffNames, Select select, String name, Vector<Option> options,
			String[] list, String selected, int rowsShowing)
	{

		if (select == null)
		{

			select = new Select(name);

			if (diffNames)
			{

				for (int loop = 0; loop < list.length; loop += 2)
				{

					String value = list[loop];

					String label = list[loop + 1];

					Option o = new Option(value);

					if (loop == 0)
					{

						o.setSelected(true);

					}

					options.addElement(o);// add to Vector containing all options

					select.addElement(o);

					select.addElement(label);

				}

			}

			else
			{

				for (int loop = 0; loop < list.length; loop++)
				{

					String value = list[loop];

					org.apache.ecs.html.Option o = new org.apache.ecs.html.Option(value);

					if (loop == 0)
					{

						o.setSelected(true);

					}

					options.addElement(o);// add to Vector containing all options

					select.addElement(o);

					select.addElement(value);

				}

			}

		}

		// find selected option and set selected

		Iterator i = options.iterator();

		while (i.hasNext())
		{

			org.apache.ecs.html.Option o = (org.apache.ecs.html.Option) i.next();

			if (selected.equalsIgnoreCase(o.getAttribute("value")))
			{

				o.setSelected(true);

			}

		}

		select.setSize(rowsShowing);

		return (select);
	}

	/**
	 * Description of the Method
	 * 
	 * @param title
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static Element makeTallHeader(String title)
	{
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < title.length(); i++)
		{
			buff.append(title.charAt(i));
			buff.append("<BR>");
		}
		return new TH(buff.toString());
	}

	/**
	 * Description of the Method
	 * 
	 * @param title
	 *            Description of the Parameter
	 * @param text
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static Element makeTextArea(String title, String text)
	{

		ElementContainer ec = new ElementContainer();

		ec.addElement(new BR());

		ec.addElement(new H3().addElement(title));

		ec.addElement(new P());

		ec.addElement("<CENTER><TEXTAREA ROWS=10 COLS=90 READONLY>" + text + "</TEXTAREA></CENTER>");

		ec.addElement(new BR());

		ec.addElement(new BR());

		return (ec);
	}

}
