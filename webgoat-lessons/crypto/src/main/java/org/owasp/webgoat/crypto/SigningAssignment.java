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

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@AssignmentHints({"crypto-signing.hints.1","crypto-signing.hints.2", "crypto-signing.hints.3", "crypto-signing.hints.4"})
@Slf4j
public class SigningAssignment extends AssignmentEndpoint {
	
	@RequestMapping(path="/crypto/signing/getprivate",produces=MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getPrivateKey(HttpServletRequest request) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		
		String privateKey = (String) request.getSession().getAttribute("privateKeyString");
		if (privateKey == null) {			
			KeyPair keyPair = CryptoUtil.generateKeyPair();
			privateKey = CryptoUtil.getPrivateKeyInPEM(keyPair);
			request.getSession().setAttribute("privateKeyString", privateKey);
			request.getSession().setAttribute("keyPair", keyPair);
		}
		return privateKey;
    }
	
    @PostMapping("/crypto/signing/verify")
    @ResponseBody
    public AttackResult completed(HttpServletRequest request, @RequestParam String modulus, @RequestParam String signature) {
		
		String tempModulus = modulus;/* used to validate the modulus of the public key but might need to be corrected */
    	KeyPair keyPair = (KeyPair) request.getSession().getAttribute("keyPair");
		RSAPublicKey rsaPubKey = (RSAPublicKey) keyPair.getPublic();
		if (tempModulus.length() == 512) {
			tempModulus = "00".concat(tempModulus);
		}
		if (!DatatypeConverter.printHexBinary(rsaPubKey.getModulus().toByteArray()).equals(tempModulus.toUpperCase())) {
			log.warn("modulus {} incorrect", modulus);
			return failed(this).feedback("crypto-signing.modulusnotok").build();
		}
		/* orginal modulus must be used otherwise the signature would be invalid */
		if (CryptoUtil.verifyMessage(modulus, signature, keyPair.getPublic())) {
			return success(this).feedback("crypto-signing.success").build();
		} else {
			log.warn("signature incorrect");
			return failed(this).feedback("crypto-signing.notok").build();
		}
       
    }
    
}
