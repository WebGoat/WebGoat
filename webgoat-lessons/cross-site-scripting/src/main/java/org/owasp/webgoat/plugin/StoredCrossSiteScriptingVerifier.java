package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by jason on 11/23/16.
 */
@AssignmentPath("/CrossSiteScripting/stored-xss-follow-up")
public class StoredCrossSiteScriptingVerifier extends AssignmentEndpoint {
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam String successMessage)  throws IOException {

        UserSessionData userSessionData = getUserSessionData();

        if (successMessage.equals(userSessionData.getValue("randValue").toString())) {
            return trackProgress(success().feedback("xss-stored-callback-success").build());
        } else {
            return trackProgress(failed().feedback("xss-stored-callback-failure").build());
        }
    }
}
// something like ... http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere%3Cscript%3Ewebgoat.customjs.phoneHome();%3C%2Fscript%3E
// or http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere<script>webgoat.customjs.phoneHome();<%2Fscript>