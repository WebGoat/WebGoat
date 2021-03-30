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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.springframework.util.StringUtils.hasText;

/**
 * Part of the password reset assignment. Used to send the e-mail.
 *
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class ResetLinkAssignmentForgotPassword extends AssignmentEndpoint {

    private final RestTemplate restTemplate;
    private final String webWolfMailURL;

    public ResetLinkAssignmentForgotPassword(RestTemplate restTemplate,
                                             @Value("${webwolf.url.mail}") String webWolfMailURL) {
        this.restTemplate = restTemplate;
        this.webWolfMailURL = webWolfMailURL;
    }

    @PostMapping("/PasswordReset/ForgotPassword/create-password-reset-link")
    @ResponseBody
    public AttackResult sendPasswordResetLink(@RequestParam String email, HttpServletRequest request) {
        String resetLink = UUID.randomUUID().toString();
        ResetLinkAssignment.resetLinks.add(resetLink);
        String host = request.getHeader("host");
        if (hasText(email)) {
            if (email.equals(ResetLinkAssignment.TOM_EMAIL) && (host.contains("9090")||host.contains("webwolf"))) { //User indeed changed the host header.
                ResetLinkAssignment.userToTomResetLink.put(getWebSession().getUserName(), resetLink);
                fakeClickingLinkEmail(host, resetLink);
            } else {
                try {
                    sendMailToUser(email, host, resetLink);
                } catch (Exception e) {
                    return failed(this).output("E-mail can't be send. please try again.").build();
                }
            }
        }
        return success(this).feedback("email.send").feedbackArgs(email).build();
    }

    private void sendMailToUser(String email, String host, String resetLink) {
        int index = email.indexOf("@");
        String username = email.substring(0, index == -1 ? email.length() : index);
        PasswordResetEmail mail = PasswordResetEmail.builder()
                .title("Your password reset link")
                .contents(String.format(ResetLinkAssignment.TEMPLATE, host, resetLink))
                .sender("password-reset@webgoat-cloud.net")
                .recipient(username).build();
        this.restTemplate.postForEntity(webWolfMailURL, mail, Object.class);
    }

    private void fakeClickingLinkEmail(String host, String resetLink) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            new RestTemplate().exchange(String.format("http://%s/PasswordReset/reset/reset-password/%s", host, resetLink), HttpMethod.GET, httpEntity, Void.class);
        } catch (Exception e) {
            //don't care
        }
    }
}
