package org.owasp.webgoat.lessons.instructor.CrossSiteScripting;

import java.util.regex.Pattern;
import org.owasp.webgoat.lessons.CrossSiteScripting.FindProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


// STAGE 5 FIXES
// Solution Summary: Edit FindProfile.java and change getRequestParameter().
// Modify getRequestParameter() with lines denoted by // STAGE 5 - FIX.
// Solution Steps:
// 1. Talk about the different parser methods. We could have used the parser method that takes a
// regular expression.
// 2. Call validate on the request parameter.
// return validate(s.getParser().getRawParameter(name), (Pattern) patterns.get(name));
//
// Note: patterns.get(name) is used to fetch the XSS validation pattern that is defined
// in FindProfile.Java
//          
// protected static Map patterns = new HashMap();
// static
// {
// patterns.put(CrossSiteScripting.SEARCHNAME, Pattern.compile("[a-zA-Z ]{0,20}"));
// }



public class FindProfile_i extends FindProfile
{
	public FindProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName, chainedAction);
	}

	protected String getRequestParameter(WebSession s, String name) throws ParameterNotFoundException,
			ValidationException
	{
		// NOTE:
		//
		// In order for this to work generically, the name of the parameter and the name
		// of the regular expression validation patter must be the same.
		// 
		// Another way this could be done is to use the reguler expression method in the
		// ParameterParser class

		// STAGE 5 - FIX
		return validate(s.getParser().getRawParameter(name), (Pattern) patterns.get(name));

		// Note the design goal here...
		// return s.getParser().getStringParameter(name), (Pattern) patterns.get(name));
	}

}
