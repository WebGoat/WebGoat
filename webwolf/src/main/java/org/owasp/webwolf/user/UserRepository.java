package org.owasp.webwolf.user;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author nbaars
 * @since 3/19/17.
 */
public interface UserRepository extends MongoRepository<WebGoatUser, String> {

    WebGoatUser findByUsername(String username);
}
