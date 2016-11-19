package org.owasp.webgoat.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.model.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import static org.owasp.webgoat.plugin.SimpleXXE.checkSolution;
import static org.owasp.webgoat.plugin.SimpleXXE.parseXml;

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
 * @author nbaars
 * @version $Id: $Id
 * @since November 17, 2016
 */
public class ContentTypeAssignment extends Assignment {

    @Override
    public String getPath() {
        return "XXE/content-type";
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewUser(@RequestBody String userInfo, @RequestHeader("Content-Type") String contentType) throws Exception {
        User user = new User();
        AttackResult attackResult = AttackResult.failed("Try again!");
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            user = parseJson(userInfo);
            attackResult = AttackResult.failed("You are posting JSON which does not work with a XXE");
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
            user = parseXml(userInfo);
            attackResult = AttackResult.failed("You are posting XML but there is no XXE attack performed");
        }

        if (checkSolution(user)) {
            attackResult = AttackResult.success(String.format("Welcome %s", user.getUsername()));
        }
        return attackResult;
    }

    private User parseJson(String userInfo) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(userInfo, User.class);
        } catch (IOException e) {
            return new User();
        }
    }

}
