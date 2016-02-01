
package org.owasp.webgoat.session;

import java.util.Properties;


/**
 * <p>SequentialLessonTracker class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class SequentialLessonTracker extends LessonTracker
{

	private int currentStage = 1;

	/**
	 * <p>getStage.</p>
	 *
	 * @return a int.
	 */
	public int getStage()
	{
		return currentStage;
	}

	/**
	 * <p>setStage.</p>
	 *
	 * @param stage a int.
	 */
	public void setStage(int stage)
	{
		currentStage = stage;
	}

	/** {@inheritDoc} */
	protected void setProperties(Properties props, Screen screen)
	{
		super.setProperties(props, screen);
		currentStage = Integer.parseInt(props.getProperty(screen.getTitle() + ".currentStage"));
	}

	/** {@inheritDoc} */
	public void store(WebSession s, Screen screen, String user)
	{
		lessonProperties.setProperty(screen.getTitle() + ".currentStage", Integer.toString(currentStage));
		super.store(s, screen, user);
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString()
	{
		return super.toString() + "    - currentStage:....... " + currentStage + "\n";
	}
}
