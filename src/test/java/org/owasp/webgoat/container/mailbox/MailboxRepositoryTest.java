/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.mailbox;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(
    properties = {
      // The mailbox entity maps to the CONTAINER schema. Let Hibernate build that schema and the
      // email table on the embedded test database (Flyway is not needed for this slice).
      "spring.flyway.enabled=false",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.properties.hibernate.default_schema=CONTAINER",
      "spring.jpa.properties.hibernate.hbm2ddl.create_namespaces=true"
    })
public class MailboxRepositoryTest {

  @Autowired private MailboxRepository mailboxRepository;

  @Test
  void emailShouldBeSaved() {
    Email email = new Email();
    email.setTime(LocalDateTime.now());
    email.setTitle("test");
    email.setSender("test@test.com");
    email.setContents("test");
    email.setRecipient("someone@webgoat.org");
    mailboxRepository.save(email);
  }

  @Test
  void savedEmailShouldBeFoundByReceipient() {
    Email email = new Email();
    email.setTime(LocalDateTime.now());
    email.setTitle("test");
    email.setSender("test@test.com");
    email.setContents("test");
    email.setRecipient("someone@webgoat.org");
    mailboxRepository.saveAndFlush(email);

    List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc("someone@webgoat.org");

    assertThat(emails.size()).isEqualTo(1);
  }
}
