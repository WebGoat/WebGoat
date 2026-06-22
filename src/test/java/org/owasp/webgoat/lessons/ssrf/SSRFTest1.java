/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.ssrf;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class SSRFTest1 extends LessonTest {

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void modifyUrlTom() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/SSRF/task1").param("url", "images/tom.png"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  public void modifyUrlJerry() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/SSRF/task1").param("url", "images/jerry.png"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void modifyUrlCat() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/SSRF/task1").param("url", "images/cat.jpg"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }
}
