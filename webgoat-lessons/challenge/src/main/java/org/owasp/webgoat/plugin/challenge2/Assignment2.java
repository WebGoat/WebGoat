package org.owasp.webgoat.plugin.challenge2;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.Flag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import static org.owasp.webgoat.plugin.SolutionConstants.SUPER_COUPON_CODE;

/**
 * @author nbaars
 * @since 4/6/17.
 */
@AssignmentPath("/challenge/2")
public class Assignment2 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String checkoutCode) throws IOException {
        if (SUPER_COUPON_CODE.equals(checkoutCode)) {
            return success().feedback("challenge.solved").feedbackArgs(Flag.FLAGS.get(2)).build();
        }
        return failed().build();
    }
}
