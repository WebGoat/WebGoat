/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternalDiagnostic {

  static final String INTERNAL_HEADER = "X-WebGoat-Lesson-Internal";

  private final Challenge2State state;

  public InternalDiagnostic(Challenge2State state) {
    this.state = state;
  }

  @GetMapping("/northstar/internal/diagnostics")
  public ResponseEntity<Map<String, String>> diagnostics(
      @RequestHeader(value = INTERNAL_HEADER, required = false) String token) {
    // This token models service identity without relying on network addresses or proxy headers.
    if (!state.internalToken().equals(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(Map.of("status", "UP", "flag", state.flag()));
  }
}
