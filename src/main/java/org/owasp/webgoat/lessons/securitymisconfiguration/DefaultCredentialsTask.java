/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Task demonstrating exploitation of default credentials. */
@RestController
@AssignmentHints({
    "securitymisconfiguration.task1.hint1",
    "securitymisconfiguration.task1.hint2"
})
public class DefaultCredentialsTask implements AssignmentEndpoint {

  private static final String DEFAULT_USERNAME = "admin";
  private static final String DEFAULT_PASSWORD = "admin";

  @PostMapping(
      value = "/SecurityMisconfiguration/task1",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseBody
  public AttackResult login(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "password", required = false) String password) {

    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      return failed(this)
          .feedback("securitymisconfiguration.task1.failure.blank")
          .build();
    }

    if (DEFAULT_USERNAME.equals(username.trim()) && DEFAULT_PASSWORD.equals(password)) {
      return success(this)
          .feedback("securitymisconfiguration.task1.success")
          .output("User profile: staging admin (no MFA)")
          .build();
    }

    return failed(this)
        .feedback("securitymisconfiguration.task1.failure.invalid")
        .build();
  }
}
