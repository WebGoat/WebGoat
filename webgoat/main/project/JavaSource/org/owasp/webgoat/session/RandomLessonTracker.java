
package org.owasp.webgoat.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RandomLessonTracker extends LessonTracker
{

	private String[] stages;

	private String stage;

	private Map<String, Boolean> completed = new HashMap<String, Boolean>();

	public RandomLessonTracker(String[] stages)
	{
		if (stages == null) stages = new String[0];
		this.stages = stages;
	}

	public void setStage(String stage)
	{
		this.stage = stage;
	}

	public String getStage()
	{
		if (this.stage == null && stages.length > 0) return stages[0];
		return this.stage;
	}

	public void setStageComplete(String stage, boolean complete)
	{
		completed.put(stage, Boolean.valueOf(complete));
		if (!complete) return;
		int i = getStageNumber(stage);
		if (i < stages.length - 1) setStage(stages[i + 1]);
	}

	public int getStageNumber(String stage)
	{
		for (int i = 0; i < stages.length; i++)
			if (stages[i].equals(stage)) return i;
		return -1;
	}

	public boolean hasCompleted(String stage)
	{
		Boolean complete = completed.get(stage);
		return complete == null ? false : complete.booleanValue();
	}

	@Override
	public boolean getCompleted()
	{
		for (int i = 0; i < stages.length; i++)
			if (!hasCompleted(stages[i])) return false;
		return true;
	}

	@Override
	public void setCompleted(boolean complete)
	{
		if (complete == true) throw new UnsupportedOperationException("Use individual stage completion instead");
		for (int i = 0; i < stages.length; i++)
			setStageComplete(stages[i], false);
		setStage(stages[0]);
	}

	protected void setProperties(Properties props, Screen screen)
	{
		super.setProperties(props, screen);
		for (int i = 0; i < stages.length; i++)
		{
			String p = props.getProperty(screen.getTitle() + "." + stages[i] + ".completed");
			if (p != null)
			{
				setStageComplete(stages[i], Boolean.valueOf(p));
			}
		}
		setStage(props.getProperty(screen.getTitle() + ".stage"));
	}

	public void store(WebSession s, Screen screen, String user)
	{
		for (int i = 0; i < stages.length; i++)
		{
			if (hasCompleted(stages[i]))
			{
				lessonProperties.setProperty(screen.getTitle() + "." + stages[i] + ".completed", Boolean.TRUE
						.toString());
			}
			else
			{
				lessonProperties.remove(screen.getTitle() + "." + stages[i] + ".completed");
			}
		}
		lessonProperties.setProperty(screen.getTitle() + ".stage", getStage());
		super.store(s, screen, user);
	}

	public String toString()
	{
		StringBuffer buff = new StringBuffer();
		buff.append(super.toString());
		for (int i = 0; i < stages.length; i++)
		{
			buff.append("    - completed " + stages[i] + " :....... " + hasCompleted(stages[i]) + "\n");
		}
		buff.append("    - currentStage:....... " + getStage() + "\n");
		return buff.toString();
	}

}
