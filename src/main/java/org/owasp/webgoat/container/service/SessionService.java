/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.i18n.Messages;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SessionService {

  private final RestartLessonService restartLessonService;
  private final Messages messages;

  @RequestMapping(path = "/service/enable-security.mvc", produces = "application/json")
  @ResponseBody
  public String applySecurity(@CurrentUser WebGoatUser user) {
    // webSession.toggleSecurity();
    // restartLessonService.restartLesson(user);

    // TODO disabled for now
    // var msg = webSession.isSecurityEnabled() ? "security.enabled" : "security.disabled";
    return messages.getMessage("Not working...");
  }
}
