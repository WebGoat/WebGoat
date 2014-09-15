
package org.owasp.webgoat.lessons.GoatHillsFinancial;

import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;


public interface LessonAction
{
    public void handleRequest(WebSession s) throws ParameterNotFoundException, UnauthenticatedException,
            UnauthorizedException, ValidationException;

    public String getNextPage(WebSession s);

    public String getActionName();

    public boolean requiresAuthentication();

    public boolean isAuthenticated(WebSession s);

    public boolean isAuthorized(WebSession s, int employeeId, String functionId);

    public int getUserId(WebSession s) throws ParameterNotFoundException;

    public String getUserName(WebSession s) throws ParameterNotFoundException;
}
