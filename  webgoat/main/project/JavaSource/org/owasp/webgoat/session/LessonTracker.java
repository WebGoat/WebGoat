package org.owasp.webgoat.session;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 *  Description of the Class
 *
 * @author     Bruce Mayhew
 * @created    October 29, 2003
 */
public class LessonTracker
{
	private boolean completed = false;
	private int currentStage = 1;
	private int maxHintLevel = 0;

	private int numVisits = 0;
	private boolean viewedCookies = false;
	private boolean viewedHtml = false;
	private boolean viewedLessonPlan = false;
	private boolean viewedParameters = false;
	private boolean viewedSource = false;

	Properties lessonProperties = new Properties();


	/**
	 *  Gets the completed attribute of the LessonTracker object
	 *
	 * @return    The completed value
	 */
	public boolean getCompleted()
	{
		return completed;
	}

	
	public int getStage()
	{
		return currentStage;
	}
	
	public void setStage(int stage)
	{
		currentStage = stage;
	}

	/**
	 *  Gets the maxHintLevel attribute of the LessonTracker object
	 *
	 * @return    The maxHintLevel value
	 */
	public int getMaxHintLevel()
	{
		return maxHintLevel;
	}


	/**
	 *  Gets the numVisits attribute of the LessonTracker object
	 *
	 * @return    The numVisits value
	 */
	public int getNumVisits()
	{
		return numVisits;
	}


	/**
	 *  Gets the viewedCookies attribute of the LessonTracker object
	 *
	 * @return    The viewedCookies value
	 */
	public boolean getViewedCookies()
	{
		return viewedCookies;
	}


	/**
	 *  Gets the viewedHtml attribute of the LessonTracker object
	 *
	 * @return    The viewedHtml value
	 */
	public boolean getViewedHtml()
	{
		return viewedHtml;
	}



	/**
	 *  Gets the viewedLessonPlan attribute of the LessonTracker object
	 *
	 * @return    The viewedLessonPlan value
	 */
	public boolean getViewedLessonPlan()
	{
		return viewedLessonPlan;
	}


	/**
	 *  Gets the viewedParameters attribute of the LessonTracker object
	 *
	 * @return    The viewedParameters value
	 */
	public boolean getViewedParameters()
	{
		return viewedParameters;
	}


	/**
	 *  Gets the viewedSource attribute of the LessonTracker object
	 *
	 * @return    The viewedSource value
	 */
	public boolean getViewedSource()
	{
		return viewedSource;
	}


	/**
	 *  Description of the Method
	 */
	public void incrementNumVisits()
	{
		numVisits++;
	}


	/**
	 *  Sets the properties attribute of the LessonTracker object
	 *
	 * @param  props  The new properties value
	 */
	private void setProperties( Properties props, Screen screen )
	{
		completed = Boolean.valueOf( props.getProperty( screen.getTitle() + ".completed" ) ).booleanValue();
		maxHintLevel = Integer.parseInt( props.getProperty( screen.getTitle() + ".maxHintLevel" ) );
		currentStage = Integer.parseInt( props.getProperty( screen.getTitle() + ".currentStage" ) );
		numVisits = Integer.parseInt( props.getProperty( screen.getTitle() + ".numVisits" ) );
		viewedCookies = Boolean.valueOf( props.getProperty( screen.getTitle() + ".viewedCookies" ) ).booleanValue();
		viewedHtml = Boolean.valueOf( props.getProperty( screen.getTitle() + ".viewedHtml" ) ).booleanValue();
		viewedLessonPlan = Boolean.valueOf( props.getProperty( screen.getTitle() + ".viewedLessonPlan" ) ).booleanValue();
		viewedParameters = Boolean.valueOf( props.getProperty( screen.getTitle() + ".viewedParameters" ) ).booleanValue();
		viewedSource = Boolean.valueOf( props.getProperty( screen.getTitle() + ".viewedSource" ) ).booleanValue();
	}


	public static String getUserDir( WebSession s )
	{
		return s.getContext().getRealPath( "users" ) +"/";
	}

	private static String getTrackerFile( WebSession s, String user, Screen screen )
	{
		return getUserDir( s ) + user + "." + screen.getClass().getName() + ".props";
	}
	
	
	/**
	 *  Description of the Method
	 *
	 * @param  screen  Description of the Parameter
	 * @param  s       Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public static LessonTracker load( WebSession s, String user, Screen screen )
	{
		FileInputStream in = null;
		try
		{
			String fileName = getTrackerFile(s, user, screen);
			if ( fileName != null )
			{
				Properties tempProps = new Properties();
				//System.out.println("Loading lesson state from: " + fileName);
				in = new FileInputStream( fileName );
				tempProps.load( in );
				// allow the screen to use any custom properties it may have set
				LessonTracker tempLessonTracker = screen.createLessonTracker( tempProps );
				tempLessonTracker.setProperties( tempProps, screen );
				return tempLessonTracker;
			}
		}
		catch ( FileNotFoundException e )
		{
			// Normal if the lesson has not been accessed yet.
		}
		catch ( Exception e )
		{
			System.out.println("Failed to load lesson state for " + screen);
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				in.close();
			} 
			catch (Exception e) {}
		}

		return screen.createLessonTracker();
	}


	/**
	 *  Sets the completed attribute of the LessonTracker object
	 *
	 * @param  completed  The new completed value
	 */
	public void setCompleted( boolean completed )
	{
		this.completed = completed;
	}



