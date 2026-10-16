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
 * Task 4: Double-encoding / partial normalization bypass.
 *
 * The (flawed) logic only decodes once and validates that the resulting string appears to target
 * the internal host webgoat.local. A second decode would reveal an attacker controlled host via a
 * userinfo (@) trick or other transformation.
 *
 * Goal: Provide a value which after ONE decode still looks internal (prefix starts with
 * https://webgoat.local) but after TWO decodes the real parsed host is EXTERNAL and not in the
 * internal host list. Example payload: https://webgoat.local%2540evil.com which decodes once to
 * https://webgoat.local%40evil.com (still internal) and twice to https://webgoat.local@evil.com
 */
@RestController
@AssignmentHints({"openredirect.hint7", "openredirect.hint8"})
public class OpenRedirectTask4 implements AssignmentEndpoint {

  private static final Set<String> INTERNAL_HOSTS = Set.of("webgoat.local", "localhost", "127.0.0.1");

  @PostMapping("/OpenRedirect/task4")
  @ResponseBody
  public AttackResult doubleDecode(@RequestParam("target") String target) {
    if (target == null || target.isBlank()) {
      return failed(this).feedback("openredirect.failure4").output("Empty value").build();
    }

    // First decode (application does this)
    String firstDecoded = URLDecoder.decode(target, StandardCharsets.UTF_8);
    String lower = firstDecoded.toLowerCase();
    boolean appearsInternal =
        lower.startsWith("https://webgoat.local") || lower.startsWith("http://webgoat.local");

    URI firstUri;
    try {
      firstUri = new URI(firstDecoded);
    } catch (URISyntaxException e) {
      return failed(this).feedback("openredirect.failure4").output("Invalid URL after first decode").build();
    }

    String firstHost = firstUri.getHost();

    // Second decode (what some downstream component might accidentally do)
    String secondDecoded = URLDecoder.decode(firstDecoded, StandardCharsets.UTF_8);
    URI secondUri;
    try {
      secondUri = new URI(secondDecoded);
    } catch (URISyntaxException e) {
      return failed(this).feedback("openredirect.failure4").output("Invalid URL after second decode").build();
    }
    String secondHost = secondUri.getHost();

    StringBuilder debug = new StringBuilder();
    debug.append("Raw: ").append(esc(target)).append("\n");
    debug.append("1st decode: ").append(esc(firstDecoded)).append("\n");
    debug.append("1st host: ").append(esc(String.valueOf(firstHost))).append("\n");
    debug.append("AppearsInternalAfter1: ").append(appearsInternal).append("\n");
    debug.append("2nd decode: ").append(esc(secondDecoded)).append("\n");
    debug.append("2nd host: ").append(esc(String.valueOf(secondHost))).append("\n");

    boolean secondHostExternal =
        secondHost != null && !INTERNAL_HOSTS.contains(secondHost.toLowerCase());
    boolean hostChanged =
        secondHost != null && (firstHost == null || !firstHost.equalsIgnoreCase(secondHost));

    if (appearsInternal && secondHostExternal && hostChanged) {
      return success(this)
          .feedback("openredirect.success4")
          .output(debug.append("Double decode reveals external host").toString())
          .build();
    }

    return failed(this)
        .feedback("openredirect.failure4")
        .output(debug.append("Bypass not achieved. Use %25 encoding to hide '@' or other host change.").toString())
        .build();
  }

  private String esc(String s) {
    return s.replace("<", "&lt;").replace(">", "&gt;");
  }
}
