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

package org.owasp.webgoat.crypto;

import java.util.Base64;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncodingAssignment extends AssignmentEndpoint {

	public static String getBasicAuth(String username, String password) {
    	return Base64.getEncoder().encodeToString(username.concat(":").concat(password).getBytes());
    }
	
	@GetMapping(path="/crypto/encoding/basic",produces=MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getBasicAuth(HttpServletRequest request) {
		
		String basicAuth = (String) request.getSession().getAttribute("basicAuth");
		String username = request.getUserPrincipal().getName();
		if (basicAuth == null) {
			String password = HashingAssignment.SECRETS[new Random().nextInt(HashingAssignment.SECRETS.length)];
			basicAuth = getBasicAuth(username, password);
			request.getSession().setAttribute("basicAuth", basicAuth);
		}
		return "Authorization: Basic ".concat(basicAuth);
    }
	
    @PostMapping("/crypto/encoding/basic-auth")
    @ResponseBody
    public AttackResult completed(HttpServletRequest request, @RequestParam String answer_user, @RequestParam String answer_pwd) {
    	String basicAuth = (String) request.getSession().getAttribute("basicAuth");
    	if (basicAuth !=null && answer_user!=null && answer_pwd !=null 
        		&& basicAuth.equals(getBasicAuth(answer_user,answer_pwd))) 
        {
            return trackProgress(success()
                .feedback("crypto-encoding.success")
                .build());
        } else {
            return trackProgress(failed().feedback("crypto-encoding.empty").build());
        }
    }
}
