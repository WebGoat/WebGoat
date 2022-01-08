/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
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

package org.owasp.webgoat.lessons.spoofcookie;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.spoofcookie.encoders.EncDec;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/***
 *
 * @author Angel Olle Blazquez
 *
 */

@RestController
public class SpoofCookieAssignment extends AssignmentEndpoint {

    private static final String COOKIE_NAME = "spoof_auth";
    private static final String COOKIE_INFO = "Cookie details for user %s:<br />" + COOKIE_NAME + "=%s";
    private static final String ATTACK_USERNAME = "tom";

    private static final Map<String, String> users = Map.of(
        "webgoat", "webgoat",
        "admin", "admin",
        ATTACK_USERNAME, "apasswordfortom");

    @PostMapping(path = "/SpoofCookie/login")
    @ResponseBody
    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public AttackResult login(
        @RequestParam String username,
        @RequestParam String password,
        @CookieValue(value = COOKIE_NAME, required = false) String cookieValue,
        HttpServletResponse response) {

        if (StringUtils.isEmpty(cookieValue)) {
            return credentialsLoginFlow(username, password, response);
        } else {
            return cookieLoginFlow(cookieValue);
        }
    }

    @GetMapping(path = "/SpoofCookie/cleanup")
    public void cleanup(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private AttackResult credentialsLoginFlow(String username, String password, HttpServletResponse response) {
        String lowerCasedUsername = username.toLowerCase();
        if (ATTACK_USERNAME.equals(lowerCasedUsername) && users.get(lowerCasedUsername).equals(password)) {
            return informationMessage(this).feedback("spoofcookie.cheating").build();
        }

        String authPassword = users.getOrDefault(lowerCasedUsername, "");
        if (!authPassword.isBlank() && authPassword.equals(password)) {
            String newCookieValue = EncDec.encode(lowerCasedUsername);
            Cookie newCookie = new Cookie(COOKIE_NAME, newCookieValue);
            newCookie.setPath("/WebGoat");
            newCookie.setSecure(true);
            response.addCookie(newCookie);
            return informationMessage(this).feedback("spoofcookie.login").output(String.format(COOKIE_INFO, lowerCasedUsername, newCookie.getValue())).build();
        }

        return informationMessage(this).feedback("spoofcookie.wrong-login").build();
    }

    private AttackResult cookieLoginFlow(String cookieValue) {
        String cookieUsername;
        try {
            cookieUsername = EncDec.decode(cookieValue).toLowerCase();
        } catch (Exception e) {
            // for providing some instructive guidance, we won't return 4xx error here
            return failed(this).output(e.getMessage()).build();
        }
        if (users.containsKey(cookieUsername)) {
            if (cookieUsername.equals(ATTACK_USERNAME)) {
                return success(this).build();
            }
            return failed(this).feedback("spoofcookie.cookie-login").output(String.format(COOKIE_INFO, cookieUsername, cookieValue)).build();
        }

        return failed(this).feedback("spoofcookie.wrong-cookie").build();
    }

}
