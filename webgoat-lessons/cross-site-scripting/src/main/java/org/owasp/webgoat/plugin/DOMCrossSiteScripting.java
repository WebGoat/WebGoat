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
@AssignmentPath("/CrossSiteScripting/dom-xss")
public class DOMCrossSiteScripting extends AssignmentEndpoint {
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam Integer param1,
                           @RequestParam Integer param2, HttpServletRequest request)
            throws IOException {
        
        if (param1 == 42 && param2 == 24 && request.getHeader("webgoat-requested-by").equals("dom-xss-vuln")) {
            return trackProgress(success().build());
        } else {
            return trackProgress(failed().build());
        }
    }
}





