/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
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

package org.owasp.webgoat.lessons.spoofcookie;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import jakarta.servlet.http.Cookie;
import java.util.stream.Stream;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/***
 *
 * @author Angel Olle Blazquez
 *
 */

@ExtendWith(MockitoExtension.class)
class SpoofCookieAssignmentTest extends AssignmentEndpointTest {

  private MockMvc mockMvc;
  private static final String COOKIE_NAME = "spoof_auth";
  private static final String LOGIN_CONTEXT_PATH = "/SpoofCookie/login";
  private static final String ERASE_COOKIE_CONTEXT_PATH = "/SpoofCookie/cleanup";

  @BeforeEach
  void setup() {
    SpoofCookieAssignment spoofCookieAssignment = new SpoofCookieAssignment();
    init(spoofCookieAssignment);
    mockMvc = standaloneSetup(spoofCookieAssignment).build();
  }

  @Test
  @DisplayName("Lesson completed")
  void success() throws Exception {
    Cookie cookie = new Cookie(COOKIE_NAME, "NjI2MTcwNGI3YTQxNGE1OTU2NzQ2ZDZmNzQ=");

    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .cookie(cookie)
                .param("username", "")
                .param("password", ""));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  @DisplayName("Valid credentials login without authentication cookie")
  void validLoginWithoutCookieTest() throws Exception {
    String username = "webgoat";
    String password = "webgoat";

    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .param("username", username)
                .param("password", password));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    result.andExpect(cookie().value(COOKIE_NAME, not(emptyString())));
  }

  @ParameterizedTest
  @MethodSource("providedCookieValues")
  @DisplayName(
      "Tests different invalid/valid -but not solved- cookie flow scenarios: "
          + "1.- Invalid encoded cookie sent. "
          + "2.- Valid cookie login (not tom) sent. "
          + "3.- Valid cookie with not known username sent ")
  void cookieLoginNotSolvedFlow(String cookieValue) throws Exception {
    Cookie cookie = new Cookie(COOKIE_NAME, cookieValue);
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .cookie(cookie)
                .param("username", "")
                .param("password", ""))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  @DisplayName("UnsatisfiedServletRequestParameterException test for missing username")
  void invalidLoginWithUnsatisfiedServletRequestParameterExceptionOnUsernameMissing()
      throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH).param("password", "anypassword"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("UnsatisfiedServletRequestParameterException test for missing password")
  void invalidLoginWithUnsatisfiedServletRequestParameterExceptionOnPasswordMissing()
      throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH).param("username", "webgoat"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("Invalid blank credentials login")
  void invalidLoginWithBlankCredentials() throws Exception {
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .param("username", "")
                .param("password", ""));

    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  @DisplayName("Invalid blank password login")
  void invalidLoginWithBlankPassword() throws Exception {
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .param("username", "webgoat")
                .param("password", ""));

    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  @DisplayName("cheater test")
  void cheat() throws Exception {
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .param("username", "tom")
                .param("password", "apasswordfortom"));

    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  @DisplayName("Invalid login with tom username")
  void invalidTomLogin() throws Exception {
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .param("username", "tom")
                .param("password", ""));

    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  @DisplayName("Erase authentication cookie")
  void eraseAuthenticationCookie() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(ERASE_COOKIE_CONTEXT_PATH))
        .andExpect(status().isOk())
        .andExpect(cookie().maxAge(COOKIE_NAME, 0))
        .andExpect(cookie().value(COOKIE_NAME, ""));
  }

  private static Stream<Arguments> providedCookieValues() {
    return Stream.of(
        Arguments.of("NjI2MTcwNGI3YTQxNGE1OTUNzQ2ZDZmNzQ="),
        Arguments.of("NjI2MTcwNGI3YTQxNGE1OTU2NzQ3NDYxNmY2NzYyNjU3Nw=="),
        Arguments.of("NmQ0NjQ1Njc0NjY4NGY2Mjc0NjQ2YzY1Njc2ZTYx"));
  }
}
