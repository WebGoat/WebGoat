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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController
@AssignmentHints({"crypto-hashing.hints.1","crypto-hashing.hints.2"})
public class HashingAssignment extends AssignmentEndpoint {
	
	public static final String[] SECRETS = {"secret","admin","password", "123456", "passw0rd"};

	@RequestMapping(path="/crypto/hashing/md5",produces=MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getMd5(HttpServletRequest request) throws NoSuchAlgorithmException {
		
		String md5Hash = (String) request.getSession().getAttribute("md5Hash");
		if (md5Hash == null) {
			
			String secret = SECRETS[new Random().nextInt(SECRETS.length)];
	         
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(secret.getBytes());
		    byte[] digest = md.digest();
		    md5Hash = DatatypeConverter
		      .printHexBinary(digest).toUpperCase();
			request.getSession().setAttribute("md5Hash", md5Hash);
			request.getSession().setAttribute("md5Secret", secret);
		}
		return md5Hash;
    }
	
	@RequestMapping(path="/crypto/hashing/sha256",produces=MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getSha256(HttpServletRequest request) throws NoSuchAlgorithmException {
		
		String sha256 = (String) request.getSession().getAttribute("sha256");
		if (sha256 == null) {
			String secret = SECRETS[new Random().nextInt(SECRETS.length)];
		    sha256 = getHash(secret, "SHA-256");
			request.getSession().setAttribute("sha256Hash", sha256);
			request.getSession().setAttribute("sha256Secret", secret);
		}
		return sha256;
    }
	
    @PostMapping("/crypto/hashing")
    @ResponseBody
    public AttackResult completed(HttpServletRequest request, @RequestParam String answer_pwd1, @RequestParam String answer_pwd2) {
        
    	String md5Secret = (String) request.getSession().getAttribute("md5Secret");
    	String sha256Secret = (String) request.getSession().getAttribute("sha256Secret");
    	
    	if (answer_pwd1!=null && answer_pwd2 !=null) {
        	if (answer_pwd1.equals(md5Secret)
        		&& answer_pwd2.equals(sha256Secret)) {
        		return success(this)
        				.feedback("crypto-hashing.success")
        				.build();
        	} else if (answer_pwd1.equals(md5Secret)
            		|| answer_pwd2.equals(sha256Secret)) {
        		return failed(this).feedback("crypto-hashing.oneok").build();
        	} 
        } 
        return failed(this).feedback("crypto-hashing.empty").build(); 
    }
    
    public static String getHash(String secret, String algorithm) throws NoSuchAlgorithmException {
    	MessageDigest md = MessageDigest.getInstance(algorithm);
	    md.update(secret.getBytes());
	    byte[] digest = md.digest();
	    return DatatypeConverter
	      .printHexBinary(digest).toUpperCase();
    }
    
}
