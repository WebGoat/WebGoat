package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@AssignmentPath("/access-control/user-hash")
@AssignmentHints({"access-control.hash.hint1","access-control.hash.hint2","access-control.hash.hint3",
        "access-control.hash.hint4","access-control.hash.hint5","access-control.hash.hint6","access-control.hash.hint7",
        "access-control.hash.hint8","access-control.hash.hint9","access-control.hash.hint10","access-control.hash.hint11","access-control.hash.hint12"})
public class MissingFunctionACYourHash extends AssignmentEndpoint {

    @Autowired
    private UserService userService;

    @PostMapping(produces = {"application/json"})
    public @ResponseBody
    AttackResult completed(String userHash) {
        String currentUser = getWebSession().getUserName();
        WebGoatUser user = userService.loadUserByUsername(currentUser);
        DisplayUser displayUser = new DisplayUser(user);
        if (userHash.equals(displayUser.getUserHash())) {
            return trackProgress(success().feedback("access-control.hash.success").build());
        } else {
            return trackProgress(failed().feedback("access-control.hash.close").build());
        }
    }
}
