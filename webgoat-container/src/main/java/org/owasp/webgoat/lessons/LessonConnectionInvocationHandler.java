package org.owasp.webgoat.lessons;

import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * Handler which sets the correct schema for the currently bounded user. This way users are not seeing each other
 * data and we can reset data for just one particular user.
 */
public class LessonConnectionInvocationHandler implements InvocationHandler {

    private final Connection targetConnection;

    public LessonConnectionInvocationHandler(Connection targetConnection) {
        this.targetConnection = targetConnection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof WebGoatUser) {
            var user = (WebGoatUser) authentication.getPrincipal();
            targetConnection.createStatement().execute("SET SCHEMA \"" + user.getUsername() + "\"");
        }
        try {
            return method.invoke(targetConnection, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
