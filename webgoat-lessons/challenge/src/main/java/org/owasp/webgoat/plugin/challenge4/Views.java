package org.owasp.webgoat.plugin.challenge4;

/**
 * @author nbaars
 * @since 4/30/17.
 */
public class Views {
    interface GuestView {
    }

    interface UserView extends GuestView {
    }

    interface AdminView extends UserView {
    }
}
