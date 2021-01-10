package org.owasp.webgoat.jwt;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class JWTDecodeEndpointTest extends LessonTest {

    @Autowired
    private JWT jwt;

    @Before
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(jwt);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void solveAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/decode")
                .param("jwt-encode-user", "user")
                .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void wrongUserShouldNotSolveAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/decode")
                .param("jwt-encode-user", "wrong")
                .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)));
    }
}