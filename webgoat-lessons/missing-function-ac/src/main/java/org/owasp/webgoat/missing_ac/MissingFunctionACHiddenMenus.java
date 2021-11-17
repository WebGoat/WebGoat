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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jason on 1/5/17.
 */
@RestController
@AssignmentHints({"access-control.hidden-menus.hint1","access-control.hidden-menus.hint2","access-control.hidden-menus.hint3"})
public class MissingFunctionACHiddenMenus extends AssignmentEndpoint {

    @PostMapping(path = "/access-control/hidden-menu", produces = {"application/json"})
    @ResponseBody
    public AttackResult completed(String hiddenMenu1, String hiddenMenu2) {
        if (hiddenMenu1.equals("Users") && hiddenMenu2.equals("Config")) {
            return success(this)
                    .output("")
                    .feedback("access-control.hidden-menus.success")
                    .build();
        }

        if (hiddenMenu1.equals("Config") && hiddenMenu2.equals("Users")) {
            return failed(this)
                    .output("")
                    .feedback("access-control.hidden-menus.close")
                    .build();
        }

        return failed(this)
                .feedback("access-control.hidden-menus.failure")
                .output("")
                .build();
    }
}
