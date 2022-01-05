/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.idor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 1/5/17.
 */
public class UserProfile {
    private String userId;
    private String name;
    private String color;
    private String size;
    private boolean isAdmin;
    private int role;

    public UserProfile() {}

    public UserProfile(String id) {
        setProfileFromId(id);
    }

    //
    private void setProfileFromId(String id) {
        // emulate look up from database
        if (id.equals("2342384")) {
            this.userId = id;
            this.color = "yellow";
            this.name = "Tom Cat";
            this.size = "small";
            this.isAdmin = false;
            this.role = 3;
        } else if (id.equals("2342388")) {
            this.userId = id;
            this.color = "brown";
            this.name = "Buffalo Bill";
            this.size = "large";
            this.isAdmin = false;
            this.role = 3;
        } else {
            //not found
        }

    }

    public Map <String,Object> profileToMap () {
        Map<String,Object> profileMap = new HashMap<>();
        profileMap.put("userId", this.userId);
        profileMap.put("name", this.name);
        profileMap.put("color", this.color);
        profileMap.put("size", this.size);
        profileMap.put("role", this.role);
        return profileMap;
    }

    public String toHTMLString() {
        String htmlBreak = "<br/>";
        return "userId" + this.userId + htmlBreak +
                "name" + this.name + htmlBreak +
                "size" + this.size + htmlBreak +
                "role" + this.role + htmlBreak +
                "isAdmin" + this.isAdmin;
    }

    //
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}
