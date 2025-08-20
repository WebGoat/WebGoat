/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.hijacksession;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import jakarta.servlet.http.Cookie;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.lessons.hijacksession.cas.Authentication;
import org.owasp.webgoat.lessons.hijacksession.cas.HijackSessionAuthenticationProvider;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class HijackSessionAssignmentTest extends LessonTest {

  private static final String COOKIE_NAME = "hijack_cookie";
  private static final String LOGIN_CONTEXT_PATH = "/HijackSession/login";

  @MockBean
  Authentication authenticationMock;

  @MockBean HijackSessionAuthenticationProvider providerMock;

  @Test
  void testValidCookie() throws Exception {
    lenient().when(authenticationMock.isAuthenticated()).thenReturn(true);
    lenient()
        .when(providerMock.authenticate(any(Authentication.class)))
        .thenReturn(authenticationMock);

    Cookie cookie = new Cookie(COOKIE_NAME, "value");

    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .cookie(cookie)
                .param("username", "")
                .param("password", ""));

    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void testBlankCookie() throws Exception {
    lenient().when(authenticationMock.isAuthenticated()).thenReturn(false);
    lenient()
        .when(providerMock.authenticate(any(Authentication.class)))
        .thenReturn(authenticationMock);
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_CONTEXT_PATH)
                .param("username", "webgoat")
                .param("password", "webgoat"));

    result.andExpect(cookie().value(COOKIE_NAME, not(emptyString())));
    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
