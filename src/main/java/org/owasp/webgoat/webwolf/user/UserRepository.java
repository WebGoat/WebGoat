

package org.owasp.webgoat.webwolf.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Repository("webWolfUserRepository")
public interface UserRepository extends JpaRepository<WebWolfUser, String> {

  WebWolfUser findByUsername(String username);
}
