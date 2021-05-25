package org.owasp.webgoat.chrome_dev_tools;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Benedikt Stuhrmann
 * @since 13/03/19.
 */
@ExtendWith(SpringExtension.class)
public class ChromeDevToolsTest extends LessonTest {

    @Autowired
    private ChromeDevTools cdt;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(cdt);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void NetworkAssignmentTest_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ChromeDevTools/network")
                .param("network_num", "123456")
                .param("number", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", Matchers.is(true)));
    }

    @Test
    public void NetworkAssignmentTest_Fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ChromeDevTools/network")
                .param("network_num", "123456")
                .param("number", "654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", Matchers.is(false)));
    }

}