package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SecureRandom;


@AssignmentPath("/CrossSiteScripting/phone-home-xss")
public class DOMCrossSiteScripting extends AssignmentEndpoint {
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam Integer param1,
                           @RequestParam Integer param2, HttpServletRequest request)  throws IOException {

        UserSessionData userSessionData = getUserSessionData();
        SecureRandom number = new SecureRandom();
        userSessionData.setValue("randValue",number.nextInt());

        if (param1 == 42 && param2 == 24 && request.getHeader("webgoat-requested-by").equals("dom-xss-vuln")) {
            System.out.println(userSessionData.getValue("randValue") + " << randValue");
            return trackProgress(success().output("phoneHome Response is " + userSessionData.getValue("randValue").toString()).build());
        } else {
            return trackProgress(failed().build());
        }
    }
}
// something like ... http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere%3Cscript%3Ewebgoat.customjs.phoneHome();%3C%2Fscript%3E--andMoreGarbageHere
// or http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere<script>webgoat.customjs.phoneHome();<%2Fscript>