package org.owasp.webgoat.plugin;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.PasswordResetEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AssignmentPath("/PasswordReset/questions")
public class QuestionsAssignment extends AssignmentEndpoint {

    private final static Map<String, String> COLORS = new HashMap<>();

    static {
        COLORS.put("admin", "green");
        COLORS.put("jerry", "orange");
        COLORS.put("tom", "purple");
        COLORS.put("larry", "yellow");
        COLORS.put("webgoat", "red");
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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
