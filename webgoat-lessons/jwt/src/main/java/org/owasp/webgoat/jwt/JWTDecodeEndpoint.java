package org.owasp.webgoat.jwt;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTDecodeEndpoint extends AssignmentEndpoint {

    @PostMapping("/JWT/decode")
    @ResponseBody
    public AttackResult decode(@RequestParam("jwt-encode-user") String user) {
        if ("user".equals(user)) {
            return success(this).build();
        } else {
            return failed(this).build();
        }
    }
}
