package org.owasp.webgoat.login;

import lombok.AllArgsConstructor;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AllArgsConstructor
@Component
public class LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private JmsTemplate jmsTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            WebGoatUser user = (WebGoatUser) authentication.getPrincipal();
            jmsTemplate.convertAndSend("webgoat", new LogoutEvent(user.getUsername()), m -> {
                m.setStringProperty("type", LogoutEvent.class.getSimpleName());
                return m;
            });
        }
        super.onLogoutSuccess(httpServletRequest, httpServletResponse, authentication);
    }

    private Optional<Cookie> findSessionCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }
}
