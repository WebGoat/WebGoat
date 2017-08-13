package org.owasp.webgoat.login;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Data
@AllArgsConstructor
public class LoginEvent {
    private String user;
    private String cookie;
}
