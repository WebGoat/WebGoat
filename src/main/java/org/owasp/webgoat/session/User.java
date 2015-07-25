package org.owasp.webgoat.session;

import java.util.ArrayList;
import java.util.Iterator;

public class User {
    private String username;
    private ArrayList<Role> roles;

    public User(String username) {
        this.username = username;
        this.roles = new ArrayList<Role>();
    }

    public String getUsername() {
        return username;
    }

    public Iterator<Role> getRoles() {
        return roles.iterator();
    }

    public void addRole(String rolename) {
        roles.add(new Role(rolename));
    }
}