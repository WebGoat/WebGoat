package org.owasp.webgoat.session;

public class Role {
    private String rolename;

    public Role(String rolename) {
        this.rolename = rolename;
    }

    public String getRolename() {
        return this.rolename;
    }
}