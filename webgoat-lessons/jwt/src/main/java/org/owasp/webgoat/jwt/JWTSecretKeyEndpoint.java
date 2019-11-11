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

package org.owasp.webgoat.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@RestController
@AssignmentHints({"jwt-secret-hint1", "jwt-secret-hint2", "jwt-secret-hint3"})
public class JWTSecretKeyEndpoint extends AssignmentEndpoint {

    public static final String[] SECRETS = {"victory", "business", "available", "shipping", "washington"};
    public static final String JWT_SECRET = TextCodec.BASE64.encode(SECRETS[new Random().nextInt(SECRETS.length)]);
    private static final String WEBGOAT_USER = "WebGoat";
    private static final List<String> expectedClaims = List.of("iss", "iat", "exp", "aud", "sub", "username", "Email", "Role");

    @RequestMapping(path = "/JWT/secret/gettoken", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getSecretToken() {
        return Jwts.builder()
                .setIssuer("WebGoat Token Builder")
                .setAudience("webgoat.org")
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(Date.from(Instant.now().plusSeconds(60)))
                .setSubject("tom@webgoat.org")
                .claim("username", "Tom")
                .claim("Email", "tom@webgoat.org")
                .claim("Role", new String[]{"Manager", "Project Administrator"})
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
    }

    @PostMapping("/JWT/secret")
    @ResponseBody
    public AttackResult login(@RequestParam String token) {
        try {
            Jwt jwt = Jwts.parser().setSigningKey(JWT_SECRET).parse(token);
            Claims claims = (Claims) jwt.getBody();
            if (!claims.keySet().containsAll(expectedClaims)) {
                return failed(this).feedback("jwt-secret-claims-missing").build();
            } else {
                String user = (String) claims.get("username");

                if (WEBGOAT_USER.equalsIgnoreCase(user)) {
                    return success(this).build();
                } else {
                    return failed(this).feedback("jwt-secret-incorrect-user").feedbackArgs(user).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed(this).feedback("jwt-invalid-token").output(e.getMessage()).build();
        }
    }
}
