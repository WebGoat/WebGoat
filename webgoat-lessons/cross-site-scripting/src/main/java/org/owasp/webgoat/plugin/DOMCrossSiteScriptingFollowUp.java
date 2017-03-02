package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jason on 11/23/16.
 */
@AssignmentPath("/CrossSiteScripting/dom-follow-up")
public class DOMCrossSiteScriptingFollowUp extends AssignmentEndpoint {
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam String successMessage)  throws IOException {
        if (successMessage.equals("DOM-XSS successful, param1 is 42")) {
            return trackProgress(success().feedback("xss-dom-message-success").build());
        } else {
            return trackProgress(failed().feedback("xss-dom-message-success").build());
        }
    }
}
// something like ... http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere%3Cscript%3Ewebgoat.customjs.phoneHome();%3C%2Fscript%3E
// or http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere<script>webgoat.customjs.phoneHome();<%2Fscript>





