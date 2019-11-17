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

package org.owasp.webgoat.bypass_restrictions;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BypassRestrictionsFieldRestrictions extends AssignmentEndpoint {

    @PostMapping("/BypassRestrictions/FieldRestrictions")
    @ResponseBody
    public AttackResult completed(@RequestParam String select, @RequestParam String radio, @RequestParam String checkbox, @RequestParam String shortInput) {
        if (select.equals("option1") || select.equals("option2")) {
            return failed(this).build();
        }
        if (radio.equals("option1") || radio.equals("option2")) {
            return failed(this).build();
        }
        if (checkbox.equals("on") || checkbox.equals("off")) {
            return failed(this).build();
        }
        if (shortInput.length() <= 5) {
            return failed(this).build();
        }
        return success(this).build();
    }
}
