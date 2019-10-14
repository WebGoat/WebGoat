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

package org.owasp.webgoat.csrf;

import javax.servlet.http.HttpServletRequest;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nbaars
 * @since 11/17/17.
 */
@RestController
@AssignmentHints({"csrf-login-hint1", "csrf-login-hint2", "csrf-login-hint3"})
public class CSRFLogin extends AssignmentEndpoint {

    @Autowired
    private UserTrackerRepository userTrackerRepository;

    @PostMapping(path = "/csrf/login", produces = {"application/json"})
    @ResponseBody
    public AttackResult completed(HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        if (userName.startsWith("csrf")) {
            markAssignmentSolvedWithRealUser(userName.substring("csrf-".length()));
            return trackProgress(success().feedback("csrf-login-success").build());
        }
        return trackProgress(failed().feedback("csrf-login-failed").feedbackArgs(userName).build());
    }

    private void markAssignmentSolvedWithRealUser(String username) {
        UserTracker userTracker = userTrackerRepository.findByUser(username);
        userTracker.assignmentSolved(getWebSession().getCurrentLesson(), this.getClass().getSimpleName());
        userTrackerRepository.save(userTracker);
    }
}
