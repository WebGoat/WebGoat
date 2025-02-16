/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.pathtraversal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.WithWebGoatUser;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WithWebGoatUser
class ProfileUploadFixTest extends LessonTest {

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  void solve() throws Exception {
    var profilePicture =
        new MockMultipartFile(
            "uploadedFileFix", "../picture.jpg", "text/plain", "an image".getBytes());

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload-fix")
                .file(profilePicture)
                .param("fullNameFix", "..././John Doe"))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.assignment", CoreMatchers.equalTo("ProfileUploadFix")))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void normalUpdate() throws Exception {
    var profilePicture =
        new MockMultipartFile(
            "uploadedFileFix", "picture.jpg", "text/plain", "an image".getBytes());

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload-fix")
                .file(profilePicture)
                .param("fullNameFix", "John Doe"))
        .andExpect(status().is(200))
        .andExpect(
            jsonPath(
                "$.feedback", CoreMatchers.containsString("test\\" + File.separator + "John Doe")))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
