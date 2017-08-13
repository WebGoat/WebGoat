package org.owasp.webwolf.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author nbaars
 * @since 8/20/17.
 */
public interface WebGoatUserToCookieRepository extends MongoRepository<WebGoatUserCookie, String> {

    Optional<WebGoatUserCookie> findByCookie(String cookie);
}
