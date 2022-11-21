package org.owasp.webgoat.lessons.jwt.votes;

/**
 * @author nbaars
 * @since 4/30/17.
 */
public class Views {
    public interface GuestView {
    }

    public interface UserView extends GuestView {
    }
}
