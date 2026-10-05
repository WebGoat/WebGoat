/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class DeserializeTest extends LessonTest {

  @Test
  void success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                .param(
                    "token",
                    encodeToken(taskJson("wait", "sleep 5", LocalDateTime.now()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  void malformedToken() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                .param("token", "not-a-valid-base64-token%%"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void rejectsJavaSerializedPayload() throws Exception {
    String token =
        "rO0ABXNyADFvcmcuZHVtbXkuaW5zZWN1cmUuZnJhbWV3b3JrLlZ1bG5lcmFibGVUYXNrSG9sZGVyAAAAAAAAAAECAANMABZyZXF1ZXN0ZWRFeGVjdXRpb25UaW1ldAAZTGphdmEvdGltZS9Mb2NhbERhdGVUaW1lO0wACnRhc2tBY3Rpb250ABJMamF2YS9sYW5nL1N0cmluZztMAAh0YXNrTmFtZXEAfgACeHBzcgANamF2YS50aW1lLlNlcpVdhLobIkiyDAAAeHB3DgUAAAfjCR4GIQgMLRSoeHQACmVjaG8gaGVsbG90AAhzYXlIZWxsbw";
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                is(messages.getMessage("insecure-deserialization.invalidversion"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void expiredTask() throws Exception {
    String token =
        encodeToken(taskJson("wait", "sleep 5", LocalDateTime.now().minusMinutes(20)));
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                is(messages.getMessage("insecure-deserialization.expired"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void rejectsMissingTaskFields() throws Exception {
    String token = encodeToken(taskJson("", "sleep 5", LocalDateTime.now()));
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                is(messages.getMessage("insecure-deserialization.wrongobject"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  private static String taskJson(String taskName, String taskAction, LocalDateTime executionTime) {
    return """
        {"taskName":"%s","taskAction":"%s","requestedExecutionTime":"%s"}
        """
        .formatted(taskName, taskAction, executionTime);
  }

  private static String encodeToken(String payload) {
    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
  }
}
