package org.owasp.webgoat.plugin;

import org.apache.commons.exec.OS;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
@AssignmentPath("xxe/content-type")
@AssignmentHints({"xxe.hints.content.type.xxe.1", "xxe.hints.content.type.xxe.2"})
public class ContentTypeAssignment extends AssignmentEndpoint {

    private final static String[] DEFAULT_LINUX_DIRECTORIES = {"usr", "etc", "var"}; 
    private final static String[] DEFAULT_WINDOWS_DIRECTORIES = {"Windows", "Program Files (x86)", "Program Files"};


    @Value("${webgoat.server.directory}")
    private String webGoatHomeDirectory;
    @Autowired
    private WebSession webSession;
    @Autowired
    private Comments comments;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewUser(@RequestBody String commentStr, @RequestHeader("Content-Type") String contentType) throws Exception {
        AttackResult attackResult = failed().build();

        if (APPLICATION_JSON_VALUE.equals(contentType)) {
            comments.parseJson(commentStr).ifPresent(c -> comments.addComment(c, true));
            attackResult = failed().feedback("xxe.content.type.feedback.json").build();
        }

        if (MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
            String error = "";
            try {
                Comment comment = comments.parseXml(commentStr);
                comments.addComment(comment, false);
                if (checkSolution(comment)) {
                    attackResult = success().build();
                }
            } catch (Exception e) {
                error = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e);
                attackResult = failed().feedback("xxe.content.type.feedback.xml").output(error).build();
            }
        }

        return trackProgress(attackResult);
    }

   private boolean checkSolution(Comment comment) {
       String[] directoriesToCheck = OS.isFamilyMac() || OS.isFamilyUnix() ? DEFAULT_LINUX_DIRECTORIES : DEFAULT_WINDOWS_DIRECTORIES;
       boolean success = true;
       for (String directory : directoriesToCheck) {
           success &= org.apache.commons.lang3.StringUtils.contains(comment.getText(), directory);
       }
       return success;
   } 

}
