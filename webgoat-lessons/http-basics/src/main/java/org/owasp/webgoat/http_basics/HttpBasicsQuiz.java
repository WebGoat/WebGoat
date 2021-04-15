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

package org.owasp.webgoat.http_basics;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"http-basics.hints.http_basic_quiz.1", "http-basics.hints.http_basic_quiz.2"})
@AssignmentPath("HttpBasics/attack2")
public class HttpBasicsQuiz extends AssignmentEndpoint {

    @PostMapping("/HttpBasics/attack2")
    @ResponseBody
    public AttackResult completed(@RequestParam String answer, @RequestParam String magic_answer, @RequestParam String magic_num) {
        if ("POST".equalsIgnoreCase(answer) && magic_answer.equals(magic_num)) {
            return success(this).build();
        } else {
            if (!"POST".equalsIgnoreCase(answer)) {
                return failed(this).feedback("http-basics.incorrect").build();
            }
            if (!magic_answer.equals(magic_num)) {
                return failed(this).feedback("http-basics.magic").build();
            }
        }
        return failed(this).build();
    }
}
