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
 * Task 2: Naive filter bypass. Developer added a substring check 'webgoat'.
 * Success if the supplied URL contains 'webgoat' substring but host resolves to external domain.
 */
@RestController
@AssignmentHints({"openredirect.hint3", "openredirect.hint4"})
public class OpenRedirectTask2 implements AssignmentEndpoint {

  private static final Set<String> ALLOWED = Set.of("webgoat.local", "webgoat.org", "localhost");

  @PostMapping("/OpenRedirect/task2")
  @ResponseBody
  public AttackResult simulate(@RequestParam("url") String url) {
    if (url == null || url.isBlank()) {
      return failed(this).feedback("openredirect.failure2").output("Empty value").build();
    }
    if (!url.contains("webgoat")) {
      return failed(this).feedback("openredirect.failure2").output("Must contain 'webgoat'").build();
    }
    if (!(url.startsWith("http://") || url.startsWith("https://"))) {
      return failed(this).feedback("openredirect.failure2").output("Needs absolute URL with http/https").build();
    }
    try {
      URI u = new URI(url);
      String host = u.getHost();
      if (host == null) {
        return failed(this).feedback("openredirect.failure2").output("Could not parse host").build();
      }
      if (!ALLOWED.contains(host.toLowerCase())) {
        return success(this)
            .feedback("openredirect.success2")
            .output("Bypassed naive filter - host: " + escape(host))
            .build();
      }
      return failed(this)
          .feedback("openredirect.failure2")
          .output("Host still allowed: " + escape(host))
          .build();
    } catch (URISyntaxException e) {
      return failed(this).feedback("openredirect.failure2").output("Invalid URL").build();
    }
  }

  private String escape(String s) {
    return s.replace("<", "&lt;").replace(">", "&gt;");
  }
}
