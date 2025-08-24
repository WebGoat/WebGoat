/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.mitigation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class SqlOnlyInputValidationTest extends LessonTest {

  @Test
  public void solve() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/SqlOnlyInputValidation/attack")
                .param(
                    "userid_sql_only_input_validation",
                    "Smith';SELECT/**/*/**/from/**/user_system_data;--"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)))
        .andExpect(jsonPath("$.feedback", containsString("passW0rD")));
  }

  @Test
  public void containsSpace() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/SqlOnlyInputValidation/attack")
                .param(
                    "userid_sql_only_input_validation", "Smith' ;SELECT from user_system_data;--"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", containsString("Using spaces is not allowed!")));
  }
}
