package org.owasp.webgoat.lessons.RoleBasedAccessControl;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.DefaultLessonAction;
import org.owasp.webgoat.session.WebSession;

public class SearchStaff extends DefaultLessonAction
{
	public SearchStaff(AbstractLesson lesson, String lessonName, String actionName)
	{
		super(lesson, lessonName, actionName);
	}

	public String getNextPage(WebSession s)
	{
		return RoleBasedAccessControl.SEARCHSTAFF_ACTION;
	}
		
}
