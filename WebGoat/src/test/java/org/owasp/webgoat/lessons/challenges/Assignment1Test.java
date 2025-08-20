/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.net.InetAddress;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.lessons.challenges.challenge1.ImageServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class Assignment1Test extends LessonTest {

  @Autowired private Flags flags;

  @BeforeEach
  public void setup() {}

  @Test
  void success() throws Exception {
    InetAddress addr = InetAddress.getLocalHost();
    String host = addr.getHostAddress();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/challenge/1")
                .header("X-Forwarded-For", host)
                .param("username", "admin")
                .param(
                    "password",
                    SolutionConstants.PASSWORD.replace(
                        "1234", String.format("%04d", ImageServlet.PINCODE))))
        .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("flag: " + flags.getFlag(1))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void wrongPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/challenge/1")
                .param("username", "admin")
                .param("password", "wrong"))
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
