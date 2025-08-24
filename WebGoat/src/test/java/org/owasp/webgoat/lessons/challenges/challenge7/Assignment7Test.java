/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge7;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

class Assignment7Test extends LessonTest {
  private static final String CHALLENGE_PATH = "/challenge/7";
  private static final String RESET_PASSWORD_PATH = CHALLENGE_PATH + "/reset-password";
  private static final String GIT_PATH = CHALLENGE_PATH + "/.git";

  @MockBean private RestTemplate restTemplate;

  @Value("${webwolf.mail.url}")
  String webWolfMailURL;

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
