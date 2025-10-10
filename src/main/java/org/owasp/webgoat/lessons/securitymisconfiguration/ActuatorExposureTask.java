/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.util.Map;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Task showing exposed actuator/admin endpoints leaking secrets. */
@RestController
@AssignmentHints({
    "securitymisconfiguration.task3.hint1",
    "securitymisconfiguration.task3.hint2"
})
public class ActuatorExposureTask implements AssignmentEndpoint {

  static final String LEAKED_API_KEY = "INTERNAL-API-KEY-987";

  @GetMapping(
      value = "/SecurityMisconfiguration/task3/actuator/env",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> actuatorEnv() {
    return Map.of(
        "name", "webgoat-staging",
        "profiles", new String[] {"staging", "debug"},
        "systemApiKey", LEAKED_API_KEY,
        "features", Map.of("betaUi", true, "payments", false));
  }

  @GetMapping(
      value = "/SecurityMisconfiguration/task3/actuator/health",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> actuatorHealth() {
    return Map.of(
        "status", "UP",
        "checks",
            Map.of(
                "database", Map.of("status", "UP", "responseTimeMs", 12),
                "cache", Map.of("status", "UP", "hitRatio", 0.91)));
  }

  @PostMapping(
      value = "/SecurityMisconfiguration/task3",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public AttackResult submitApiKey(@RequestParam("apiKey") String apiKey) {
    if (LEAKED_API_KEY.equals(apiKey)) {
      return success(this)
          .feedback("securitymisconfiguration.task3.success")
          .output("Actuator endpoints now require authentication and are limited to ops network.")
          .build();
    }
    if (apiKey == null || apiKey.isBlank()) {
      return failed(this)
          .feedback("securitymisconfiguration.task3.failure.blank")
          .build();
    }
    return failed(this)
        .feedback("securitymisconfiguration.task3.failure.invalid")
        .build();
  }
}
