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

package org.owasp.webgoat.xxe;

import org.apache.commons.exec.OS;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AssignmentHints({"xxe.hints.content.type.xxe.1", "xxe.hints.content.type.xxe.2"})
public class ContentTypeAssignment extends AssignmentEndpoint {

    private static final String[] DEFAULT_LINUX_DIRECTORIES = {"usr", "etc", "var"};
    private static final String[] DEFAULT_WINDOWS_DIRECTORIES = {"Windows", "Program Files (x86)", "Program Files", "pagefile.sys"};

    @Value("${webgoat.server.directory}")
    private String webGoatHomeDirectory;
    @Autowired
    private WebSession webSession;
    @Autowired
    private Comments comments;

    @PostMapping(path = "xxe/content-type")
    @ResponseBody
    public AttackResult createNewUser(HttpServletRequest request, @RequestBody String commentStr, @RequestHeader("Content-Type") String contentType) throws Exception {
        AttackResult attackResult = failed(this).build();

        if (APPLICATION_JSON_VALUE.equals(contentType)) {
            comments.parseJson(commentStr).ifPresent(c -> comments.addComment(c, true));
            attackResult = failed(this).feedback("xxe.content.type.feedback.json").build();
        }

        if (null != contentType && contentType.contains(MediaType.APPLICATION_XML_VALUE)) {
            String error = "";
            try {
            	boolean secure = false;
            	if (null != request.getSession().getAttribute("applySecurity")) {
            		secure = true;
            	}
                Comment comment = comments.parseXml(commentStr, secure);
                comments.addComment(comment, false);
                if (checkSolution(comment)) {
                    attackResult = success(this).build();
                }
            } catch (Exception e) {
                error = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e);
                attackResult = failed(this).feedback("xxe.content.type.feedback.xml").output(error).build();
            }
        }

        return attackResult;
    }

   private boolean checkSolution(Comment comment) {
       String[] directoriesToCheck = OS.isFamilyMac() || OS.isFamilyUnix() ? DEFAULT_LINUX_DIRECTORIES : DEFAULT_WINDOWS_DIRECTORIES;
       boolean success = false;
       for (String directory : directoriesToCheck) {
           success |= org.apache.commons.lang3.StringUtils.contains(comment.getText(), directory);
       }
       return success;
   } 

}
