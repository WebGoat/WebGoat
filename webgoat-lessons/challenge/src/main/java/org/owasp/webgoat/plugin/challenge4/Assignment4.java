package org.owasp.webgoat.plugin.challenge4;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@AssignmentPath("/challenge/4")
public class Assignment4 extends AssignmentEndpoint {

    @PutMapping  //assignment path is bounded to class so we use different http method :-)
    @ResponseBody
    public AttackResult test() {
        return success().build();
    }

    @RequestMapping(method = POST)
    @ResponseBody
    public AttackResult login(@RequestParam String username, @RequestParam String password) throws Exception {
        if (StringUtils.isAlphanumeric(username) && StringUtils.isAlphanumeric(password)) {
            return success().build();
        } else {
            return failed().build();
        }
    }

}

