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

package org.owasp.webgoat.auth_bypass;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 1/5/17.
 */
@RestController
@AssignmentHints({"auth-bypass.hints.verify.1", "auth-bypass.hints.verify.2", "auth-bypass.hints.verify.3", "auth-bypass.hints.verify.4"})
public class VerifyAccount extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;

    @Autowired
    UserSessionData userSessionData;

    @PostMapping(path = "/auth-bypass/verify-account", produces = {"application/json"})
    @ResponseBody
    public AttackResult completed(@RequestParam String userId, @RequestParam String verifyMethod, HttpServletRequest req) throws ServletException, IOException {
        AccountVerificationHelper verificationHelper = new AccountVerificationHelper();
        Map<String, String> submittedAnswers = parseSecQuestions(req);
        if (verificationHelper.didUserLikelylCheat((HashMap) submittedAnswers)) {
            return failed(this)
                    .feedback("verify-account.cheated")
                    .output("Yes, you guessed correctly, but see the feedback message")
                    .build();
        }

        // else
        if (verificationHelper.verifyAccount(Integer.valueOf(userId), (HashMap) submittedAnswers)) {
            userSessionData.setValue("account-verified-id", userId);
            return success(this)
                    .feedback("verify-account.success")
                    .build();
        } else {
            return failed(this)
                    .feedback("verify-account.failed")
                    .build();
        }

    }

    private HashMap<String, String> parseSecQuestions(HttpServletRequest req) {
        Map<String, String> userAnswers = new HashMap<>();
        List<String> paramNames = Collections.list(req.getParameterNames());
        for (String paramName : paramNames) {
            //String paramName = req.getParameterNames().nextElement();
            if (paramName.contains("secQuestion")) {
                userAnswers.put(paramName, req.getParameter(paramName));
            }
        }
        return (HashMap) userAnswers;
    }

}
