package org.owasp.webwolf.mailbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.mail.IncomingMailEvent;
import org.owasp.webwolf.user.UserRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@Component
@AllArgsConstructor
@Slf4j
public class MailboxListener {

    private final MailboxRepository repository;
    private final UserRepository userRepository;

    @JmsListener(destination = "mailbox", containerFactory = "jmsFactory")
    public void incomingMail(IncomingMailEvent event) {
        if (userRepository.findByUsername(event.getRecipient()) != null) {
            Email email = Email.builder()
                    .contents(event.getContents())
                    .sender(event.getSender())
                    .time(event.getTime())
                    .recipient(event.getRecipient())
                    .title(event.getTitle()).build();
            repository.save(email);
        } else {
            log.trace("Mail received for unknown user: {}", event.getRecipient());
        }

    }
}
