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

package org.owasp.webgoat.lessons.password_reset;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class SimpleMailAssignment extends AssignmentEndpoint {

    private final String webWolfURL;
    private RestTemplate restTemplate;

    public SimpleMailAssignment(RestTemplate restTemplate, @Value("${webwolf.mail.url}") String webWolfURL) {
        this.restTemplate = restTemplate;
        this.webWolfURL = webWolfURL;
    }

    @PostMapping(path = "/PasswordReset/simple-mail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public AttackResult login(@RequestParam String email, @RequestParam String password) {
        String emailAddress = ofNullable(email).orElse("unknown@webgoat.org");
        String username = extractUsername(emailAddress);

        if (username.equals(getWebSession().getUserName()) && StringUtils.reverse(username).equals(password)) {
            return success(this).build();
        } else {
            return failed(this).feedbackArgs("password-reset-simple.password_incorrect").build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, value = "/PasswordReset/simple-mail/reset")
    @ResponseBody
    public AttackResult resetPassword(@RequestParam String emailReset) {
        String email = ofNullable(emailReset).orElse("unknown@webgoat.org");
        return sendEmail(extractUsername(email), email);
    }

    private String extractUsername(String email) {
        int index = email.indexOf("@");
        return email.substring(0, index == -1 ? email.length() : index);
    }

    private AttackResult sendEmail(String username, String email) {
        if (username.equals(getWebSession().getUserName())) {
            PasswordResetEmail mailEvent = PasswordResetEmail.builder()
                    .recipient(username)
                    .title("Simple e-mail assignment")
                    .time(LocalDateTime.now())
                    .contents("Thanks for resetting your password, your new password is: " + StringUtils.reverse(username))
                    .sender("webgoat@owasp.org")
                    .build();
            try {
                restTemplate.postForEntity(webWolfURL, mailEvent, Object.class);
            } catch (RestClientException e) {
                return informationMessage(this).feedback("password-reset-simple.email_failed").output(e.getMessage()).build();
            }
            return informationMessage(this).feedback("password-reset-simple.email_send").feedbackArgs(email).build();
        } else {
            return informationMessage(this).feedback("password-reset-simple.email_mismatch").feedbackArgs(username).build();
        }
    }
}
