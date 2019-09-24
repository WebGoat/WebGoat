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

package org.owasp.webgoat.template;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jason on 1/5/17.
 */

@RestController
public class SampleAttack extends AssignmentEndpoint {

    String secretValue = "secr37Value";

    //UserSessionData is bound to session and can be used to persist data across multiple assignments
    @Autowired
    UserSessionData userSessionData;


    @GetMapping(path = "/lesson-template/sample-attack", produces = {"application/json"})
    public @ResponseBody
    AttackResult completed(String param1, String param2, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (userSessionData.getValue("some-value") != null) {
            // do any session updating you want here ... or not, just comment/example here
            //return trackProgress(failed().feedback("lesson-template.sample-attack.failure-2").build());
        }

        //overly simple example for success. See other existing lesssons for ways to detect 'success' or 'failure'
        if (secretValue.equals(param1)) {
            return trackProgress(success()
                    .output("Custom Output ...if you want, for success")
                    .feedback("lesson-template.sample-attack.success")
                    .build());
            //lesson-template.sample-attack.success is defined in src/main/resources/i18n/WebGoatLabels.properties
        }

        // else
        return trackProgress(failed()
                .feedback("lesson-template.sample-attack.failure-2")
                .output("Custom output for this failure scenario, usually html that will get rendered directly ... yes, you can self-xss if you want")
                .build());
    }
}
