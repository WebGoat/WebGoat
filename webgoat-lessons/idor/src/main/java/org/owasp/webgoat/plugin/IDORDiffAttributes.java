package org.owasp.webgoat.plugin;

import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.lessons.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.io.IOException;

/**
 * Created by jason on 1/5/17.
 */
@Path("IDOR/diff-attributes")
public class IDORDiffAttributes extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam String attributes, HttpServletRequest request) throws IOException {
        attributes = attributes.trim();
        String[] diffAttribs = attributes.split(",");
        if (diffAttribs.length < 2) {
            return AttackResult.failed("You did not list two attributes string delimited");
        }
        if (diffAttribs[0].toLowerCase().trim().equals("userid") && diffAttribs[1].toLowerCase().trim().equals("role") ||
                diffAttribs[1].toLowerCase().trim().equals("userid") && diffAttribs[0].toLowerCase().trim().equals("role")) {
            return trackProgress(AttackResult.success("Correct, the two attributes not displayed are userId & role. Keep those in mind"));
        } else {
            return trackProgress(AttackResult.failed("Try again. Look in your browser dev tools or Proxy and compare to what's displayed on the screen."));
        }
    }
}
