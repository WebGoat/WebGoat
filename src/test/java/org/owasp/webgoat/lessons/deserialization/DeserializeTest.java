/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class DeserializeTest extends LessonTest {

  private static String OS = System.getProperty("os.name").toLowerCase();

  @Test
  void success() throws Exception {
    if (OS.indexOf("win") > -1) {
      mockMvc
          .perform(
              MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                  .param(
                      "token",
                      SerializationHelper.toString(
                          new VulnerableTaskHolder("wait", "ping localhost -n 5"))))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.lessonCompleted", is(true)));
    } else {
      mockMvc
          .perform(
              MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                  .param(
                      "token",
                      SerializationHelper.toString(new VulnerableTaskHolder("wait", "sleep 5"))))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }
  }

  @Test
  void fail() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                .param(
                    "token",
                    SerializationHelper.toString(new VulnerableTaskHolder("delete", "rm *"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void wrongVersion() throws Exception {
    String token =
        "rO0ABXNyADFvcmcuZHVtbXkuaW5zZWN1cmUuZnJhbWV3b3JrLlZ1bG5lcmFibGVUYXNrSG9sZGVyAAAAAAAAAAECAANMABZyZXF1ZXN0ZWRFeGVjdXRpb25UaW1ldAAZTGphdmEvdGltZS9Mb2NhbERhdGVUaW1lO0wACnRhc2tBY3Rpb250ABJMamF2YS9sYW5nL1N0cmluZztMAAh0YXNrTmFtZXEAfgACeHBzcgANamF2YS50aW1lLlNlcpVdhLobIkiyDAAAeHB3DgUAAAfjCR4GIQgMLRSoeHQACmVjaG8gaGVsbG90AAhzYXlIZWxsbw";
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("insecure-deserialization.invalidversion"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void expiredTask() throws Exception {
    String token =
        "rO0ABXNyADFvcmcuZHVtbXkuaW5zZWN1cmUuZnJhbWV3b3JrLlZ1bG5lcmFibGVUYXNrSG9sZGVyAAAAAAAAAAICAANMABZyZXF1ZXN0ZWRFeGVjdXRpb25UaW1ldAAZTGphdmEvdGltZS9Mb2NhbERhdGVUaW1lO0wACnRhc2tBY3Rpb250ABJMamF2YS9sYW5nL1N0cmluZztMAAh0YXNrTmFtZXEAfgACeHBzcgANamF2YS50aW1lLlNlcpVdhLobIkiyDAAAeHB3DgUAAAfjCR4IDC0YfvNIeHQACmVjaG8gaGVsbG90AAhzYXlIZWxsbw";
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("insecure-deserialization.expired"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void checkOtherObject() throws Exception {
    String token =
        "rO0ABXQAVklmIHlvdSBkZXNlcmlhbGl6ZSBtZSBkb3duLCBJIHNoYWxsIGJlY29tZSBtb3JlIHBvd2VyZnVsIHRoYW4geW91IGNhbiBwb3NzaWJseSBpbWFnaW5l";
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("insecure-deserialization.stringobject"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }
}
