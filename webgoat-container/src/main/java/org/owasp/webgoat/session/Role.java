package org.owasp.webgoat.session;

/**
 * <p>Role class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class Role {
    private String rolename;

    /**
     * <p>Constructor for Role.</p>
     *
     * @param rolename a {@link java.lang.String} object.
     */
    public Role(String rolename) {
        this.rolename = rolename;
    }

    /**
     * <p>Getter for the field <code>rolename</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRolename() {
        return this.rolename;
    }
}
