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

package org.owasp.webgoat.idor;


import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class IDORViewOwnProfile {

    @Autowired
    UserSessionData userSessionData;

    @GetMapping(path = {"/IDOR/own", "/IDOR/profile"}, produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> invoke() {
        Map<String,Object> details = new HashMap<>();
        try {
            if (userSessionData.getValue("idor-authenticated-as").equals("tom")) {
                //going to use session auth to view this one
                String authUserId = (String)userSessionData.getValue("idor-authenticated-user-id");
                UserProfile userProfile = new UserProfile(authUserId);
                details.put("userId",userProfile.getUserId());
                details.put("name",userProfile.getName());
                details.put("color",userProfile.getColor());
                details.put("size",userProfile.getSize());
                details.put("role",userProfile.getRole());
            } else {
                details.put("error","You do not have privileges to view the profile. Authenticate as tom first please.");
            }
        }catch (Exception ex) {
            log.error("something went wrong", ex.getMessage());
        }
        return details;
    }
}
