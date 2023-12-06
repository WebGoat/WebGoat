/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
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
