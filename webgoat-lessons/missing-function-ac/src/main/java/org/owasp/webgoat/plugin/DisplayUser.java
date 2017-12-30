package org.owasp.webgoat.plugin;


import lombok.Getter;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.security.core.GrantedAuthority;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 */


public class DisplayUser {
    //intended to provide a display version of WebGoatUser for admins to view user attributes


    private String username;
    private boolean admin;
    private String userHash;

    public DisplayUser(WebGoatUser user) {
        this.username = user.getUsername();
        this.admin = false;

        for (GrantedAuthority authority : user.getAuthorities()) {
            this.admin = (authority.getAuthority().contains("WEBGOAT_ADMIN")) ? true : false;
        }

        // create userHash on the fly
        //TODO: persist userHash
        try {
            this.userHash = genUserHash(user.getUsername(), user.getPassword());
        } catch (Exception ex) {
            //TODO: implement better fallback operation
            this.userHash = "Error generating user hash";
        }

    }

    protected String genUserHash (String username, String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // salting is good, but static & too predictable ... short too for a salt
        String salted = password + "DeliberatelyInsecure1234" + username;
        //md.update(salted.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] hash = md.digest(salted.getBytes("UTF-8"));
        String encoded = Base64.getEncoder().encodeToString(hash);
        return encoded;
    }


    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getUserHash() {
        return userHash;
    }

}
