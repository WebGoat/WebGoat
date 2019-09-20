package org.owasp.webgoat.ssrf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author afry 
 * @since 12/28/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SSRFTest1 extends LessonTest {

    @Autowired
    private SSRF ssrf;

    @Before
    public void setup() throws Exception {
        when(webSession.getCurrentLesson()).thenReturn(ssrf);
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
