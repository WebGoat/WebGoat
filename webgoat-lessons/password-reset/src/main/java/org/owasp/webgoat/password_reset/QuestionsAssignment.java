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

package org.owasp.webgoat.password_reset;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class QuestionsAssignment extends AssignmentEndpoint {

    private static final Map<String, String> COLORS = new HashMap<>();

    static {
        COLORS.put("admin", "green");
        COLORS.put("jerry", "orange");
        COLORS.put("tom", "purple");
        COLORS.put("larry", "yellow");
        COLORS.put("webgoat", "red");
    }

    @PostMapping(path = "/PasswordReset/questions", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public AttackResult passwordReset(@RequestParam Map<String, Object> json) {
        String securityQuestion = (String) json.getOrDefault("securityQuestion", "");
        String username = (String) json.getOrDefault("username", "");

        if ("webgoat".equalsIgnoreCase(username.toLowerCase())) {
            return failed(this).feedback("password-questions-wrong-user").build();
        }

        String validAnswer = COLORS.get(username.toLowerCase());
        if (validAnswer == null) {
            return failed(this).feedback("password-questions-unknown-user").feedbackArgs(username).build();
        } else if (validAnswer.equals(securityQuestion)) {
            return success(this).build();
        }
        return failed(this).build();
    }
}
