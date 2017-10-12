package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jason on 9/29/17.
 */

@AssignmentPath("/csrf/confirm-flag-1")
@AssignmentHints({""})
public class CSRFConfirmFlag1 extends AssignmentEndpoint {

    @Autowired
    UserSessionData userSessionData;

    @PostMapping(produces = {"application/json"})
    public @ResponseBody AttackResult completed(String confirmFlagVal) {
//        String host = (req.getHeader("host") == null) ? "NULL" : req.getHeader("host");
//        String origin = (req.getHeader("origin") == null) ? "NULL" : req.getHeader("origin");
//        Integer serverPort = (req.getServerPort() < 1) ? 0 : req.getServerPort();
//        String serverName = (req.getServerName() == null) ? "NULL" : req.getServerName();
//        String referer = (req.getHeader("referer") == null) ? "NULL" : req.getHeader("referer");

        if (confirmFlagVal.equals(userSessionData.getValue("csrf-get-success"))) {
            return success().feedback("csrf-get-success").output("Correct, the flag was " + userSessionData.getValue("csrf-get-success")).build();
        }
        return  failed().feedback("").build();
    }
}
