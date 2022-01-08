
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

package org.owasp.webgoat.lessons.sql_injection.mitigation;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.sql_injection.advanced.SqlInjectionLesson6a;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AssignmentHints(value = {"SqlOnlyInputValidationOnKeywords-1", "SqlOnlyInputValidationOnKeywords-2", "SqlOnlyInputValidationOnKeywords-3"})
public class SqlOnlyInputValidationOnKeywords extends AssignmentEndpoint {

    private final SqlInjectionLesson6a lesson6a;

    public SqlOnlyInputValidationOnKeywords(SqlInjectionLesson6a lesson6a) {
        this.lesson6a = lesson6a;
    }

    @PostMapping("/SqlOnlyInputValidationOnKeywords/attack")
    @ResponseBody
    public AttackResult attack(@RequestParam("userid_sql_only_input_validation_on_keywords") String userId) {
        userId = userId.toUpperCase().replace("FROM", "").replace("SELECT", "");
        if (userId.contains(" ")) {
            return failed(this).feedback("SqlOnlyInputValidationOnKeywords-failed").build();
        }
        AttackResult attackResult = lesson6a.injectableQuery(userId);
        return new AttackResult(attackResult.isLessonCompleted(), attackResult.getFeedback(), attackResult.getOutput(), getClass().getSimpleName(), true);
    }
}
