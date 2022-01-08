package org.owasp.webgoat.lessons.missing_ac;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
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
 */
@Getter
public class DisplayUser {
    //intended to provide a display version of WebGoatUser for admins to view user attributes

    private String username;
    private boolean admin;
    private String userHash;

    public DisplayUser(User user, String passwordSalt) {
        this.username = user.getUsername();
        this.admin = user.isAdmin();

        try {
            this.userHash = genUserHash(user.getUsername(), user.getPassword(), passwordSalt);
        } catch (Exception ex) {
            this.userHash = "Error generating user hash";
        }
    }

    protected String genUserHash(String username, String password, String passwordSalt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // salting is good, but static & too predictable ... short too for a salt
        String salted = password + passwordSalt + username;
        //md.update(salted.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] hash = md.digest(salted.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

}
