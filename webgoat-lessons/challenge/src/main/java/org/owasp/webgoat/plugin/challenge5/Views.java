package org.owasp.webgoat.plugin.challenge5;

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
