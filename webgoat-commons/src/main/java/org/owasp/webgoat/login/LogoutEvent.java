package org.owasp.webgoat.login;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AllArgsConstructor
@Data
public class LogoutEvent {
    private String user;
}