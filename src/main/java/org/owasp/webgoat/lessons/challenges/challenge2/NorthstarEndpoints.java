/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NorthstarEndpoints {

  @GetMapping("/northstar/go")
  public ResponseEntity<Void> redirect(@RequestParam String url) {
    try {
      return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/northstar/feeds")
  public String feeds() {
    return "Partner bulletin: the evening edition is ready for review.";
  }
}
