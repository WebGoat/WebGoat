

package org.owasp.webgoat.webwolf.mailbox;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nbaars
 * @since 8/17/17.
 */
public interface MailboxRepository extends JpaRepository<Email, String> {

  List<Email> findByRecipientOrderByTimeDesc(String recipient);
}
