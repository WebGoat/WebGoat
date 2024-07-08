package org.owasp.webgoat.container.users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nbaars
 * @since 4/30/17.
 */
public interface UserProgressRepository extends JpaRepository<UserProgress, String> {

  UserProgress findByUser(String user);
}
