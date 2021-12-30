package org.owasp.webgoat.container.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nbaars
 * @since 3/19/17.
 */
public interface UserRepository extends JpaRepository<WebGoatUser, String> {

    WebGoatUser findByUsername(String username);

    List<WebGoatUser> findAll();

    boolean existsByUsername(String username);

}
