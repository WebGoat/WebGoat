package org.owasp.webgoat.lessons.ssrf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.lessons.ssrf.SSRF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author afry 
 * @since 12/28/18.
 */
@ExtendWith(SpringExtension.class)
public class SSRFTest1 extends LessonTest {

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(new SSRF());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void modifyUrlTom() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SSRF/task1")
                .param("url", "images/tom.png"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }

    @Test
    public void modifyUrlJerry() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SSRF/task1")
                .param("url", "images/jerry.png"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void modifyUrlCat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SSRF/task1")
                .param("url", "images/cat.jpg"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }
}
