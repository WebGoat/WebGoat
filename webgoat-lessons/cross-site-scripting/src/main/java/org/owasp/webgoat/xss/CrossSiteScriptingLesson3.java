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

package org.owasp.webgoat.xss;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//@RestController
@Deprecated
//TODO This assignment seems not to be in use in the UI
// it is there to make sure the lesson can be marked complete
// in order to restore it, make it accessible through the UI and uncomment RestController
@AssignmentHints(value = {"xss-mitigation-3-hint1", "xss-mitigation-3-hint2", "xss-mitigation-3-hint3", "xss-mitigation-3-hint4"})
public class CrossSiteScriptingLesson3 extends AssignmentEndpoint {

    @PostMapping("/CrossSiteScripting/attack3")
    @ResponseBody
    public AttackResult completed(@RequestParam String editor) {
        String unescapedString = org.jsoup.parser.Parser.unescapeEntities(editor, true);
        try {
            if (editor.isEmpty()) return failed(this).feedback("xss-mitigation-3-no-code").build();
            Document doc = Jsoup.parse(unescapedString);
            String[] lines = unescapedString.split("<html>");

            String include = (lines[0]);
            String fistNameElement = doc.select("body > table > tbody > tr:nth-child(1) > td:nth-child(2)").first().text();
            String lastNameElement = doc.select("body > table > tbody > tr:nth-child(2) > td:nth-child(2)").first().text();

            Boolean includeCorrect = false;
            Boolean firstNameCorrect = false;
            Boolean lastNameCorrect = false;

            if (include.contains("<%@") && include.contains("taglib") && include.contains("uri=\"https://www.owasp.org/index.php/OWASP_Java_Encoder_Project\"") && include.contains("%>")) {
                includeCorrect = true;
            }
            if (fistNameElement.equals("${e:forHtml(param.first_name)}")) {
                firstNameCorrect = true;
            }
            if (lastNameElement.equals("${e:forHtml(param.last_name)}")) {
                lastNameCorrect = true;
            }

            if (includeCorrect && firstNameCorrect && lastNameCorrect) {
                System.out.println("true");
                return success(this).feedback("xss-mitigation-3-success").build();
            } else {
                return failed(this).feedback("xss-mitigation-3-failure").build();
            }
        } catch (Exception e) {
            return failed(this).output(e.getMessage()).build();
        }
    }
}
