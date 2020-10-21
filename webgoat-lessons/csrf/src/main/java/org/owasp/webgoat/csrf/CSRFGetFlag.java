/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.csrf;

import org.owasp.webgoat.i18n.PluginMessages;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by jason on 9/30/17.
 */
@RestController
public class CSRFGetFlag {

    @Autowired
    UserSessionData userSessionData;
    @Autowired
    private PluginMessages pluginMessages;

    @RequestMapping(path = "/csrf/basic-get-flag", produces = {"application/json"}, method = RequestMethod.POST)
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
