/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf.mailbox;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MailboxRepositoryTest {

  @Autowired private MailboxRepository mailboxRepository;

  @Test
  void emailShouldBeSaved() {
    Email email = new Email();
    email.setTime(LocalDateTime.now());
    email.setTitle("test");
    email.setSender("test@test.com");
    email.setContents("test");
    email.setRecipient("someone@webwolf.org");
    mailboxRepository.save(email);
  }

  @Test
  void savedEmailShouldBeFoundByReceipient() {
    Email email = new Email();
    email.setTime(LocalDateTime.now());
    email.setTitle("test");
    email.setSender("test@test.com");
    email.setContents("test");
    email.setRecipient("someone@webwolf.org");
    mailboxRepository.saveAndFlush(email);

    List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc("someone@webwolf.org");

    assertThat(emails.size()).isEqualTo(1);
  }
}
