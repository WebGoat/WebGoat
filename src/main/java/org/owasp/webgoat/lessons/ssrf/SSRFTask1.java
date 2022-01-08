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

package org.owasp.webgoat.lessons.ssrf;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AssignmentHints({"ssrf.hint1", "ssrf.hint2"})
public class SSRFTask1 extends AssignmentEndpoint {

    @PostMapping("/SSRF/task1")
    @ResponseBody
    public AttackResult completed(@RequestParam String url) {
        return stealTheCheese(url);
    }

    protected AttackResult stealTheCheese(String url) {
        try {
            StringBuffer html = new StringBuffer();

            if (url.matches("images/tom.png")) {
                html.append("<img class=\"image\" alt=\"Tom\" src=\"images/tom.png\" width=\"25%\" height=\"25%\">");
                return failed(this)
                        .feedback("ssrf.tom")
                        .output(html.toString())
                        .build();
            } else if (url.matches("images/jerry.png")) {
                html.append("<img class=\"image\" alt=\"Jerry\" src=\"images/jerry.png\" width=\"25%\" height=\"25%\">");
                return success(this)
                        .feedback("ssrf.success")
                        .output(html.toString())
                        .build();
            } else {
                html.append("<img class=\"image\" alt=\"Silly Cat\" src=\"images/cat.jpg\">");
                return failed(this)
                        .feedback("ssrf.failure")
                        .output(html.toString())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed(this)
                    .output(e.getMessage())
                    .build();
        }
    }
}
