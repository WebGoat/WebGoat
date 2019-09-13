package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * This is just a class used to make the the HTTP request.
 *
 * @author TMelzer
 * @since 30.11.18
 */
@RestController
public class NetworkDummy extends AssignmentEndpoint {

    @PostMapping("/ChromeDevTools/dummy")
    @ResponseBody
    public AttackResult completed(@RequestParam String successMessage) {
        UserSessionData userSessionData = getUserSessionData();
        String answer = (String) userSessionData.getValue("randValue");

        if (successMessage != null && successMessage.equals(answer)) {
            return trackProgress(success().feedback("xss-dom-message-success").build());
        } else {
            return trackProgress(failed().feedback("xss-dom-message-failure").build());
        }
    }
}