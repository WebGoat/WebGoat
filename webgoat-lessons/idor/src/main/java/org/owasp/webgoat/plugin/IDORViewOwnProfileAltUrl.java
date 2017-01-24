package org.owasp.webgoat.plugin;


import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.endpoints.AssignmentPath;
import org.owasp.webgoat.endpoints.Endpoint;
import org.owasp.webgoat.lessons.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
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

@AssignmentPath("IDOR/profile/alt-path")
public class IDORViewOwnProfileAltUrl extends AssignmentEndpoint{

    @Autowired
    UserSessionData userSessionData;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String url, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object> details = new HashMap<>();
        try {
            if (userSessionData.getValue("idor-authenticated-as").equals("tom")) {
                //going to use session auth to view this one
                String authUserId = (String)userSessionData.getValue("idor-authenticated-user-id");
                //don't care about http://localhost:8080 ... just want WebGoat/
                String[] urlParts = url.split("/");
                if (urlParts[0].equals("WebGoat") && urlParts[1].equals("IDOR") && urlParts[2].equals("profile") && urlParts[3].equals(authUserId)) {
                    UserProfile userProfile = new UserProfile(authUserId);
                    return trackProgress(AttackResult.success("congratultions, you have used the alternate Url/route to view your own profile.",userProfile.profileToMap().toString()));
                } else {
                    return trackProgress(AttackResult.failed("please try again. The alternoute route is very similar to the previous way you viewed your profile. Only one difference really"));
                }

            } else {
                return trackProgress(AttackResult.failed("You need to authenticate as tom first."));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return AttackResult.failed("fall back");
    }

}
