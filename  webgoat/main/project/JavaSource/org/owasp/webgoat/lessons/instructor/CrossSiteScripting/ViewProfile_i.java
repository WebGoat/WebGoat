package org.owasp.webgoat.lessons.instructor.CrossSiteScripting;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.CrossSiteScripting.ViewProfile;

/* STAGE 4 FIXES
Solution Summary: Look in the WebContent/lesson/CrossSiteScripting/ViewProfile.jsp

Look for the <-- STAGE 4 - FIX    in the ViewProfile.jsp

*/

public class ViewProfile_i extends ViewProfile
{
	public ViewProfile_i(AbstractLesson lesson, String lessonName, String actionName)
	{
		super(lesson, lessonName, actionName);
	}
}
