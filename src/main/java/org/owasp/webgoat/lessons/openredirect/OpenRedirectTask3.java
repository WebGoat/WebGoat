/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task 3 (Challenge): Demonstrates flawed normalization logic using startsWith on the raw host
 * portion which can be bypassed using userinfo (@) or crafted subdomains.
 */
@RestController
@AssignmentHints({"openredirect.hint5", "openredirect.hint6"})
public class OpenRedirectTask3 implements AssignmentEndpoint {

  private static final Set<String> INTERNAL_HOSTS = Set.of("webgoat.local", "localhost", "127.0.0.1");

  @PostMapping("/OpenRedirect/task3")
  @ResponseBody
  public AttackResult challenge(
      @RequestParam("target") String target,
      @RequestParam(value = "token", required = false) String token) {
    if (target == null || target.isBlank()) {
      return failed(this).feedback("openredirect.failure3").output("Empty value").build();
    }
    String decoded = URLDecoder.decode(target, StandardCharsets.UTF_8);
    String lower = decoded.toLowerCase();
    // Vulnerable heuristic: treat anything starting with protocol + webgoat.local as internal
    boolean appearsInternal = lower.startsWith("http://webgoat.local") || lower.startsWith("https://webgoat.local");

    URI uri;
    try {
      uri = new URI(decoded);
    } catch (URISyntaxException e) {
      return failed(this).feedback("openredirect.failure3").output("Invalid URL").build();
    }

    String realHost = uri.getHost();
    StringBuilder debug = new StringBuilder();
    debug.append("Raw: ").append(escape(target)).append("\n");
    debug.append("Decoded: ").append(escape(decoded)).append("\n");
    debug.append("AppearsInternal: ").append(appearsInternal).append("\n");
    debug.append("RealHost: ").append(escape(String.valueOf(realHost))).append("\n");
    if (token != null) {
      debug.append("Token: ").append(escape(token)).append("\n");
    }

    if (appearsInternal && realHost != null && !INTERNAL_HOSTS.contains(realHost.toLowerCase())) {
      return success(this)
          .feedback("openredirect.success3")
          .output(debug.append("Bypassed flawed normalization, real host external").toString())
          .build();
    }

    return failed(this)
        .feedback("openredirect.failure3")
        .output(debug.append("Did not bypass. Provide host confusion payload (try userinfo @).").toString())
        .build();
  }

  private String escape(String s) {
    return s.replace("<", "&lt;").replace(">", "&gt;");
  }
}
