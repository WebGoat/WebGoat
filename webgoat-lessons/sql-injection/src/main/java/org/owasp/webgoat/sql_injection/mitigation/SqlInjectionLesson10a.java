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

package org.owasp.webgoat.sql_injection.mitigation;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AssignmentHints(value = {"SqlStringInjectionHint-mitigation-10a-1", "SqlStringInjectionHint-mitigation-10a-10a2"})
public class SqlInjectionLesson10a extends AssignmentEndpoint {

    private String[] results = {"getConnection", "PreparedStatement", "prepareStatement", "?", "?", "setString", "setString"};

    @PostMapping("/SqlInjectionMitigations/attack10a")
    @ResponseBody
    public AttackResult completed(@RequestParam String field1, @RequestParam String field2, @RequestParam String field3, @RequestParam String field4, @RequestParam String field5, @RequestParam String field6, @RequestParam String field7) {
        String[] userInput = {field1, field2, field3, field4, field5, field6, field7};
        int position = 0;
        boolean completed = false;
        for (String input : userInput) {
            if (input.toLowerCase().contains(this.results[position].toLowerCase())) {
                completed = true;
            } else {
                return failed(this).build();
            }
            position++;
        }
        if (completed) {
            return success(this).build();
        }
        return failed(this).build();
    }
}
