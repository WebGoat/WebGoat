package org.owasp.webgoat.lessons;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */
public class Category implements Comparable
{
		
	private String category;
	private Integer ranking;

	public Category( String category, Integer ranking )
	{
		this.category = category;
		this.ranking = ranking;
	}
		 
	public int compareTo( Object obj )
	{
		int value = 1;
		
		if ( obj instanceof Category )
		{
			value = this.getRanking().compareTo( ( (Category) obj ).getRanking() );
		}
			
		return value;
	}
		
	public Integer getRanking()
	{
		return ranking;
	}
		
	public Integer setRanking( Integer ranking )
	{
		return this.ranking = ranking;
	}
		
	public String getName()
	{
		return category;
	}
	
	public boolean equals( Object obj )
	{
		return getName().equals( ((Category)obj).getName() );
	}
	
	public String toString()
	{
		return getName();
	}
}