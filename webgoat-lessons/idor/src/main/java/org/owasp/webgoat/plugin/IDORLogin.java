package org.owasp.webgoat.plugin;

import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.lessons.AttackResult;

import org.owasp.webgoat.session.UserSessionData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 * @author misfir3
 * @version $Id: $Id
 * @since January 3, 2017
 */

@Path("/IDOR/login")
public class IDORLogin extends AssignmentEndpoint {

    private Map<String,Map<String,String>> idorUserInfo = new HashMap<>();

    public void initIDORInfo() {

        idorUserInfo.put("tom",new HashMap<String,String>());
        idorUserInfo.get("tom").put("password","cat");
        idorUserInfo.get("tom").put("id","2342384");
        idorUserInfo.get("tom").put("color","yellow");
        idorUserInfo.get("tom").put("size","small");

        idorUserInfo.put("bill",new HashMap<String,String>());
        idorUserInfo.get("bill").put("password","buffalo");
        idorUserInfo.get("bill").put("id","2342388");
        idorUserInfo.get("bill").put("color","brown");
        idorUserInfo.get("bill").put("size","large");

    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam String username, @RequestParam String password, HttpServletRequest request) throws IOException {

        initIDORInfo();
        UserSessionData userSessionData = getUserSessionData();

        if (idorUserInfo.containsKey(username)) {
            if ("tom".equals(username) && idorUserInfo.get("tom").get("password").equals(password)) {
                userSessionData.setValue("idor-authenticated-as", username);
                userSessionData.setValue("idor-authenticated-user-id", idorUserInfo.get(username).get("id"));
                return trackProgress(AttackResult.success("You are now logged in as " + username + ". Please proceed."));
            } else {
                return trackProgress(AttackResult.failed("credentials provided are not correct"));
            }
        } else {
            return trackProgress(AttackResult.failed("credentials provided are not correct"));
        }
    }

//        userSessionData.setValue("foo","bar");
//        System.out.println("*** value set");
//        System.out.println("*** fetching value");
//        System.out.println(userSessionData.getValue("foo"));
//        System.out.println("*** DONE fetching value");
//        return trackProgress(AttackResult.failed("You are close, try again"));

}
