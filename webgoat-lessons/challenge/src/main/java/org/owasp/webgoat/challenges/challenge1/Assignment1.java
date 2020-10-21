package org.owasp.webgoat.challenges.challenge1;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.challenges.Flag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.owasp.webgoat.challenges.SolutionConstants.PASSWORD;

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
 * @author WebGoat
 * @version $Id: $Id
 * @since August 11, 2016
 */
@RestController
public class Assignment1 extends AssignmentEndpoint {

    @PostMapping("/challenge/1")
    @ResponseBody
    public AttackResult completed(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        boolean ipAddressKnown =  true;
        boolean passwordCorrect = "admin".equals(username) && PASSWORD.replace("1234", String.format("%04d",ImageServlet.PINCODE)).equals(password);
        if (passwordCorrect && ipAddressKnown) {
            return success(this).feedback("challenge.solved").feedbackArgs(Flag.FLAGS.get(1)).build();
        } else if (passwordCorrect) {
            return failed(this).feedback("ip.address.unknown").build();
        }
        return failed(this).build();
    }

    public static boolean containsHeader(HttpServletRequest request) {
        return StringUtils.hasText(request.getHeader("X-Forwarded-For"));
    }
}
