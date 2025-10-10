/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Task where learners harden a configuration by disabling insecure settings. */
@RestController
@AssignmentHints({
    "securitymisconfiguration.task4.hint1",
    "securitymisconfiguration.task4.hint2"
})
public class ConfigHardeningTask implements AssignmentEndpoint {

  private static final Map<String, String> EXPECTED =
      Map.of(
          "management.endpoint.env.enabled", "false",
          "management.endpoint.health.show-details", "never",
          "spring.security.user.name", "",
          "spring.security.user.password", "");

  @PostMapping(
      value = "/SecurityMisconfiguration/task4",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public AttackResult submitConfig(
      @RequestParam("envEnabled") @NotBlank String envEnabled,
      @RequestParam("healthDetails") @NotBlank String healthDetails,
      @RequestParam(value = "defaultUser", required = false) String defaultUser,
      @RequestParam(value = "defaultPassword", required = false) String defaultPassword) {

    Map<String, String> current = new HashMap<>();
    current.put("management.endpoint.env.enabled", envEnabled.trim());
    current.put("management.endpoint.health.show-details", healthDetails.trim());
    current.put("spring.security.user.name", defaultUser == null ? "" : defaultUser.trim());
    current.put(
        "spring.security.user.password", defaultPassword == null ? "" : defaultPassword.trim());

    if (current.equals(EXPECTED)) {
      return success(this)
          .feedback("securitymisconfiguration.task4.success")
          .output("Configuration hardened: debug endpoints disabled, default user removed.")
          .build();
    }

    return failed(this)
        .feedback("securitymisconfiguration.task4.failure.invalid")
        .output("Check that env endpoint is disabled, health details hidden, and default user removed.")
        .build();
  }
}
