/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.ssrf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"ssrf.hint3"})
public class SSRFTask2 implements AssignmentEndpoint {

  @PostMapping("/SSRF/task2")
  @ResponseBody
  public AttackResult completed(@RequestParam String url) {
    return furBall(url);
  }

  protected AttackResult furBall(String url) {
    URL parsedUrl;

    try {
      parsedUrl = new URL(url);
    } catch (MalformedURLException e) {
      return getFailedResult("Invalid URL");
    }

    if (!"http".equalsIgnoreCase(parsedUrl.getProtocol())) {
      return getFailedResult("Only HTTP protocol is allowed");
    }

    if (!parsedUrl.getHost().equals("ifconfig.pro")) {
      return getFailedResult("Host not allowed");
    }
    
    String html;
    try (InputStream in = parsedUrl.openStream()) {
      html =
          new String(in.readAllBytes(), StandardCharsets.UTF_8)
              .replaceAll("\n", "<br>"); // Otherwise the \n gets escaped in the response
    } catch (IOException e) {
      // in case the external site is down, the test and lesson should still be ok
      html =
          "<html><body>Although the http://ifconfig.pro site is down, you still managed to solve"
              + " this exercise the right way!</body></html>";
    }
    
    return success(this).feedback("ssrf.success").output(html).build();
  }

  private AttackResult getFailedResult(String errorMsg) {
    return failed(this).feedback("ssrf.failure").output(errorMsg).build();
  }
}
