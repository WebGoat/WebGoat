/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "insecure-deserialization.hints.1",
  "insecure-deserialization.hints.2",
  "insecure-deserialization.hints.3"
})
public class InsecureDeserializationTask implements AssignmentEndpoint {

  private static final int MAX_PAYLOAD_SIZE = 4096;
  private static final int TIME_WINDOW_MINUTES = 10;

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Safe DTO for deserialization—only fields we expect, no command execution.
   */
  static class TaskDto {
    public String taskName;
    public String taskAction;
    public String requestedExecutionTime;
  }

  @PostMapping("/InsecureDeserialization/task")
  @ResponseBody
  public AttackResult completed(@RequestParam String token) {
    if (token == null || token.isBlank()) {
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    }

    try {
      // Normalize base64 padding
      String normalized = token.replace('-', '+').replace('_', '/');
      while (normalized.length() % 4 != 0) {
        normalized += '=';
      }

      byte[] payload;
      try {
        payload = Base64.getDecoder().decode(normalized);
      } catch (IllegalArgumentException e) {
        return failed(this).feedback("insecure-deserialization.expired").build();
      }

      // Reject oversized payloads
      if (payload.length == 0 || payload.length > MAX_PAYLOAD_SIZE) {
        return failed(this).feedback("insecure-deserialization.expired").build();
      }

      // Deserialize JSON instead of binary Java objects
      TaskDto task = objectMapper.readValue(payload, TaskDto.class);

      // Validate required fields
      if (task.taskName == null || task.taskName.isBlank() ||
          task.taskAction == null || task.taskAction.isBlank()) {
        return failed(this).feedback("insecure-deserialization.wrongobject").build();
      }

      // Validate timestamp is within acceptable window
      if (task.requestedExecutionTime == null || task.requestedExecutionTime.isBlank()) {
        return failed(this).feedback("insecure-deserialization.invalidversion").build();
      }

      LocalDateTime executionTime = LocalDateTime.parse(task.requestedExecutionTime);
      LocalDateTime now = LocalDateTime.now();
      long minutesDiff = Math.abs(ChronoUnit.MINUTES.between(executionTime, now));

      if (minutesDiff > TIME_WINDOW_MINUTES) {
        return failed(this).feedback("insecure-deserialization.expired").build();
      }

      return success(this).build();

    } catch (IOException e) {
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    } catch (Exception e) {
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    }
  }
}
