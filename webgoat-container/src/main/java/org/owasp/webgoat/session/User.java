package org.owasp.webgoat.session;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>User class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class User {
    private String username;
    private ArrayList<Role> roles;

    /**
     * <p>Constructor for User.</p>
     *
     * @param username a {@link java.lang.String} object.
     */
    public User(String username) {
        this.username = username;
        this.roles = new ArrayList<Role>();
    }

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Getter for the field <code>roles</code>.</p>
     *
     * @return a {@link java.util.Iterator} object.
     */
    public Iterator<Role> getRoles() {
        return roles.iterator();
    }

    /**
     * <p>addRole.</p>
     *
     * @param rolename a {@link java.lang.String} object.
     */
    public void addRole(String rolename) {
        roles.add(new Role(rolename));
    }
}
