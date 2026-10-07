/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedImportEndpoint {

  private final ChainedXmlService xmlService;
  private final Challenge2State state;

  public FeedImportEndpoint(ChainedXmlService xmlService, Challenge2State state) {
    this.xmlService = xmlService;
    this.state = state;
  }

  @PostMapping("/northstar/import")
  public ResponseEntity<Map<String, String>> importFeed(
      @RequestParam String xml, HttpServletRequest request) {
    try {
      URI importUri = URI.create(request.getRequestURL().toString());
      return ResponseEntity.ok(Map.of("preview", xmlService.parse(xml, state, importUri)));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("error", e.getMessage()));
    }
  }
}
