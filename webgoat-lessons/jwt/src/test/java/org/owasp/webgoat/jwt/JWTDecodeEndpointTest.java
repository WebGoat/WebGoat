package org.owasp.webgoat.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class JWTDecodeEndpointTest extends LessonTest {

    @Autowired
    private JWT jwt;

    @BeforeEach
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