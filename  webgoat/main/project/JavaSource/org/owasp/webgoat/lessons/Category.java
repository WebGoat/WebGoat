package org.owasp.webgoat.lessons;

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