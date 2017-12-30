package org.owasp.webgoat.plugin;



import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
@AssignmentPath("/CrossSiteScripting/attack5b")
public class CrossSiteScriptingLesson5b extends AssignmentEndpoint {

    @Autowired
    UserSessionData userSessionData;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam String isReflectedXSS)  throws IOException {
    // init
    System.out.println(userSessionData.getValue("xss-reflected5a-complete"));

    //TODO
//    if (null == userSessionData.getValue("xss-reflected5a-complete") || userSessionData.getValue("xss-reflected-5a-complete").equals("false")) {
//        //userSessionData.setValue("xss-reflected1-complete",(Object)"false");
//        return trackProgress(success().feedback("xss-reflected-5b-do5a-first").build());
//    }

    if (isReflectedXSS.toLowerCase().equals("no") || isReflectedXSS.toLowerCase().equals("n")) {
            //return trackProgress()
            return trackProgress(success().feedback("xss-reflected-5b-success").build());
        } else {
            return trackProgress(success().feedback("xss-reflected-5b-failure").build());
        }
    }
}
