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

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;

@RestController
public class DOMCrossSiteScripting extends AssignmentEndpoint {

    @PostMapping("/CrossSiteScripting/phone-home-xss")
    @ResponseBody
    public AttackResult completed(@RequestParam Integer param1,
                                  @RequestParam Integer param2, HttpServletRequest request) {
        UserSessionData userSessionData = getUserSessionData();
        SecureRandom number = new SecureRandom();
        userSessionData.setValue("randValue", String.valueOf(number.nextInt()));

        if (param1 == 42 && param2 == 24 && request.getHeader("webgoat-requested-by").equals("dom-xss-vuln")) {
            return success(this).output("phoneHome Response is " + userSessionData.getValue("randValue").toString()).build();
        } else {
            return failed(this).build();
        }
    }
}
// something like ... http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere%3Cscript%3Ewebgoat.customjs.phoneHome();%3C%2Fscript%3E--andMoreGarbageHere
// or http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere<script>webgoat.customjs.phoneHome();<%2Fscript>