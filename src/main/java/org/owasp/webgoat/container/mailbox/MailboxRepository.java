/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.mailbox;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailboxRepository extends JpaRepository<Email, String> {

  List<Email> findByRecipientOrderByTimeDesc(String recipient);

  int countByRecipientAndReadFalse(String recipient);
}
