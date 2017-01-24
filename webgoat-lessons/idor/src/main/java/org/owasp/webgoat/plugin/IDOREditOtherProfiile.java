package org.owasp.webgoat.plugin;

import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.endpoints.AssignmentPath;
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

@AssignmentPath("IDOR/profile/{userId}")
public class IDOREditOtherProfiile extends AssignmentEndpoint {

    @Autowired
    private UserSessionData userSessionData;

    @PutMapping(consumes = "application/json")
    public @ResponseBody
    AttackResult completed(@PathVariable("userId") String userId, @RequestBody UserProfile userSubmittedProfile) {

        String authUserId = (String)userSessionData.getValue("idor-authenticated-user-id");
        // this is where it starts ... accepting the user submitted ID and assuming it will be the same as the logged in userId and not checking for proper authorization
        // Certain roles can sometimes edit others' profiles, but we shouldn't just assume that and let everyone, right?
        // Except that this is a vulnerable app ... so we will
        UserProfile currentUserProfile = new UserProfile(userId);
        if (userSubmittedProfile.getUserId() != null && !userSubmittedProfile.getUserId().equals(authUserId)) {
            // let's get this started ...
            currentUserProfile.setColor(userSubmittedProfile.getColor());
            currentUserProfile.setRole(userSubmittedProfile.getRole());
            // we will persist in the session object for now in case we want to refer back or use it later
            userSessionData.setValue("idor-updated-other-profile",currentUserProfile);
            if (currentUserProfile.getRole() <= 1 && currentUserProfile.getColor().toLowerCase().equals("red")) {
                return trackProgress(AttackResult.success("Well done, you have modified someone else's profile (as displayed below)",currentUserProfile.profileToMap().toString()));
            }

            if (currentUserProfile.getRole() > 1 && currentUserProfile.getColor().toLowerCase().equals("red")) {
                return trackProgress(AttackResult.success("Close ... you've got the technique. Now try for a lower role number)",currentUserProfile.profileToMap().toString()));
            }

            if (currentUserProfile.getRole() <= 1 && !currentUserProfile.getColor().toLowerCase().equals("red")) {
                return trackProgress(AttackResult.success("Close ... you've got the technique. Now change the color in their profile to red.)",currentUserProfile.profileToMap().toString()));
            }

            // else
            return trackProgress(AttackResult.success("Try again. Use the hints if you need to.",currentUserProfile.profileToMap().toString()));

        } else if (userSubmittedProfile.getUserId().equals(authUserId)) {
            return AttackResult.failed("Modifying your own profile is good, but we want to do this to Buffalo Bill's profile.");
        }

        if (currentUserProfile.getColor().equals("black") && currentUserProfile.getRole() <= 1 ) {
            return trackProgress(AttackResult.success("Good work! View the updated profile below",userSessionData.getValue("idor-updated-own-profile").toString()));
        } else {
            return trackProgress(AttackResult.failed("Please try again. Use the hints if need be."));
        }

    }

}
