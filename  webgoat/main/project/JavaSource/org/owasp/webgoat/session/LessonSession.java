package org.owasp.webgoat.session;

/**
 * Represents a virtual session for a lesson.  Lesson-specific session data may
 * be stored here.
 * 
 * @author David Anderson <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created January 19, 2006
 */
public class LessonSession
{
	private boolean isAuthenticated = false;
	
	private String currentLessonScreen;
	
	public void setAuthenticated(boolean isAuthenticated)
	{
		this.isAuthenticated = isAuthenticated;
	}
	
	public boolean isAuthenticated()
	{
		return this.isAuthenticated;
	}
	
	public void setCurrentLessonScreen(String currentLessonScreen)
	{
		this.currentLessonScreen = currentLessonScreen;
	}
	
	public String getCurrentLessonScreen()
	{
		return this.currentLessonScreen;
	}
	
}
