package org.owasp.webgoat.lessons;

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 *
 * @author     Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */
public class Category implements Comparable
{

    public final static Category A1 = new Category("Unvalidated Parameters",
	    new Integer(110));

    public final static Category A2 = new Category("Broken Access Control",
	    new Integer(210));

    public final static Category A3 = new Category(
	    "Broken Authentication and Session Management", new Integer(310));

    public final static Category A4 = new Category(
	    "Cross-Site Scripting (XSS)", new Integer(410));

    public final static Category A5 = new Category("Buffer Overflows",
	    new Integer(510));

    public final static Category A6 = new Category("Injection Flaws",
	    new Integer(610));

    public final static Category A7 = new Category("Improper Error Handling",
	    new Integer(710));

    public final static Category A8 = new Category("Insecure Storage",
	    new Integer(810));

    public final static Category A9 = new Category("Denial of Service",
	    new Integer(910));

    public final static Category A10 = new Category(
    	    "Insecure Configuration Management", new Integer(1010));

    public final static Category WEB_SERVICES = new Category("Web Services",
	    new Integer(1110));

    public final static Category AJAX_SECURITY = new Category("AJAX Security",
	    new Integer(1150));

    public final static Category ADMIN_FUNCTIONS = new Category(
	    "Admin Functions", new Integer(10));

    public final static Category GENERAL = new Category("General", new Integer(
	    50));

    public final static Category CODE_QUALITY = new Category("Code Quality",
	    new Integer(70));

    public final static Category CHALLENGE = new Category("Challenge",
	    new Integer(2000));

    private String category;

    private Integer ranking;


    public Category(String category, Integer ranking)
    {
	this.category = category;
	this.ranking = ranking;
    }

    public int compareTo(Object obj)
    {
	int value = 1;

	if (obj instanceof Category)
	{
	    value = this.getRanking().compareTo(((Category) obj).getRanking());
	}

	return value;
    }


    public Integer getRanking()
    {
	return ranking;
    }


    public Integer setRanking(Integer ranking)
    {
	return this.ranking = ranking;
    }


    public String getName()
    {
	return category;
    }


    public boolean equals(Object obj)
    {
	return (obj instanceof Category) && getName().equals(((Category) obj).getName());
    }


    public String toString()
    {
	return getName();
    }
}