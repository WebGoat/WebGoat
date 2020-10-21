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

package org.owasp.webgoat.missing_ac;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"access-control.hash.hint1","access-control.hash.hint2","access-control.hash.hint3",
        "access-control.hash.hint4","access-control.hash.hint5","access-control.hash.hint6","access-control.hash.hint7",
        "access-control.hash.hint8","access-control.hash.hint9","access-control.hash.hint10","access-control.hash.hint11","access-control.hash.hint12"})
public class MissingFunctionACYourHash extends AssignmentEndpoint {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/access-control/user-hash", produces = {"application/json"})
    @ResponseBody
    public AttackResult completed(String userHash) {
        String currentUser = getWebSession().getUserName();
        WebGoatUser user = userService.loadUserByUsername(currentUser);
        DisplayUser displayUser = new DisplayUser(user);
        if (userHash.equals(displayUser.getUserHash())) {
            return success(this).feedback("access-control.hash.success").build();
        } else {
            return failed(this).feedback("access-control.hash.close").build();
        }
    }
}
