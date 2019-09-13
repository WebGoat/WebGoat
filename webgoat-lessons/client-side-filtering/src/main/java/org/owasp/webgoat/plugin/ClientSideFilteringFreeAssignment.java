package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author nbaars
 * @since 4/6/17.
 */
@RestController
@AssignmentHints({"client.side.filtering.free.hint1", "client.side.filtering.free.hint2", "client.side.filtering.free.hint3"})
public class ClientSideFilteringFreeAssignment extends AssignmentEndpoint {

    public static final String SUPER_COUPON_CODE = "get_it_for_free";

    @PostMapping("/clientSideFiltering/getItForFree")
    @ResponseBody
    public AttackResult completed(@RequestParam String checkoutCode) {
        if (SUPER_COUPON_CODE.equals(checkoutCode)) {
            return trackProgress(success().build());
        }
        return trackProgress(failed().build());
    }
}
