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

public class DOMCrossSiteScriptingTest extends LessonTest {

  @Test
  void success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/CrossSiteScripting/phone-home-xss")
                .header("webgoat-requested-by", "dom-xss-vuln")
                .param("param1", "42")
                .param("param2", "24"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void failure() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/CrossSiteScripting/phone-home-xss")
                .header("webgoat-requested-by", "wrong-value")
                .param("param1", "22")
                .param("param2", "20"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
