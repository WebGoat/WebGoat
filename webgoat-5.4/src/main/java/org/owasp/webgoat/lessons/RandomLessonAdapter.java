
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.SQLException;
import org.owasp.webgoat.session.CreateDB;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.RandomLessonTracker;
import org.owasp.webgoat.session.WebSession;


public abstract class RandomLessonAdapter extends LessonAdapter
{

	public abstract String[] getStages();

	public void setStage(WebSession s, String stage)
	{
		getLessonTracker(s).setStage(stage);
		try
		{
			Connection connection = DatabaseUtilities.getConnection(s);

			CreateDB db = new CreateDB();
			db.makeDB(connection);
			System.out.println("Successfully refreshed the database.");

		} catch (SQLException sqle)
		{
			System.out.println("Error refreshing the database!");
			sqle.printStackTrace();
		}
	}

	public String getStage(WebSession s)
	{
		return getLessonTracker(s).getStage();
	}

	public void setStageComplete(WebSession s, String stage)
	{
		RandomLessonTracker lt = getLessonTracker(s);
		lt.setStageComplete(stage, true);
		if (lt.getCompleted())
		{
			s.setMessage("Congratulations, you have completed this lab");
		}
		else
		{
			s.setMessage("You have completed Stage " + (lt.getStageNumber(stage) + 1) + ": " + stage + ".");
			if (!stage.equals(lt.getStage()))
				s.setMessage(" Welcome to Stage " + (lt.getStageNumber(lt.getStage()) + 1) + ": " + lt.getStage());
		}
		try
		{
			Connection connection = DatabaseUtilities.getConnection(s);

			CreateDB db = new CreateDB();
			db.makeDB(connection);
			System.out.println("Successfully refreshed the database.");

		} catch (SQLException sqle)
		{
			System.out.println("Error refreshing the database!");
			sqle.printStackTrace();
		}
	}

	public boolean isStageComplete(WebSession s, String stage)
	{
		return getLessonTracker(s).hasCompleted(stage);
	}

	@Override
	public RandomLessonTracker getLessonTracker(WebSession s)
	{
		return (RandomLessonTracker) super.getLessonTracker(s);
	}

	@Override
	public RandomLessonTracker getLessonTracker(WebSession s, AbstractLesson lesson)
	{
		return (RandomLessonTracker) super.getLessonTracker(s, lesson);
	}

	@Override
	public RandomLessonTracker getLessonTracker(WebSession s, String userNameOverride)
	{
		return (RandomLessonTracker) super.getLessonTracker(s, userNameOverride);
	}

	@Override
	public LessonTracker createLessonTracker()
	{
		return new RandomLessonTracker(getStages());
	}

}
