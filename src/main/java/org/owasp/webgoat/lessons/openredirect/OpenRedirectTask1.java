/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task 1: Basic open redirect. Application trusts user supplied URL and would redirect directly.
 */
@RestController
@AssignmentHints({"openredirect.hint1", "openredirect.hint2"})
public class OpenRedirectTask1 implements AssignmentEndpoint {

  private static final Set<String> INTERNAL_HOSTS = Set.of("webgoat.local", "localhost", "127.0.0.1");

  @PostMapping("/OpenRedirect/task1")
  @ResponseBody
  public AttackResult simulate(@RequestParam("url") String url) {
    if (url == null || url.isBlank()) {
      return failed(this).feedback("openredirect.failure1").output("Empty value").build();
    }
    if (!(url.startsWith("http://") || url.startsWith("https://"))) {
      return failed(this).feedback("openredirect.failure1").output("Needs absolute URL with http/https").build();
    }
    try {
      URI u = new URI(url);
      String host = u.getHost();
      if (host == null) {
        return failed(this).feedback("openredirect.failure1").output("Host could not be determined").build();
      }
      if (INTERNAL_HOSTS.contains(host.toLowerCase())) {
        return failed(this).feedback("openredirect.failure1").output("Internal host: " + host).build();
      }
      return success(this).feedback("openredirect.success1").output("Would redirect to: " + escape(url)).build();
    } catch (URISyntaxException e) {
      return failed(this).feedback("openredirect.failure1").output("Invalid URL").build();
    }
  }

  private String escape(String s) {
    return s.replace("<", "&lt;").replace(">", "&gt;");
  }
}
