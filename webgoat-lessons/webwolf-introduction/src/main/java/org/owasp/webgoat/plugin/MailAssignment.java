package org.owasp.webgoat.plugin;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AssignmentPath("/WebWolf/mail")
public class MailAssignment extends AssignmentEndpoint {

    private final String webWolfURL;
    private RestTemplate restTemplate;

    public MailAssignment(RestTemplate restTemplate, @Value("${webwolf.url.mail}") String webWolfURL) {
        this.restTemplate = restTemplate;
        this.webWolfURL = webWolfURL;
    }

    @PostMapping("send")
    @ResponseBody
    public AttackResult sendEmail(@RequestParam String email) {
        String username = email.substring(0, email.indexOf("@"));
        if (username.equals(getWebSession().getUserName())) {
            Email mailEvent = Email.builder()
                    .recipient(username)
                    .title("Test messages from WebWolf")
                    .time(LocalDateTime.now())
                    .contents("This is a test message from WebWolf, your unique code is: " + StringUtils.reverse(username))
                    .sender("webgoat@owasp.org")
                    .build();
            try {
                restTemplate.postForEntity(webWolfURL, mailEvent, Object.class);
            } catch (RestClientException e ) {
                return informationMessage().feedback("webwolf.email_failed").output(e.getMessage()).build();
            }
            return informationMessage().feedback("webwolf.email_send").feedbackArgs(email).build();
        } else {
            return informationMessage().feedback("webwolf.email_mismatch").feedbackArgs(username).build();
        }
    }

    @PostMapping
    @ResponseBody
    public AttackResult completed(@RequestParam String uniqueCode) {
        if (uniqueCode.equals(StringUtils.reverse(getWebSession().getUserName()))) {
            return trackProgress(success().build());
        } else {
            return trackProgress(failed().feedbackArgs("webwolf.code_incorrect").feedbackArgs(uniqueCode).build());
        }
    }
}
