package org.owasp.webgoat.plugin;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.mail.IncomingMailEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AssignmentPath("/WebWolf/mail")
@AllArgsConstructor
public class MailAssignment extends AssignmentEndpoint {

    private JmsTemplate jmsTemplate;

    @PostMapping("send")
    @ResponseBody
    public AttackResult sendEmail(@RequestParam String email) {
        String username = email.substring(0, email.indexOf("@"));
        if (username.equals(getWebSession().getUserName())) {
            IncomingMailEvent mailEvent = IncomingMailEvent.builder()
                    .recipient(username)
                    .title("Test messages from WebWolf")
                    .time(LocalDateTime.now())
                    .contents("This is a test message from WebWolf, your unique code is" + StringUtils.reverse(username))
                    .sender("webgoat@owasp.org")
                    .build();
            jmsTemplate.convertAndSend("mailbox", mailEvent);
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
