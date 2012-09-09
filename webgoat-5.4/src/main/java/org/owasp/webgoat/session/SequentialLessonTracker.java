
package org.owasp.webgoat.session;

import java.util.Properties;


public class SequentialLessonTracker extends LessonTracker
{

	private int currentStage = 1;

	public int getStage()
	{
		return currentStage;
	}

	public void setStage(int stage)
	{
		currentStage = stage;
	}

	protected void setProperties(Properties props, Screen screen)
	{
		super.setProperties(props, screen);
		currentStage = Integer.parseInt(props.getProperty(screen.getTitle() + ".currentStage"));
	}

	public void store(WebSession s, Screen screen, String user)
	{
		lessonProperties.setProperty(screen.getTitle() + ".currentStage", Integer.toString(currentStage));
		super.store(s, screen, user);
	}

	public String toString()
	{
		return super.toString() + "    - currentStage:....... " + currentStage + "\n";
	}
}
