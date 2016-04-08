
package org.owasp.webgoat.lessons;

import org.owasp.webgoat.session.CreateDB;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.RandomLessonTracker;
import org.owasp.webgoat.session.WebSession;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * <p>Abstract RandomLessonAdapter class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public abstract class RandomLessonAdapter extends LessonAdapter
{

    /**
     * <p>getStages.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public abstract String[] getStages();

    /**
     * <p>setStage.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param stage a {@link java.lang.String} object.
     * @param stage a {@link java.lang.String} object.
     */
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

    /**
     * <p>getStage.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @return a {@link java.lang.String} object.
     */
    public String getStage(WebSession s)
    {
        return getLessonTracker(s).getStage();
    }

    /**
     * <p>setStageComplete.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param stage a {@link java.lang.String} object.
     * @param stage a {@link java.lang.String} object.
     */
    public void setStageComplete(WebSession s, String stage)
    {
        RandomLessonTracker lt = getLessonTracker(s);
        lt.setStageComplete(stage, true);
        if (lt.getCompleted())
        {
            //s.setMessage("Congratulations, you have completed this lab");
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

    /**
     * <p>isStageComplete.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param stage a {@link java.lang.String} object.
     * @param stage a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isStageComplete(WebSession s, String stage)
    {
        return getLessonTracker(s).hasCompleted(stage);
    }

    /** {@inheritDoc} */
    @Override
    public RandomLessonTracker getLessonTracker(WebSession s)
    {
        return (RandomLessonTracker) super.getLessonTracker(s);
    }

    /** {@inheritDoc} */
    @Override
    public RandomLessonTracker getLessonTracker(WebSession s, AbstractLesson lesson)
    {
        return (RandomLessonTracker) super.getLessonTracker(s, lesson);
    }

    /** {@inheritDoc} */
    @Override
    public RandomLessonTracker getLessonTracker(WebSession s, String userNameOverride)
    {
        return (RandomLessonTracker) super.getLessonTracker(s, userNameOverride);
    }

    /** {@inheritDoc} */
    @Override
    public LessonTracker createLessonTracker()
    {
        return new RandomLessonTracker(getStages());
    }

}
