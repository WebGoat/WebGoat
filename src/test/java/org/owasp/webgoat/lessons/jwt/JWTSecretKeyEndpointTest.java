/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.jwt;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.hamcrest.Matchers.is;
import static org.owasp.webgoat.lessons.jwt.JWTSecretKeyEndpoint.JWT_SECRET;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.WithWebGoatUser;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WithWebGoatUser
public class JWTSecretKeyEndpointTest extends LessonTest {

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
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
    claims.put("Role", new String[] {"user"});
    return claims;
  }

  @Test
  public void solveAssignment() throws Exception {
    Claims claims = createClaims("WebGoat");
    String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/secret").param("token", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void solveAssignmentWithLowercase() throws Exception {
    Claims claims = createClaims("webgoat");
    String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/secret").param("token", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void oneOfClaimIsMissingShouldNotSolveAssignment() throws Exception {
    Claims claims = createClaims("WebGoat");
    claims.remove("aud");
    String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/secret").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback", CoreMatchers.is(messages.getMessage("jwt-secret-claims-missing"))));
  }

  @Test
  public void incorrectUser() throws Exception {
    Claims claims = createClaims("Tom");
    String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/secret").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(
                    messages.getMessage("jwt-secret-incorrect-user", "default", "Tom"))));
  }

  @Test
  public void incorrectToken() throws Exception {
    Claims claims = createClaims("Tom");
    String token = Jwts.builder().setClaims(claims).signWith(HS512, "wrong_password").compact();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/secret").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
  }

  @Test
  void unsignedToken() throws Exception {
    Claims claims = createClaims("WebGoat");
    String token = Jwts.builder().setClaims(claims).compact();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/secret").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
  }
}
