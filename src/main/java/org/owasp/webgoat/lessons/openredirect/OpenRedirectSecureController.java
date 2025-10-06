/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Demonstrates a safer redirect pattern using server-side id mapping instead of raw user URLs.
 * Not part of the scored assignments – just for experimentation.
 */
@Controller
public class OpenRedirectSecureController {

  // Use only confirmed existing internal endpoints within WebGoat
  // 1 -> welcome page, 2 -> login page, 3 -> logout endpoint
  private static final Map<Integer, String> DESTINATIONS =
      Map.of(1, "/welcome.mvc", 2, "/login", 3, "/logout");

  @GetMapping("/OpenRedirect/safe")
  public ModelAndView safe(@RequestParam(name = "destId", defaultValue = "1") Integer destId) {
    String dest = DESTINATIONS.getOrDefault(destId, "/welcome.mvc");
    return new ModelAndView("redirect:" + dest);
  }
}
