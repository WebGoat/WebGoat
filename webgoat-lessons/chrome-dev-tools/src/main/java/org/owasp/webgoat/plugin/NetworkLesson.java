package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Assignment where the user has to look through an HTTP Request
 * using the Developer Tools and find a specific number.
 *
 * @author TMelzer
 * @since 30.11.18
 */
@RestController
@AssignmentHints({"networkHint1", "networkHint2"})
public class NetworkLesson extends AssignmentEndpoint {

    @PostMapping(value = "/ChromeDevTools/network", params = {"network_num", "number"})
    @ResponseBody
    public AttackResult completed(@RequestParam String network_num, @RequestParam String number) {
        if (network_num.equals(number)) {
            return trackProgress(success().feedback("network.success").output("").build());
        } else {
            return trackProgress(failed().feedback("network.failed").build());
        }
    }

    @PostMapping(path = "/ChromeDevTools/network", params = "networkNum")
    @ResponseBody
    public ResponseEntity<?> ok(@RequestParam String networkNum) {
        return ResponseEntity.ok().build();
    }
}
