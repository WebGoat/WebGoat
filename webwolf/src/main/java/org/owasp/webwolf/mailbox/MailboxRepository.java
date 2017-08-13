package org.owasp.webwolf.mailbox;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author nbaars
 * @since 8/17/17.
 */
public interface MailboxRepository extends MongoRepository<Email, ObjectId> {

    List<Email> findByRecipientOrderByTimeDesc(String recipient);

}
