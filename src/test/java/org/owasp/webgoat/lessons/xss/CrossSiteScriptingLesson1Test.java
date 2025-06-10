/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xss;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class CrossSiteScriptingLesson1Test extends LessonTest {

  private static final String CONTEXT_PATH = "/CrossSiteScripting/attack1";

  @Test
  void success() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTEXT_PATH).param("checkboxAttack1", "value"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void failure() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTEXT_PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
