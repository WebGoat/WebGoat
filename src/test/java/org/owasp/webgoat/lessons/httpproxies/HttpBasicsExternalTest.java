/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpproxies;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class HttpBasicsExternalTest extends LessonTest {

  @Test
  void externalSuccess() throws Exception {
      mockMvc
          .perform(
                  MockMvcRequestBuilders.put("/HttpBasics/external")
                  .header("User-Agent", "Attacker")
                  .header("Content-Type", "application/json")
                  .content("{\"external\": true}"))
          .andExpect(status().isOk())
          .andExpect(
                  jsonPath(
                      "$.secret_code").exists());
  }

  @Test
  void externalFailWrongType() throws Exception {
      mockMvc
          .perform(
                  MockMvcRequestBuilders.post("/HttpBasics/external")
                  .header("User-Agent", "Attacker")
                  .header("Content-Type", "application/json")
                  .content("{\"external\": true}"))
          .andExpect(status().is4xxClientError());
  }

  @Test
  void externalFailWrongAgent() throws Exception {
      mockMvc
          .perform(
                  MockMvcRequestBuilders.put("/HttpBasics/external")
                  .header("User-Agent", "Fire fox")
                  .header("Content-Type", "application/json")
                  .content("{\"external\": true}"))
          .andExpect(status().isBadRequest());
  }

  @Test
  void externalFailWrongContentType() throws Exception {
      mockMvc
          .perform(
                  MockMvcRequestBuilders.put("/HttpBasics/external?external=true")
                  .header("User-Agent", "Attacker")
                  .header("Content-Type", "application/x-www-form-urlencoded"))
          .andExpect(status().isUnsupportedMediaType());
  }

  @Test
  void externalFailWrongBody() throws Exception {
      mockMvc
          .perform(
                  MockMvcRequestBuilders.put("/HttpBasics/external")
                  .header("User-Agent", "Attacker")
                  .header("Content-Type", "application/json")
                  .content("{\"test\": false}"))
          .andExpect(status().isBadRequest());
  }

}
