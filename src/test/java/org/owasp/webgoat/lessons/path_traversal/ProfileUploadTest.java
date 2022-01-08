package org.owasp.webgoat.lessons.path_traversal;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.lessons.path_traversal.PathTraversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileUploadTest extends LessonTest {

    @BeforeEach
    public void setup() {
        Mockito.when(webSession.getCurrentLesson()).thenReturn(new PathTraversal());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Mockito.when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void solve() throws Exception {
        var profilePicture = new MockMultipartFile("uploadedFile", "../picture.jpg", "text/plain", "an image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload")
                .file(profilePicture)
                .param("fullName", "../John Doe"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.assignment", CoreMatchers.equalTo("ProfileUpload")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void attemptWithWrongDirectory() throws Exception {
        var profilePicture = new MockMultipartFile("uploadedFile", "../picture.jpg", "text/plain", "an image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload")
                .file(profilePicture)
                .param("fullName", "../../" + webSession.getUserName()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.assignment", CoreMatchers.equalTo("ProfileUpload")))
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("Nice try")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void shouldNotOverrideExistingFile() throws Exception {
        var profilePicture = new MockMultipartFile("uploadedFile", "picture.jpg", "text/plain", "an image".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload")
                .file(profilePicture)
                .param("fullName", ".."+File.separator + webSession.getUserName()))
                .andExpect(jsonPath("$.output", CoreMatchers.anyOf(
                		CoreMatchers.containsString("Is a directory"),
                		CoreMatchers.containsString("..\\\\"+ webSession.getUserName()))))
                .andExpect(status().is(200));
    }

    @Test
    public void normalUpdate() throws Exception {
        var profilePicture = new MockMultipartFile("uploadedFile", "picture.jpg", "text/plain", "an image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload")
                .file(profilePicture)
                .param("fullName", "John Doe"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsStringIgnoringCase("PathTraversal\\"+File.separator+"unit-test\\"+File.separator+"John Doe")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

}