	/**
	 *  Sets the maxHintLevel attribute of the LessonTracker object
	 *
	 * @param  maxHintLevel  The new maxHintLevel value
	 */
	public void setMaxHintLevel( int maxHintLevel )
	{
		this.maxHintLevel = Math.max( this.maxHintLevel, maxHintLevel );
	}


	/**
	 *  Sets the viewedCookies attribute of the LessonTracker object
	 *
	 * @param  viewedCookies  The new viewedCookies value
	 */
	public void setViewedCookies( boolean viewedCookies )
	{
		this.viewedCookies = viewedCookies;
	}


	/**
	 *  Sets the viewedHtml attribute of the LessonTracker object
	 *
	 * @param  viewedHtml  The new viewedHtml value
	 */
	public void setViewedHtml( boolean viewedHtml )
	{
		this.viewedHtml = viewedHtml;
	}



	/**
	 *  Sets the viewedLessonPlan attribute of the LessonTracker object
	 *
	 * @param  viewedLessonPlan  The new viewedLessonPlan value
	 */
	public void setViewedLessonPlan( boolean viewedLessonPlan )
	{
		this.viewedLessonPlan = viewedLessonPlan;
	}


	/**
	 *  Sets the viewedParameters attribute of the LessonTracker object
	 *
	 * @param  viewedParameters  The new viewedParameters value
	 */
	public void setViewedParameters( boolean viewedParameters )
	{
		this.viewedParameters = viewedParameters;
	}


	/**
	 *  Sets the viewedSource attribute of the LessonTracker object
	 *
	 * @param  viewedSource  The new viewedSource value
	 */
	public void setViewedSource( boolean viewedSource )
	{
		this.viewedSource = viewedSource;
	}


	/**
	 *  Allows the storing of properties for the logged in and a screen.
	 *
	 * @param  s  Description of the Parameter
	 */
	public void store( WebSession s, Screen screen )
	{
		store( s, screen, s.getUserName() );
	}
	
	/**
	 *  Allows the storing of properties for a user and a screen.
	 *
	 * @param  s  Description of the Parameter
	 */
	public void store( WebSession s, Screen screen, String user )
	{
		FileOutputStream out = null;
		String fileName = getTrackerFile(s, user, screen);
		//System.out.println( "Storing data to" + fileName );
		lessonProperties.setProperty( screen.getTitle() + ".completed", Boolean.toString( completed ) );
		lessonProperties.setProperty( screen.getTitle() + ".currentStage", Integer.toString( currentStage ) );
		lessonProperties.setProperty( screen.getTitle() + ".maxHintLevel", Integer.toString( maxHintLevel ) );
		lessonProperties.setProperty( screen.getTitle() + ".numVisits", Integer.toString( numVisits ) );
		lessonProperties.setProperty( screen.getTitle() + ".viewedCookies", Boolean.toString( viewedCookies ) );
		lessonProperties.setProperty( screen.getTitle() + ".viewedHtml", Boolean.toString( viewedHtml ) );
		lessonProperties.setProperty( screen.getTitle() + ".viewedLessonPlan", Boolean.toString( viewedLessonPlan ) );
		lessonProperties.setProperty( screen.getTitle() + ".viewedParameters", Boolean.toString( viewedParameters ) );
		lessonProperties.setProperty( screen.getTitle() + ".viewedSource", Boolean.toString( viewedSource ) );
		try
		{
			out = new FileOutputStream( fileName );
			lessonProperties.store( out, s.getUserName() );
		}
		catch ( Exception e )
		{
			// what do we want to do,  I think nothing.
			System.out.println( "Warning User data for " + s.getUserName() + " will not persist" );
		}
		finally
		{
			try 
			{
				out.close();
			} 
			catch (Exception e) {}
		}

	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		StringBuffer buff = new StringBuffer();
		buff.append( "LessonTracker:" + "\n" );
		buff.append( "    - completed:.......... " + completed + "\n" );
		buff.append( "    - currentStage:....... " + currentStage + "\n" );
		buff.append( "    - maxHintLevel:....... " + maxHintLevel + "\n" );
		buff.append( "    - numVisits:.......... " + numVisits + "\n" );
		buff.append( "    - viewedCookies:...... " + viewedCookies + "\n" );
		buff.append( "    - viewedHtml:......... " + viewedHtml + "\n" );
		buff.append( "    - viewedLessonPlan:... " + viewedLessonPlan + "\n" );
		buff.append( "    - viewedParameters:... " + viewedParameters + "\n" );
		buff.append( "    - viewedSource:....... " + viewedSource + "\n" + "\n" );
		return buff.toString();
	}
	
	/**
	 * @return Returns the lessonProperties.
	 */
	public Properties getLessonProperties() 
	{
		return lessonProperties;
	}
	
	/**
	 * @param lessonProperties The lessonProperties to set.
	 */
	public void setLessonProperties(Properties lessonProperties) 
	{
		this.lessonProperties = lessonProperties;
	}
}

