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

import io.jsonwebtoken.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@RestController
@AssignmentHints({"jwt-refresh-hint1", "jwt-refresh-hint2", "jwt-refresh-hint3", "jwt-refresh-hint4"})
public class JWTRefreshEndpoint extends AssignmentEndpoint {

    public static final String PASSWORD = "bm5nhSkxCXZkKRy4";
    private static final String JWT_PASSWORD = "bm5n3SkxCX4kKRy4";
    private static final List<String> validRefreshTokens = new ArrayList<>();

    @PostMapping(value = "/JWT/refresh/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity follow(@RequestBody(required = false) Map<String, Object> json) {
        if (json == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String user = (String) json.get("user");
        String password = (String) json.get("password");

        if ("Jerry".equalsIgnoreCase(user) && PASSWORD.equals(password)) {
            return ok(createNewTokens(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private Map<String, Object> createNewTokens(String user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("admin", "false");
        claims.put("user", user);
        String token = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toDays(10)))
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, JWT_PASSWORD)
                .compact();
        Map<String, Object> tokenJson = new HashMap<>();
        String refreshToken = RandomStringUtils.randomAlphabetic(20);
        validRefreshTokens.add(refreshToken);
        tokenJson.put("access_token", token);
        tokenJson.put("refresh_token", refreshToken);
        return tokenJson;
    }

    @PostMapping("/JWT/refresh/checkout")
    @ResponseBody
    public ResponseEntity<?> checkout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Jwt jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(token.replace("Bearer ", ""));
            Claims claims = (Claims) jwt.getBody();
            String user = (String) claims.get("user");
            if ("Tom".equals(user)) {
                return ok(trackProgress(success().build()));
            }
            return ok(trackProgress(failed().feedback("jwt-refresh-not-tom").feedbackArgs(user).build()));
        } catch (ExpiredJwtException e) {
            return ok(trackProgress(failed().output(e.getMessage()).build()));
        } catch (JwtException e) {
            return ok(trackProgress(failed().feedback("jwt-invalid-token").build()));
        }
    }

    @PostMapping("/JWT/refresh/newToken")
    @ResponseBody
    public ResponseEntity newToken(@RequestHeader(value = "Authorization", required = false) String token,
                                   @RequestBody(required = false) Map<String, Object> json) {
        if (token == null || json == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String user;
        String refreshToken;
        try {
            Jwt<Header, Claims> jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(token.replace("Bearer ", ""));
            user = (String) jwt.getBody().get("user");
            refreshToken = (String) json.get("refresh_token");
        } catch (ExpiredJwtException e) {
            user = (String) e.getClaims().get("user");
            refreshToken = (String) json.get("refresh_token");
        }

        if (user == null || refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (validRefreshTokens.contains(refreshToken)) {
            validRefreshTokens.remove(refreshToken);
            return ok(createNewTokens(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
