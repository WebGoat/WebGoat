package org.owasp.webgoat.users;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author nbaars
 * @since 3/19/17.
 */
public interface UserRepository extends MongoRepository<WebGoatUser, String> {

    WebGoatUser findByUsername(String username);

    List<WebGoatUser> findAll();

}
