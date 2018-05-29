package org.owasp.webgoat.users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nbaars
 * @since 4/30/17.
 */
public interface UserTrackerRepository extends JpaRepository<UserTracker, String> {

    UserTracker findByUser(String user);

}
