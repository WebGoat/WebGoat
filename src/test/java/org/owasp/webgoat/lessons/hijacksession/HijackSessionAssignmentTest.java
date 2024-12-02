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

/***
 *
 * @author Angel Olle Blazquez
 *
 */
class HijackSessionAssignmentTest extends LessonTest {

  private static final String COOKIE_NAME = "hijack_cookie";
  private static final String LOGIN_CONTEXT_PATH = "/HijackSession/login";

  @MockBean Authentication authenticationMock;

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
