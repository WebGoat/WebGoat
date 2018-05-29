package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nbaars
 * @since 11/17/17.
 */
@AssignmentPath("/csrf/login")
@AssignmentHints({"csrf-login-hint1", "csrf-login-hint2", "csrf-login-hint3"})
public class CSRFLogin extends AssignmentEndpoint {

    @Autowired
    private UserTrackerRepository userTrackerRepository;

    @PostMapping(produces = {"application/json"})
    @ResponseBody
    public AttackResult completed() {
        String userName = getWebSession().getUserName();
        if (userName.startsWith("csrf")) {
            markAssignmentSolvedWithRealUser(userName.substring("csrf-".length()));
            return trackProgress(success().feedback("csrf-login-success").build());
        }
        return trackProgress(failed().feedback("csrf-login-failed").feedbackArgs(userName).build());
    }

    private void markAssignmentSolvedWithRealUser(String username) {
        UserTracker userTracker = userTrackerRepository.findByUser(username);
        userTracker.assignmentSolved(getWebSession().getCurrentLesson(), this.getClass().getSimpleName());
        userTrackerRepository.save(userTracker);
    }
}
