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
 * Mitigation assignment: demonstrate that an attempted external redirect would be blocked and
 * rewritten to a safe internal destination.
 */
@RestController
@AssignmentHints({"openredirect.mitigation.hint1", "openredirect.mitigation.hint2"})
public class OpenRedirectMitigationCheck implements AssignmentEndpoint {

  private static final Set<String> INTERNAL = Set.of("webgoat.local", "localhost", "127.0.0.1");

  @PostMapping("/OpenRedirect/mitigation")
  @ResponseBody
  public AttackResult check(@RequestParam("url") String url) {
    if (url == null || url.isBlank()) {
      return failed(this).feedback("openredirect.mitigation.failure").output("Empty value").build();
    }
    boolean absolute = url.startsWith("http://") || url.startsWith("https://");
    if (!absolute) {
      return failed(this)
          .feedback("openredirect.mitigation.failure")
          .output("Provide an absolute external URL (http/https)")
          .build();
    }
    try {
      URI u = new URI(url);
      String host = u.getHost();
      if (host == null) {
        return failed(this).feedback("openredirect.mitigation.failure").output("Host parse failed").build();
      }
      if (INTERNAL.contains(host.toLowerCase())) {
        return failed(this)
            .feedback("openredirect.mitigation.failure")
            .output("This host is internal, show a blocked external attempt instead")
            .build();
      }
      // Simulated mitigation: external target rejected, internal safe path chosen
      String safe = "/home";
      String output =
          "Attempted external host: "
              + esc(host)
              + " blocked. Application would redirect to safe internal path: "
              + esc(safe);
      return success(this).feedback("openredirect.mitigation.success").output(output).build();
    } catch (URISyntaxException e) {
      return failed(this).feedback("openredirect.mitigation.failure").output("Invalid URL").build();
    }
  }

  private String esc(String s) {
    return s.replace("<", "&lt;").replace(">", "&gt;");
  }
}
