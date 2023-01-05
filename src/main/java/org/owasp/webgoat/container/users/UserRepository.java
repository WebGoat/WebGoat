package org.owasp.webgoat.container.users;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nbaars
 * @since 3/19/17.
 */
public interface UserRepository extends JpaRepository<WebGoatUser, String> {

  WebGoatUser findByUsername(String username);

  List<WebGoatUser> findAll();

  boolean existsByUsername(String username);
}
