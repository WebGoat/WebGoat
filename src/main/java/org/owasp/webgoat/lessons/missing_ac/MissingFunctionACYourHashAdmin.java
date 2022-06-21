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

package org.owasp.webgoat.lessons.missing_ac;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.owasp.webgoat.lessons.missing_ac.MissingFunctionAC.PASSWORD_SALT_ADMIN;

@RestController
@AssignmentHints({"access-control.hash.hint6", "access-control.hash.hint7",
        "access-control.hash.hint8", "access-control.hash.hint9", "access-control.hash.hint10", "access-control.hash.hint11", "access-control.hash.hint12"})
public class MissingFunctionACYourHashAdmin extends AssignmentEndpoint {

    private final MissingAccessControlUserRepository userRepository;

    public MissingFunctionACYourHashAdmin(MissingAccessControlUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/access-control/user-hash-fix", produces = {"application/json"})
    @ResponseBody
    public AttackResult admin(String userHash) {
        //current user should be in the DB
        //if not admin then return 403

        var user = userRepository.findByUsername("Jerry");
        var displayUser = new DisplayUser(user, PASSWORD_SALT_ADMIN);
        if (userHash.equals(displayUser.getUserHash())) {
            return success(this).feedback("access-control.hash.success").build();
        } else {
            return failed(this).feedback("access-control.hash.close").build();
        }
    }

}
