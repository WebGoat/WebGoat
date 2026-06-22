/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.missingac;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MissingFunctionYourHashTest extends LessonTest {

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  void hashDoesNotMatch() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/access-control/user-hash").param("userHash", "42"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  void hashMatches() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/user-hash")
                .param("userHash", "SVtOlaa+ER+w2eoIIVE5/77umvhcsh5V8UyDLUa1Itg="))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }
}
