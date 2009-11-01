
package org.owasp.webgoat.lessons;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.SequentialLessonTracker;
import org.owasp.webgoat.session.WebSession;


public abstract class SequentialLessonAdapter extends LessonAdapter
{

	public void setStage(WebSession s, int stage)
	{
		// System.out.println("Changed to stage " + stage);
		getLessonTracker(s).setStage(stage);
	}

	/*
	 * By default returns 1 stage. (non-Javadoc)
	 */
	public int getStageCount()
	{
		return 1;
	}

	public int getStage(WebSession s)
	{
		int stage = getLessonTracker(s).getStage();

		// System.out.println("In stage " + stage);
		return stage;
	}

	@Override
	public SequentialLessonTracker getLessonTracker(WebSession s)
	{
		return (SequentialLessonTracker) super.getLessonTracker(s);
	}

	@Override
	public SequentialLessonTracker getLessonTracker(WebSession s, AbstractLesson lesson)
	{
		return (SequentialLessonTracker) super.getLessonTracker(s, lesson);
	}

	@Override
	public SequentialLessonTracker getLessonTracker(WebSession s, String userNameOverride)
	{
		return (SequentialLessonTracker) super.getLessonTracker(s, userNameOverride);
	}

	@Override
	public LessonTracker createLessonTracker()
	{
		return new SequentialLessonTracker();
	}

	protected Element createStagedContent(WebSession s)
	{
		try
		{
			int stage = getLessonTracker(s).getStage();
			// int stage = Integer.parseInt(
			// getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE,"1"));

			switch (stage)
			{
				case 1:
					return (doStage1(s));
				case 2:
					return (doStage2(s));
				case 3:
					return (doStage3(s));
				case 4:
					return (doStage4(s));
				case 5:
					return (doStage5(s));
				case 6:
					return (doStage6(s));
				default:
					throw new Exception("Invalid stage");
			}
		} catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			// System.out.println(e);
			e.printStackTrace();
		}

		return (new StringElement(""));
	}

	protected Element doStage1(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement("Stage 1 Stub");
		return ec;
	}

	protected Element doStage2(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement("Stage 2 Stub");
		return ec;
	}

	protected Element doStage3(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement("Stage 3 Stub");
		return ec;
	}

	protected Element doStage4(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement("Stage 4 Stub");
		return ec;
	}

	protected Element doStage5(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement("Stage 5 Stub");
		return ec;
	}

	protected Element doStage6(WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement("Stage 6 Stub");
		return ec;
	}

}
