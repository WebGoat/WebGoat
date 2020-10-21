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

import lombok.AllArgsConstructor;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jason on 1/5/17.
 */

@RestController
@AssignmentHints({"lesson-template.hints.1", "lesson-template.hints.2", "lesson-template.hints.3"})
public class SampleAttack extends AssignmentEndpoint {

    String secretValue = "secr37Value";

    //UserSessionData is bound to session and can be used to persist data across multiple assignments
    @Autowired
    UserSessionData userSessionData;

    @PostMapping("/lesson-template/sample-attack")
    @ResponseBody
    public AttackResult completed(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        if (userSessionData.getValue("some-value") != null) {
            // do any session updating you want here ... or not, just comment/example here
            //return failed().feedback("lesson-template.sample-attack.failure-2").build());
        }

        //overly simple example for success. See other existing lesssons for ways to detect 'success' or 'failure'
        if (secretValue.equals(param1)) {
            return success(this)
                    .output("Custom Output ...if you want, for success")
                    .feedback("lesson-template.sample-attack.success")
                    .build();
            //lesson-template.sample-attack.success is defined in src/main/resources/i18n/WebGoatLabels.properties
        }

        // else
        return failed(this)
                .feedback("lesson-template.sample-attack.failure-2")
                .output("Custom output for this failure scenario, usually html that will get rendered directly ... yes, you can self-xss if you want")
                .build();
    }

    @GetMapping("lesson-template/shop/{user}")
    @ResponseBody
    public List<Item> getItemsInBasket(@PathVariable("user") String user) {
        return List.of(new Item("WG-1", "WebGoat promo", 12.0), new Item("WG-2", "WebGoat sticker", 0.00));
    }

    @AllArgsConstructor
    private class Item {
        private String number;
        private String description;
        private double price;
    }
}
