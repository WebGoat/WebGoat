package org.owasp.webwolf.mailbox;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class MailboxRepositoryTest {


    @Autowired
    private MailboxRepository mailboxRepository;

    @Test
    public void emailShouldBeSaved() {
        Email email = new Email();
        email.setTime(LocalDateTime.now());
        email.setTitle("test");
        email.setSender("test@test.com");
        email.setContents("test");
        email.setRecipient("someone@webwolf.org");
        mailboxRepository.save(email);
    }

    @Test
    public void savedEmailShouldBeFoundByReceipient() {
        Email email = new Email();
        email.setTime(LocalDateTime.now());
        email.setTitle("test");
        email.setSender("test@test.com");
        email.setContents("test");
        email.setRecipient("someone@webwolf.org");
        mailboxRepository.saveAndFlush(email);

        List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc("someone@webwolf.org");

        assertThat(emails.size(), CoreMatchers.is(1));
    }

}
