/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.csrf;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Created by jason on 9/30/17. */
@RestController
public class CSRFGetFlag {

  @Autowired LessonSession userSessionData;
  @Autowired private PluginMessages pluginMessages;

  @PostMapping(
      path = "/csrf/basic-get-flag",
      produces = {"application/json"})
  @ResponseBody
  public Map<String, Object> invoke(HttpServletRequest req) {

    Map<String, Object> response = new HashMap<>();

    String host = (req.getHeader("host") == null) ? "NULL" : req.getHeader("host");
    String referer = (req.getHeader("referer") == null) ? "NULL" : req.getHeader("referer");
    String[] refererArr = referer.split("/");

    if (referer.equals("NULL")) {
      if ("true".equals(req.getParameter("csrf"))) {
        Random random = new Random();
        userSessionData.setValue("csrf-get-success", random.nextInt(65536));
        response.put("success", true);
        response.put("message", pluginMessages.getMessage("csrf-get-null-referer.success"));
        response.put("flag", userSessionData.getValue("csrf-get-success"));
      } else {
        Random random = new Random();
        userSessionData.setValue("csrf-get-success", random.nextInt(65536));
        response.put("success", true);
        response.put("message", pluginMessages.getMessage("csrf-get-other-referer.success"));
        response.put("flag", userSessionData.getValue("csrf-get-success"));
      }
    } else if (refererArr[2].equals(host)) {
      response.put("success", false);
      response.put("message", "Appears the request came from the original host");
      response.put("flag", null);
    } else {
      Random random = new Random();
      userSessionData.setValue("csrf-get-success", random.nextInt(65536));
      response.put("success", true);
      response.put("message", pluginMessages.getMessage("csrf-get-other-referer.success"));
      response.put("flag", userSessionData.getValue("csrf-get-success"));
    }

    return response;
  }
}
