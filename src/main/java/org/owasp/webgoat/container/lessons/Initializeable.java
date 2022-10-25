package org.owasp.webgoat.container.lessons;

import org.owasp.webgoat.container.users.WebGoatUser;

/**
 * Interface for initialization of a lesson. It is called when a new user is added to WebGoat and when a users
 * reset a lesson. Make sure to clean beforehand and then re-initialize the lesson.
 */
public interface Initializeable {

    void initialize(WebGoatUser webGoatUser);
}
