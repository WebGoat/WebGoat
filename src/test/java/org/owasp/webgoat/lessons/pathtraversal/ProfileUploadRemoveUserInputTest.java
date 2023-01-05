package org.owasp.webgoat.lessons.pathtraversal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ProfileUploadRemoveUserInputTest extends LessonTest {

  @BeforeEach
  public void setup() {
    Mockito.when(webSession.getCurrentLesson()).thenReturn(new PathTraversal());
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    Mockito.when(webSession.getUserName()).thenReturn("unit-test");
  }

  @Test
  public void solve() throws Exception {
    var profilePicture =
        new MockMultipartFile(
            "uploadedFileRemoveUserInput", "../picture.jpg", "text/plain", "an image".getBytes());

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload-remove-user-input")
                .file(profilePicture)
                .param("fullNameFix", "John Doe"))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.assignment", CoreMatchers.equalTo("ProfileUploadRemoveUserInput")))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  public void normalUpdate() throws Exception {
    var profilePicture =
        new MockMultipartFile(
            "uploadedFileRemoveUserInput", "picture.jpg", "text/plain", "an image".getBytes());

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload-remove-user-input")
                .file(profilePicture)
                .param("fullNameFix", "John Doe"))
        .andExpect(status().is(200))
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.containsString("unit-test\\" + File.separator + "picture.jpg")))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
