
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */

public class AccessControlMatrix extends LessonAdapter
{
	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	private final static String RESOURCE = "Resource";

	private final static String USER = "User";

	private final static String[] resources = { "Public Share", "Time Card Entry", "Performance Review",
			"Time Card Approval", "Site Manager", "Account Manager" };

	private final static String[] roles = { "Public", "User", "Manager", "Admin" };

	private final static String[] users = { "Moe", "Larry", "Curly", "Shemp" };

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
			String user = s.getParser().getRawParameter(USER, users[0]);
			String resource = s.getParser().getRawParameter(RESOURCE, resources[0]);
			String credentials = getRoles(user).toString();

			Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

			if (s.isColor())
			{
				t.setBorder(1);
			}

			TR tr = new TR();
			tr.addElement(new TD().addElement("Change user:"));
			tr.addElement(new TD().addElement(ECSFactory.makePulldown(USER, users, user, 1)));
			t.addElement(tr);

			// These two lines would allow the user to select the resource from a list
			// Didn't seem right to me so I made them type it in.
			// ec.addElement( new P().addElement( "Choose a resource:" ) );
			// ec.addElement( ECSFactory.makePulldown( RESOURCE, resources, resource, 1 ) );
			tr = new TR();
			tr.addElement(new TD().addElement("Select resource: "));
			tr.addElement(new TD().addElement(ECSFactory.makePulldown(RESOURCE, resources, resource, 1)));
			t.addElement(tr);

			tr = new TR();
			tr.addElement(new TD("&nbsp;").setColSpan(2).setAlign("center"));
			t.addElement(tr);

			tr = new TR();
			tr.addElement(new TD(ECSFactory.makeButton("Check Access")).setColSpan(2).setAlign("center"));
			t.addElement(tr);
			ec.addElement(t);

			if (isAllowed(user, resource))
			{
				if (!getRoles(user).contains("Admin") && resource.equals("Account Manager"))
				{
					makeSuccess(s);
				}
				s.setMessage("User " + user + " " + credentials + " was allowed to access resource " + resource);
			}
			else
			{
				s.setMessage("User " + user + " " + credentials + " did not have privilege to access resource "
						+ resource);
			}
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

	protected Category getDefaultCategory()
	{
		return Category.ACCESS_CONTROL;
	}

	/**
	 * Gets the hints attribute of the RoleBasedAccessControl object
	 * 
	 * @return The hints value
	 */
	protected List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Many sites attempt to restrict access to resources by role.");
		hints.add("Developers frequently make mistakes implementing this scheme.");
		hints.add("Attempt combinations of users, roles, and resources.");
		return hints;
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
	private List getResources(List rl)
	{
		// return the resources allowed for these roles
		ArrayList<String> list = new ArrayList<String>();

		if (rl.contains(roles[0]))
		{
			list.add(resources[0]);
		}

		if (rl.contains(roles[1]))
		{
			list.add(resources[1]);
			list.add(resources[5]);
		}

		if (rl.contains(roles[2]))
		{
			list.add(resources[2]);
			list.add(resources[3]);
		}

		if (rl.contains(roles[3]))
		{
			list.add(resources[4]);
			list.add(resources[5]);
		}

		return list;
	}

	/**
	 * Gets the role attribute of the RoleBasedAccessControl object
	 * 
	 * @param user
	 *            Description of the Parameter
	 * @return The role value
	 */

	private List getRoles(String user)
	{
		ArrayList<String> list = new ArrayList<String>();

		if (user.equals(users[0]))
		{
			list.add(roles[0]);
		}
		else if (user.equals(users[1]))
		{
			list.add(roles[1]);
			list.add(roles[2]);
		}
		else if (user.equals(users[2]))
		{
			list.add(roles[0]);
			list.add(roles[2]);
		}
		else if (user.equals(users[3]))
		{
			list.add(roles[3]);
		}

		return list;
	}

	/**
	 * Gets the title attribute of the AccessControlScreen object
	 * 
	 * @return The title value
	 */

	public String getTitle()
	{
		return ("Using an Access Control Matrix");
	}

	// private final static ArrayList userList = new ArrayList(Arrays.asList(users));
	// private final static ArrayList resourceList = new ArrayList(Arrays.asList(resources));
	// private final static ArrayList roleList = new ArrayList(Arrays.asList(roles));

	/**
	 * Please do not ever implement an access control scheme this way! But it's not the worst I've
	 * seen.
	 * 
	 * @param user
	 *            Description of the Parameter
	 * @param resource
	 *            Description of the Parameter
	 * @return The allowed value
	 */

	private boolean isAllowed(String user, String resource)
	{
		List roles = getRoles(user);
		List resources = getResources(roles);
		return (resources.contains(resource));
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}
