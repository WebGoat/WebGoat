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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.owasp.webgoat.jwt.JWTRefreshEndpoint.PASSWORD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class JWTRefreshEndpointTest extends LessonTest {

    @Autowired
    private JWT jwt;

    @Before
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(jwt);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void solveAssignment() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        //First login to obtain tokens for Jerry
        var loginJson = Map.of("user", "Jerry", "password", PASSWORD);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginJson)))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, String> tokens = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");

        //Now create a new refresh token for Tom based on Toms old access token and send the refresh token of Jerry
        String accessTokenTom = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1MjYxMzE0MTEsImV4cCI6MTUyNjIxNzgxMSwiYWRtaW4iOiJmYWxzZSIsInVzZXIiOiJUb20ifQ.DCoaq9zQkyDH25EcVWKcdbyVfUL4c9D4jRvsqOqvi9iAd4QuqmKcchfbU8FNzeBNF9tLeFXHZLU4yRkq-bjm7Q";
        Map<String, Object> refreshJson = new HashMap<>();
        refreshJson.put("refresh_token", refreshToken);
        result = mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/newToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessTokenTom)
                .content(objectMapper.writeValueAsString(refreshJson)))
                .andExpect(status().isOk())
                .andReturn();
        tokens = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        accessTokenTom = tokens.get("access_token");

        //Now checkout with the new token from Tom
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/checkout")
                .header("Authorization", "Bearer " + accessTokenTom))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void checkoutWithTomsTokenFromAccessLogShouldFail() throws Exception {
        String accessTokenTom = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1MjYxMzE0MTEsImV4cCI6MTUyNjIxNzgxMSwiYWRtaW4iOiJmYWxzZSIsInVzZXIiOiJUb20ifQ.DCoaq9zQkyDH25EcVWKcdbyVfUL4c9D4jRvsqOqvi9iAd4QuqmKcchfbU8FNzeBNF9tLeFXHZLU4yRkq-bjm7Q";
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/checkout")
                .header("Authorization", "Bearer " + accessTokenTom))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output", CoreMatchers.containsString("JWT expired at")));
    }

    @Test
    public void checkoutWitRandomTokenShouldFail() throws Exception {
        String accessTokenTom = "eyJhbGciOiJIUzUxMiJ9.eyJpLXQiOjE1MjYxMzE0MTEsImV4cCI6MTUyNjIxNzgxMSwiYWRtaW4iOiJmYWxzZSIsInVzZXIiOiJUb20ifQ.DCoaq9zQkyDH25EcVWKcdbyVfUL4c9D4jRvsqOqvi9iAd4QuqmKcchfbU8FNzeBNF9tLeFXHZLU4yRkq-bjm7Q";
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/checkout")
                .header("Authorization", "Bearer " + accessTokenTom))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
    }

    @Test
    public void flowForJerryAlwaysWorks() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        var loginJson = Map.of("user", "Jerry", "password", PASSWORD);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginJson)))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, String> tokens = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        String accessToken = tokens.get("access_token");

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/checkout")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", is("User is not Tom but Jerry, please try again")));
    }

    @Test
    public void loginShouldNotWorkForJerryWithWrongPassword() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        var loginJson = Map.of("user", "Jerry", "password", PASSWORD + "wrong");
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginJson)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginShouldNotWorkForTom() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        var loginJson = Map.of("user", "Tom", "password", PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginJson)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void newTokenShouldWorkForJerry() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> loginJson = new HashMap<>();
        loginJson.put("user", "Jerry");
        loginJson.put("password", PASSWORD);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginJson)))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, String> tokens = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");

        var refreshJson = Map.of("refresh_token", refreshToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/newToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(refreshJson)))
                .andExpect(status().isOk());
    }

    @Test
    public void unknownRefreshTokenShouldGiveUnauthorized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> loginJson = new HashMap<>();
        loginJson.put("user", "Jerry");
        loginJson.put("password", PASSWORD);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginJson)))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, String> tokens = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        String accessToken = tokens.get("access_token");

        var refreshJson = Map.of("refresh_token", "wrong_refresh_token");
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/newToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(refreshJson)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void noTokenWhileCheckoutShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/checkout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void noTokenWhileRequestingNewTokenShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/newToken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void noTokenWhileLoginShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/refresh/login"))
                .andExpect(status().isUnauthorized());
    }
}