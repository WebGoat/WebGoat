package org.owasp.webgoat.plugin;


import org.owasp.webgoat.assignments.Endpoint;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

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
 * @author misfir3
 * @version $Id: $Id
 * @since January 3, 2017
 */

public class IDORViewOwnProfile extends Endpoint{

    @Autowired
    UserSessionData userSessionData;

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> invoke(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            System.out.println(ex.getMessage());
        }
        return details;
    }

    @Override
    public String getPath() {
        return "/IDOR/profile";
    }
}
