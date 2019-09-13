package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class QuestionsAssignment extends AssignmentEndpoint {

    private final static Map<String, String> COLORS = new HashMap<>();

    static {
        COLORS.put("admin", "green");
        COLORS.put("jerry", "orange");
        COLORS.put("tom", "purple");
        COLORS.put("larry", "yellow");
        COLORS.put("webgoat", "red");
    }

    @PostMapping(path = "/PasswordReset/questions", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public AttackResult passwordReset(@RequestParam Map<String, Object> json) {
        String securityQuestion = (String) json.getOrDefault("securityQuestion", "");
        String username = (String) json.getOrDefault("username", "");

        if ("webgoat".equalsIgnoreCase(username.toLowerCase())) {
            return trackProgress(failed().feedback("password-questions-wrong-user").build());
        }

        String validAnswer = COLORS.get(username.toLowerCase());
        if (validAnswer == null) {
            return trackProgress(failed().feedback("password-questions-unknown-user").feedbackArgs(username).build());
        } else if (validAnswer.equals(securityQuestion)) {
            return trackProgress(success().build());
        }
        return trackProgress(failed().build());
    }
}
