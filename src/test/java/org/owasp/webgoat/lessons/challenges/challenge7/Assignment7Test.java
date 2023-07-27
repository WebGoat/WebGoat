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
 * Getting Source
 * ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.challenges.challenge7;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class Assignment7Test extends AssignmentEndpointTest {
  private MockMvc mockMvc;

  private static final String CHALLENGE_PATH = "/challenge/7";
  private static final String RESET_PASSWORD_PATH = CHALLENGE_PATH + "/reset-password";
  private static final String GIT_PATH = CHALLENGE_PATH + "/.git";

  @Mock private RestTemplate restTemplate;

  @Value("${webwolf.mail.url}")
  String webWolfMailURL;

  @BeforeEach
  void setup() {
    Assignment7 assignment7 = new Assignment7(new Flags(), restTemplate, webWolfMailURL);
    init(assignment7);
    mockMvc = standaloneSetup(assignment7).build();
  }

  @Test
  @DisplayName("Reset password test")
  void resetPasswordTest() throws Exception {
    ResultActions result =
        mockMvc.perform(MockMvcRequestBuilders.get(RESET_PASSWORD_PATH + "/any"));
    result.andExpect(status().is(equalTo(HttpStatus.I_AM_A_TEAPOT.value())));

    result =
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                RESET_PASSWORD_PATH + "/" + Assignment7.ADMIN_PASSWORD_LINK));
    result.andExpect(status().is(equalTo(HttpStatus.ACCEPTED.value())));
  }

  @Test
  @DisplayName("Send password reset link test")
  void sendPasswordResetLinkTest() throws Exception {
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post(CHALLENGE_PATH)
                .param("email", "webgoat@webgoat-cloud.net"));
    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  @DisplayName("git test")
  void gitTest() throws Exception {
    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GIT_PATH));
    result.andExpect(content().contentType("application/zip"));
  }
}
