package org.owasp.webwolf.mailbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nbaars
 * @since 8/17/17.
 */
public interface MailboxRepository extends JpaRepository<Email, String> {

    List<Email> findByRecipientOrderByTimeDesc(String recipient);

}
