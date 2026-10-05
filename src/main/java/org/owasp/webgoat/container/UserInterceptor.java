/*
 * SPDX-FileCopyrightText: Copyright © 2023 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.owasp.webgoat.container.asciidoc.EnvironmentExposure;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserInterceptor implements HandlerInterceptor {

  private Environment env = EnvironmentExposure.getEnv();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // Do nothing
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    if (null != modelAndView) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (null != authentication) {
        modelAndView.getModel().put("username", authentication.getName());
      }
      if (null != env) {
        String githubClientId =
            env.getProperty("spring.security.oauth2.client.registration.github.client-id");
        if (null != githubClientId && !githubClientId.equals("dummy")) {
          modelAndView.getModel().put("oauth", Boolean.TRUE);
        }
      } else {
        modelAndView.getModel().put("oauth", Boolean.FALSE);
      }
    }
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // Do nothing
  }
}
