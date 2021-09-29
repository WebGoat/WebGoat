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
import io.jsonwebtoken.Jwts;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.owasp.webgoat.jwt.JWTSecretKeyEndpoint.JWT_SECRET;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class JWTSecretKeyEndpointTest extends LessonTest {

    @Autowired
    private JWT jwt;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(jwt);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    private Claims createClaims(String username) {
        Claims claims = Jwts.claims();
        claims.put("admin", "true");
        claims.put("user", "Tom");
        claims.setExpiration(Date.from(Instant.now().plus(Duration.ofDays(1))));
        claims.setIssuedAt(Date.from(Instant.now().plus(Duration.ofDays(1))));
        claims.setIssuer("iss");
        claims.setAudience("aud");
        claims.setSubject("sub");
        claims.put("username", username);
        claims.put("Email", "webgoat@webgoat.io");
        claims.put("Role", new String[]{"user"});
        return claims;
    }

    @Test
    public void solveAssignment() throws Exception {
        Claims claims = createClaims("WebGoat");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void solveAssignmentWithLowercase() throws Exception {
        Claims claims = createClaims("webgoat");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void oneOfClaimIsMissingShouldNotSolveAssignment() throws Exception {
        Claims claims = createClaims("WebGoat");
        claims.remove("aud");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-secret-claims-missing"))));
    }

    @Test
    public void incorrectUser() throws Exception {
        Claims claims = createClaims("Tom");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-secret-incorrect-user", "default", "Tom"))));
    }

    @Test
    public void incorrectToken() throws Exception {
        Claims claims = createClaims("Tom");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, "wrong_password").compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
    }

    @Test
    void unsignedToken() throws Exception {
        Claims claims = createClaims("WebGoat");
        String token = Jwts.builder().setClaims(claims).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
    }
}