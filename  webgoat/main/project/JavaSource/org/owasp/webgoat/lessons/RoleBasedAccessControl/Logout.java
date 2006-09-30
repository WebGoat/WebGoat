package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;

public class Logout extends DefaultLessonAction
{
	private LessonAction chainedAction;
	
	public Logout(AbstractLesson lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName);
		this.chainedAction = chainedAction;
	}

	public void handleRequest( WebSession s ) throws ParameterNotFoundException, ValidationException
	{
		//System.out.println("Logging out");

		setSessionAttribute(s, getLessonName() + ".isAuthenticated", Boolean.FALSE);

		// FIXME: Maybe we should forward to Login.
		try
		{
			chainedAction.handleRequest(s);
		}
		catch (UnauthenticatedException ue1)
		{
			System.out.println("Internal server error");
			ue1.printStackTrace();
		}
		catch (UnauthorizedException ue2)
		{
			System.out.println("Internal server error");
			ue2.printStackTrace();
		}

	}

	public String getNextPage(WebSession s)
	{
		return chainedAction.getNextPage(s);
	}

}
