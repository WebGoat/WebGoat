
package org.owasp.webgoat.lessons.instructor.CrossSiteScripting;

import org.owasp.webgoat.lessons.CrossSiteScripting.ViewProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;


// STAGE 4 FIXES
//
//Solution Summary: Look in the WebContent/lesson/CrossSiteScripting/ViewProfile.jsp
//
//Look for the <-- STAGE 4 - FIX    in the ViewProfile.jsp
//
//

public class ViewProfile_i extends ViewProfile
{
    public ViewProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName)
    {
        super(lesson, lessonName, actionName);
    }
}
