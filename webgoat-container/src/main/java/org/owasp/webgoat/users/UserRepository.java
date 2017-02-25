package org.owasp.webgoat.users;

import org.owasp.webgoat.session.WebGoatUser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author nbaars
 * @since 3/19/17.
 */
public interface UserRepository extends CrudRepository<WebGoatUser, Long> {

    WebGoatUser findByUsername(String username);
}
