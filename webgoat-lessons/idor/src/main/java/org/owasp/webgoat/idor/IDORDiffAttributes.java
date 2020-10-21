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

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.*;

@RestController
@AssignmentHints({"idor.hints.idorDiffAttributes1", "idor.hints.idorDiffAttributes2", "idor.hints.idorDiffAttributes3"})
public class IDORDiffAttributes extends AssignmentEndpoint {

    @PostMapping("/IDOR/diff-attributes")
    @ResponseBody
    public AttackResult completed(@RequestParam String attributes) {
        attributes = attributes.trim();
        String[] diffAttribs = attributes.split(",");
        if (diffAttribs.length < 2) {
            return failed(this).feedback("idor.diff.attributes.missing").build();
        }
        if (diffAttribs[0].toLowerCase().trim().equals("userid") && diffAttribs[1].toLowerCase().trim().equals("role")
                || diffAttribs[1].toLowerCase().trim().equals("userid") && diffAttribs[0].toLowerCase().trim().equals("role")) {
            return success(this).feedback("idor.diff.success").build();
        } else {
            return failed(this).feedback("idor.diff.failure").build();
        }
    }
}
