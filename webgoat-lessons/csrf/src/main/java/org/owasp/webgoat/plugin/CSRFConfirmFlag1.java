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
@AssignmentHints({"csrf-get.hint1","csrf-get.hint2","csrf-get.hint3","csrf-get.hint4"})
public class CSRFConfirmFlag1 extends AssignmentEndpoint {

    @Autowired
    UserSessionData userSessionData;

    @PostMapping(produces = {"application/json"})
    public @ResponseBody AttackResult completed(String confirmFlagVal) {

        if (confirmFlagVal.equals(userSessionData.getValue("csrf-get-success").toString())) {
            return success().feedback("csrf-get-null-referer.success").output("Correct, the flag was " + userSessionData.getValue("csrf-get-success")).build();
        }

        return  failed().feedback("").build();
    }
}
