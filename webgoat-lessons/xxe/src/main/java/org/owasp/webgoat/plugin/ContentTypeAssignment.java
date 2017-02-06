package org.owasp.webgoat.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
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
@AssignmentPath("XXE/content-type")
@AssignmentHints({"xxe.hints.content.type.xxe.1", "xxe.hints.content.type.xxe.2"})
public class ContentTypeAssignment extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewUser(@RequestBody String userInfo, @RequestHeader("Content-Type") String contentType) throws Exception {
        User user = new User();
        AttackResult attackResult = failed().build();
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            user = parseJson(userInfo);
            attackResult = failed().feedback("xxe.content.type.feedback.json").build();
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
            user = parseXml(userInfo);
            attackResult = failed().feedback("xxe.content.type.feedback.xml").build();
        }

        if (checkSolution(user)) {
            attackResult = success().output("xxe.content.output").outputArgs(user.getUsername()).build();
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
