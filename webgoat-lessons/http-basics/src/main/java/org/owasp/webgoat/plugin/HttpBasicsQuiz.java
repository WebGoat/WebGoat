package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * For details, please see http://webgoat.github.io
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
@AssignmentPath("/HttpBasics/attack2")
@AssignmentHints({"http-basics.hints.http_basic_quiz.1", "http-basics.hints.http_basic_quiz.2"})
public class HttpBasicsQuiz extends AssignmentEndpoint {

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody AttackResult completed(@RequestParam String answer, @RequestParam String magic_answer, @RequestParam String magic_num, HttpServletRequest request) throws IOException {
        if ("POST".equals(answer.toUpperCase()) && magic_answer.equals(magic_num)) {
	        return trackProgress(success().build());
	    } else {
	    	if (!"POST".equals(answer.toUpperCase())) {
                return trackProgress(failed().feedback("http-basics.incorrect").build());
 			}
	    	if (!magic_answer.equals(magic_num)){
                return trackProgress(failed().feedback("http-basics.magic").build());
	    	}
	    }
	    return trackProgress(failed().build());
	}
}
