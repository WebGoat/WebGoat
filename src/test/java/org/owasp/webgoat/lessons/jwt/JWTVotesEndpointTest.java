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

package org.owasp.webgoat.lessons.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.owasp.webgoat.lessons.jwt.JWTVotesEndpoint.JWT_PASSWORD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import java.util.Map;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.WithWebGoatUser;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WithWebGoatUser
public class JWTVotesEndpointTest extends LessonTest {

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void solveAssignment() throws Exception {
    // Create new token and set alg to none and do not sign it
    Claims claims = Jwts.claims();
    claims.put("admin", "true");
    claims.put("user", "Tom");
    String token = Jwts.builder().setClaims(claims).setHeaderParam("alg", "none").compact();

    // Call the reset endpoint
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/JWT/votings")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("access_token", token)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void solveAssignmentWithBoolean() throws Exception {
    // Create new token and set alg to none and do not sign it
    Claims claims = Jwts.claims();
    claims.put("admin", true);
    claims.put("user", "Tom");
    String token = Jwts.builder().setClaims(claims).setHeaderParam("alg", "none").compact();

    // Call the reset endpoint
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/JWT/votings")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("access_token", token)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void resetWithoutTokenShouldNotWork() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/JWT/votings").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
  }

  @Test
  public void guestShouldNotGetAToken() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/JWT/votings/login")
                .contentType(MediaType.APPLICATION_JSON)
                .param("user", "Guest"))
        .andExpect(status().isUnauthorized())
        .andExpect(cookie().value("access_token", ""));
  }

  @Test
  public void tomShouldGetAToken() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/JWT/votings/login")
                .contentType(MediaType.APPLICATION_JSON)
                .param("user", "Tom"))
        .andExpect(status().isOk())
        .andExpect(cookie().value("access_token", containsString("eyJhbGciOiJIUzUxMiJ9.")));
  }

  @Test
  public void guestShouldNotSeeNumberOfVotes() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/JWT/votings").cookie(new Cookie("access_token", "")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].numberOfVotes").doesNotExist())
        .andExpect(jsonPath("$[0].votingAllowed").doesNotExist())
        .andExpect(jsonPath("$[0].average").doesNotExist());
  }

  @Test
  public void tomShouldSeeNumberOfVotes() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/JWT/votings/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("user", "Tom"))
            .andExpect(status().isOk())
            .andReturn();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/JWT/votings").cookie(result.getResponse().getCookies()[0]))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].numberOfVotes").exists())
        .andExpect(jsonPath("$[0].votingAllowed").exists())
        .andExpect(jsonPath("$[0].average").exists());
  }

  @Test
  public void invalidTokenShouldSeeGuestView() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/JWT/votings")
                .cookie(new Cookie("access_token", "abcd.efgh.ijkl")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].numberOfVotes").doesNotExist())
        .andExpect(jsonPath("$[0].votingAllowed").doesNotExist())
        .andExpect(jsonPath("$[0].average").doesNotExist());
  }

  @Test
  public void tomShouldBeAbleToVote() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/JWT/votings/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("user", "Tom"))
            .andExpect(status().isOk())
            .andReturn();
    Cookie cookie = result.getResponse().getCookie("access_token");

    result =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/JWT/votings").cookie(cookie))
            .andExpect(status().isOk())
            .
            /*andDo(print()).*/ andReturn();
    Object[] nodes =
        new ObjectMapper().readValue(result.getResponse().getContentAsString(), Object[].class);
    int currentNumberOfVotes =
        (int) findNodeByTitle(nodes, "Admin lost password").get("numberOfVotes");

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/votings/Admin lost password").cookie(cookie))
        .andExpect(status().isAccepted());
    result =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/JWT/votings").cookie(cookie))
            .andExpect(status().isOk())
            .andReturn();
    nodes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Object[].class);
    int numberOfVotes = (int) findNodeByTitle(nodes, "Admin lost password").get("numberOfVotes");
    assertThat(numberOfVotes).isEqualTo(currentNumberOfVotes + 1);
  }

  private Map<String, Object> findNodeByTitle(Object[] nodes, String title) {
    for (Object n : nodes) {
      Map<String, Object> node = (Map<String, Object>) n;
      if (node.get("title").equals(title)) {
        return node;
      }
    }
    return null;
  }

  @Test
  public void guestShouldNotBeAbleToVote() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/JWT/votings/Admin lost password")
                .cookie(new Cookie("access_token", "")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void unknownUserWithValidTokenShouldNotBeAbleToVote() throws Exception {
    Claims claims = Jwts.claims();
    claims.put("admin", "true");
    claims.put("user", "Intruder");
    String token =
        Jwts.builder()
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, JWT_PASSWORD)
            .setClaims(claims)
            .compact();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/JWT/votings/Admin lost password")
                .cookie(new Cookie("access_token", token)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void unknownUserShouldSeeGuestView() throws Exception {
    Claims claims = Jwts.claims();
    claims.put("admin", "true");
    claims.put("user", "Intruder");
    String token =
        Jwts.builder()
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, JWT_PASSWORD)
            .setClaims(claims)
            .compact();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/JWT/votings").cookie(new Cookie("access_token", token)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].numberOfVotes").doesNotExist())
        .andExpect(jsonPath("$[0].votingAllowed").doesNotExist())
        .andExpect(jsonPath("$[0].average").doesNotExist());
  }
}
