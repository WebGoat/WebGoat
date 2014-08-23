
package org.owasp.webgoat.lessons.instructor.CrossSiteScripting;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.owasp.webgoat.lessons.CrossSiteScripting.CrossSiteScripting;
import org.owasp.webgoat.lessons.CrossSiteScripting.UpdateProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.session.Employee;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.ParameterParser;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


// STAGE 2 FIXES
// Solution Summary: Edit UpdateProfile.java and change parseEmployeeProfile().
// Modify parseEmployeeProfile() with lines denoted by // STAGE 2 - FIX.
// Solution Steps:
// 1. Talk about the different parser methods.
// a. parseEmployeeProfile(subjectId, s.getRequest())
// - uses the request object directly.
// - calling validate() on the appropriate parameter
// b. parseEmployeeProfile(subjectId, s.getParser())
// - uses the parser object to pull request data (centralized mechanism)
//
// 2. Fix the request object version of the call // STAGE 2 - FIX
// Replace the call to:
// String address1 = request.getParameter(CrossSiteScripting.ADDRESS1);
//   
// With:
// final Pattern PATTERN_ADDRESS1 = Pattern.compile("[a-zA-Z0-9,\\.\\- ]{0,80}"); // STAGE 2 - FIX
// String address1 = validate(request.getParameter(CrossSiteScripting.ADDRESS1), PATTERN_ADDRESS1);
// // STAGE 2 - FIX
//
//
// 3. Fix the parser version of the call. // STAGE 2 - ALTERNATE FIX
// Change all calls in parseEmployeeProfile(subjectId, s.getParser()) to use
// the appropriate parser.method() call
//

public class UpdateProfile_i extends UpdateProfile
{
	public UpdateProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName, chainedAction);
	}

	protected Employee parseEmployeeProfile(int subjectId, WebSession s) throws ParameterNotFoundException,
			ValidationException
	{
		HttpServletRequest request = s.getRequest();
		String firstName = request.getParameter(CrossSiteScripting.FIRST_NAME);
		String lastName = request.getParameter(CrossSiteScripting.LAST_NAME);
		String ssn = request.getParameter(CrossSiteScripting.SSN);
		String title = request.getParameter(CrossSiteScripting.TITLE);
		String phone = request.getParameter(CrossSiteScripting.PHONE_NUMBER);

		// Validate this parameter against a regular expression pattern designed for street
		// addresses.

		// STAGE 2 - FIX
		final Pattern PATTERN_ADDRESS1 = Pattern.compile("[a-zA-Z0-9,\\.\\- ]{0,80}");
		String address1 = validate(request.getParameter(CrossSiteScripting.ADDRESS1), PATTERN_ADDRESS1);

		
		String address2 = request.getParameter(CrossSiteScripting.ADDRESS2);
		int manager = Integer.parseInt(request.getParameter(CrossSiteScripting.MANAGER));
		String startDate = request.getParameter(CrossSiteScripting.START_DATE);
		int salary = Integer.parseInt(request.getParameter(CrossSiteScripting.SALARY));
		String ccn = request.getParameter(CrossSiteScripting.CCN);
		int ccnLimit = Integer.parseInt(request.getParameter(CrossSiteScripting.CCN_LIMIT));
		String disciplinaryActionDate = request.getParameter(CrossSiteScripting.DISCIPLINARY_DATE);
		String disciplinaryActionNotes = request.getParameter(CrossSiteScripting.DISCIPLINARY_NOTES);
		String personalDescription = request.getParameter(CrossSiteScripting.DESCRIPTION);

		Employee employee = new Employee(subjectId, firstName, lastName, ssn, title, phone, address1, address2,
				manager, startDate, salary, ccn, ccnLimit, disciplinaryActionDate, disciplinaryActionNotes,
				personalDescription);

		return employee;
	}

	protected Employee parseEmployeeProfile(int subjectId, ParameterParser parser) throws ParameterNotFoundException,
			ValidationException
	{
		// STAGE 2 - ALTERNATE FIX
		String firstName = parser.getStrictAlphaParameter(CrossSiteScripting.FIRST_NAME, 20);
		String lastName = parser.getStrictAlphaParameter(CrossSiteScripting.LAST_NAME, 20);
		String ssn = parser.getSsnParameter(CrossSiteScripting.SSN);
		String title = parser.getStrictAlphaParameter(CrossSiteScripting.TITLE, 20);
		String phone = parser.getPhoneParameter(CrossSiteScripting.PHONE_NUMBER);
		String address1 = parser.getStringParameter(CrossSiteScripting.ADDRESS1);
		String address2 = parser.getStringParameter(CrossSiteScripting.ADDRESS2);
		int manager = parser.getIntParameter(CrossSiteScripting.MANAGER);
		String startDate = parser.getDateParameter(CrossSiteScripting.START_DATE);
		int salary = parser.getIntParameter(CrossSiteScripting.SALARY);
		String ccn = parser.getCcnParameter(CrossSiteScripting.CCN);
		int ccnLimit = parser.getIntParameter(CrossSiteScripting.CCN_LIMIT);
		String disciplinaryActionDate = parser.getDateParameter(CrossSiteScripting.DISCIPLINARY_DATE);
		String disciplinaryActionNotes = parser.getStrictAlphaParameter(CrossSiteScripting.DISCIPLINARY_NOTES, 60);
		String personalDescription = parser.getStrictAlphaParameter(CrossSiteScripting.DESCRIPTION, 60);

		Employee employee = new Employee(subjectId, firstName, lastName, ssn, title, phone, address1, address2,
				manager, startDate, salary, ccn, ccnLimit, disciplinaryActionDate, disciplinaryActionNotes,
				personalDescription);

		return employee;
	}

}
