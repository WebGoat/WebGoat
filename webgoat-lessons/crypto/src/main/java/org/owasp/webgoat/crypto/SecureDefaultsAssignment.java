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

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@AssignmentHints({"crypto-secure-defaults.hints.1", "crypto-secure-defaults.hints.2", "crypto-secure-defaults.hints.3"})
public class SecureDefaultsAssignment extends AssignmentEndpoint {

    @PostMapping("/crypto/secure/defaults")
    @ResponseBody
    public AttackResult completed(@RequestParam String secretFileName,  @RequestParam String secretText) throws NoSuchAlgorithmException {
        if (secretFileName!=null && secretFileName.equals("default_secret")) {
        	if (secretText!=null && HashingAssignment.getHash(secretText, "SHA-256").equalsIgnoreCase("34de66e5caf2cb69ff2bebdc1f3091ecf6296852446c718e38ebfa60e4aa75d2")) {
        		return success(this)
        				.feedback("crypto-secure-defaults.success")
        				.build();
        	} else {
        		return failed(this).feedback("crypto-secure-defaults.messagenotok").build(); 
        	}
        } 
        return failed(this).feedback("crypto-secure-defaults.notok").build(); 
    }
}
