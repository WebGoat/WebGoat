/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Provides a real 302 redirect for experimentation separate from assignment scoring.
 */
@Controller
public class OpenRedirectRealRedirect {

  @GetMapping("/OpenRedirect/realRedirect")
  public ModelAndView real(@RequestParam("url") String url) {
    // Intentionally vulnerable: no validation
    // Fix: validate URL against allowlist to prevent open redirect
    try {
      java.net.URI uri = new java.net.URI(url);
      String host = uri.getHost();
      if (host != null && ALLOWED_HOSTS.contains(host)) {
        return new ModelAndView("redirect:" + url);
      }
    } catch (java.net.URISyntaxException e) {
    }
    return new ModelAndView("redirect:/error");
  }
}
