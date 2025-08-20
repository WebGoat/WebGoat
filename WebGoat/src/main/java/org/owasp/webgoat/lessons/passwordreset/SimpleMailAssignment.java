/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordreset;

import static java.util.Optional.ofNullable;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.informationMessage;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.CurrentUsername;
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

@RestController
public class SimpleMailAssignment implements AssignmentEndpoint {
  private final String webWolfURL;
  private RestTemplate restTemplate;

  public SimpleMailAssignment(
      RestTemplate restTemplate, @Value("${webwolf.mail.url}") String webWolfURL) {
    this.restTemplate = restTemplate;
    this.webWolfURL = webWolfURL;
  }

  @PostMapping(
      path = "/PasswordReset/simple-mail",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseBody
  public AttackResult login(
      @RequestParam String email,
      @RequestParam String password,
      @CurrentUsername String webGoatUsername) {
    String emailAddress = ofNullable(email).orElse("unknown@webgoat.org");
    String username = extractUsername(emailAddress);

    if (username.equals(webGoatUsername) && StringUtils.reverse(username).equals(password)) {
      return success(this).build();
    } else {
      return failed(this).feedbackArgs("password-reset-simple.password_incorrect").build();
    }
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      value = "/PasswordReset/simple-mail/reset")
  @ResponseBody
  public AttackResult resetPassword(
      @RequestParam String emailReset, @CurrentUsername String username) {
    String email = ofNullable(emailReset).orElse("unknown@webgoat.org");
    return sendEmail(extractUsername(email), email, username);
  }

  private String extractUsername(String email) {
    int index = email.indexOf("@");
    return email.substring(0, index == -1 ? email.length() : index);
  }

  private AttackResult sendEmail(String username, String email, String webGoatUsername) {
    if (username.equals(webGoatUsername)) {
      PasswordResetEmail mailEvent =
          PasswordResetEmail.builder()
              .recipient(username)
              .title("Simple e-mail assignment")
              .time(LocalDateTime.now())
              .contents(
                  "Thanks for resetting your password, your new password is: "
                      + StringUtils.reverse(username))
              .sender("webgoat@owasp.org")
              .build();
      try {
        restTemplate.postForEntity(webWolfURL, mailEvent, Object.class);
      } catch (RestClientException e) {
        return informationMessage(this)
            .feedback("password-reset-simple.email_failed")
            .output(e.getMessage())
            .build();
      }
      return informationMessage(this)
          .feedback("password-reset-simple.email_send")
          .feedbackArgs(email)
          .build();
    } else {
      return informationMessage(this)
          .feedback("password-reset-simple.email_mismatch")
          .feedbackArgs(username)
          .build();
    }
  }
}
