package org.owasp.webgoat.path_traversal;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class ProfileUploadFixTest extends LessonTest {

    @Autowired
    private PathTraversal pathTraversal;

    @BeforeEach
    public void setup() {
        Mockito.when(webSession.getCurrentLesson()).thenReturn(pathTraversal);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Mockito.when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void solve() throws Exception {
        var profilePicture = new MockMultipartFile("uploadedFileFix", "../picture.jpg", "text/plain", "an image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload-fix")
                .file(profilePicture)
                .param("fullNameFix", "..././John Doe"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.assignment", CoreMatchers.equalTo("ProfileUploadFix")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void normalUpdate() throws Exception {
        var profilePicture = new MockMultipartFile("uploadedFileFix", "picture.jpg", "text/plain", "an image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/PathTraversal/profile-upload-fix")
                .file(profilePicture)
                .param("fullNameFix", "John Doe"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("unit-test\\"+File.separator+"John Doe")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }


}