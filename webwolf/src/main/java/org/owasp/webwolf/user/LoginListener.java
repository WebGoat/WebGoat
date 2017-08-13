package org.owasp.webwolf.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.login.LoginEvent;
import org.owasp.webgoat.login.LogoutEvent;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Component
@Slf4j
@AllArgsConstructor
public class LoginListener {
    
    private final WebGoatUserToCookieRepository repository;

    @JmsListener(destination = "webgoat", containerFactory = "jmsFactory", selector = "type = 'LoginEvent'")
    public void loginEvent(LoginEvent loginEvent) {
        log.trace("Login event occurred for user: '{}'", loginEvent.getUser());
        repository.save(new WebGoatUserCookie(loginEvent.getUser(), loginEvent.getCookie()));
    }

    @JmsListener(destination = "webgoat", containerFactory = "jmsFactory", selector = "type = 'LogoutEvent'")
    public void logoutEvent(LogoutEvent logoutEvent) {
        repository.delete(logoutEvent.getUser());

    }

}