package org.owasp.webgoat.session;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WebgoatProperties extends Properties
{
	public WebgoatProperties(String propertiesFileName) throws IOException
	{
		try
		{
			FileInputStream in = new FileInputStream(propertiesFileName);
			load(in);
		}
		catch ( IOException e )
		{
			System.out.println("Warning: Unable to open webgoat.properties file");
		}
	}
	
	public int getIntProperty(String key, int defaultValue)
	{
		int value = defaultValue;
		
		String s = getProperty(key);
		if (s != null)
		{
			value = Integer.parseInt(s);
		}
		
		return value;
	}

	public boolean getBooleanProperty(String key, boolean defaultValue)
	{
		boolean value = defaultValue;
		key = this.trimLesson(key);
		
		String s = getProperty(key);
		if (s != null)
		{
			if (s.equalsIgnoreCase("true"))
				value = true;
			else if (s.equalsIgnoreCase("yes"))
				value = true;
			else if (s.equalsIgnoreCase("on"))
				value = true;
			else if (s.equalsIgnoreCase("false"))
				value = false;
			else if (s.equalsIgnoreCase("no"))
				value = false;
			else if (s.equalsIgnoreCase("off"))
				value = false;
		}
		
		return value;
	}
	
	private String trimLesson(String lesson)
	{
		String result = "";
		
		if(lesson.startsWith("org.owasp.webgoat.lessons."))
		{
			result = lesson.substring("org.owasp.webgoat.lessons.".length(), lesson.length());
		}
		else
		{
			result = lesson;
		}
		
		return result;
	}

	public static void main(String[] args)
	{
		WebgoatProperties properties = null;
		try
		{
			properties = new WebgoatProperties("C:\\webgoat.properties");
		}
		catch (IOException e)
		{
			System.out.println("Error loading properties");
			e.printStackTrace();
		}
		System.out.println(properties.getProperty("CommandInjection.category"));
	}

}
