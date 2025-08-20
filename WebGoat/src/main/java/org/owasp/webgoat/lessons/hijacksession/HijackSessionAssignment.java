/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.hijacksession;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.hijacksession.cas.Authentication;
import org.owasp.webgoat.lessons.hijacksession.cas.HijackSessionAuthenticationProvider;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "hijacksession.hints.1",
  "hijacksession.hints.2",
  "hijacksession.hints.3",
  "hijacksession.hints.4",
  "hijacksession.hints.5"
})
public class HijackSessionAssignment implements AssignmentEndpoint {
  private static final String COOKIE_NAME = "hijack_cookie";

  private final HijackSessionAuthenticationProvider provider;

  public HijackSessionAssignment(HijackSessionAuthenticationProvider provider) {
    this.provider = provider;
  }

  @PostMapping(path = "/HijackSession/login")
  @ResponseBody
  public AttackResult login(
      @RequestParam String username,
      @RequestParam String password,
      @CookieValue(value = COOKIE_NAME, required = false) String cookieValue,
      HttpServletResponse response) {

    Authentication authentication;
    if (StringUtils.isEmpty(cookieValue)) {
      authentication =
          provider.authenticate(
              Authentication.builder().name(username).credentials(password).build());
      setCookie(response, authentication.getId());
    } else {
      authentication = provider.authenticate(Authentication.builder().id(cookieValue).build());
    }

    if (authentication.isAuthenticated()) {
      return success(this).build();
    }

    return failed(this).build();
  }

  private void setCookie(HttpServletResponse response, String cookieValue) {
    Cookie cookie = new Cookie(COOKIE_NAME, cookieValue);
    cookie.setPath("/WebGoat");
    cookie.setSecure(true);
    response.addCookie(cookie);
  }
}
