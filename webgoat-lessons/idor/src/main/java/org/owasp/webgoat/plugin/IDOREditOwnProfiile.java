package org.owasp.webgoat.plugin;

import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.lessons.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

@Path("IDOR/profile/{userId}")
public class IDOREditOwnProfiile extends AssignmentEndpoint {

    @Autowired UserSessionData userSessionData;

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public @ResponseBody
    AttackResult completed(@PathVariable("userId") String userId, @RequestParam UserProfile userSubmittedProfile, HttpServletRequest request) {

        String authUserId = (String)userSessionData.getValue("idor-authenticated-user-id");
        UserProfile currentUserProfile = new UserProfile(authUserId);
        if (userSubmittedProfile.getUserId() != null && !userSubmittedProfile.getUserId().equals(authUserId)) {
            return AttackResult.failed("Don't worry, we'll get to modifying someone else's profile, just modify your own for now.");
        } else if (userSubmittedProfile.getUserId().equals(authUserId)) {
            // this is commonly how vulnerable code will act ... updating w/out an authorization check
            currentUserProfile.setColor(userSubmittedProfile.getColor());
            currentUserProfile.setRole(userSubmittedProfile.getRole());
            // we will persist in the session object for now
            userSessionData.setValue("idor-updated-own-profile",currentUserProfile);


        }

        if (currentUserProfile.getColor().equals("black") && currentUserProfile.getRole() <= 1 ) {
            return trackProgress(AttackResult.success("Good work! View the updated profile below",userSessionData.getValue("idor-updated-own-profile").toString()));
        } else {
            return trackProgress(AttackResult.failed("Please try again. Use the hints if need be."));
        }

    }

}
