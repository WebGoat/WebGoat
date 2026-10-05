/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Welcome {

  private static final String WELCOMED = "welcomed";

  /**
   * welcome.
   *
   * @param request a {@link jakarta.servlet.http.HttpServletRequest} object.
   * @return a {@link org.springframework.web.servlet.ModelAndView} object.
   */
  @GetMapping(path = {"welcome.mvc"})
  public ModelAndView welcome(HttpServletRequest request) {

    // set the welcome attribute
    // this is so the attack servlet does not also
    // send them to the welcome page
    HttpSession session = request.getSession();
    if (session.getAttribute(WELCOMED) == null) {
      session.setAttribute(WELCOMED, "true");
    }

    // go ahead and send them to webgoat (skip the welcome page)
    ModelAndView model = new ModelAndView();
    model.setViewName("forward:/attack?start=true");
    return model;
  }
}
