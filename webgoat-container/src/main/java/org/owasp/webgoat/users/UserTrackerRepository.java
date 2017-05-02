package org.owasp.webgoat.users;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author nbaars
 * @since 4/30/17.
 */
public interface UserTrackerRepository extends MongoRepository<UserTracker, String> {


}
