/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.chromedevtools;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ChromeDevToolsTest extends LessonTest {

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void NetworkAssignmentTest_Success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/ChromeDevTools/network")
                .param("network_num", "123456")
                .param("number", "123456"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", Matchers.is(true)));
  }

  @Test
  public void NetworkAssignmentTest_Fail() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/ChromeDevTools/network")
                .param("network_num", "123456")
                .param("number", "654321"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", Matchers.is(false)));
  }
}
