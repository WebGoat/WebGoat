package org.owasp.webgoat.client_side_filtering;

import org.hamcrest.CoreMatchers;
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

@ExtendWith(SpringExtension.class)
public class ClientSideFilteringFreeAssignmentTest extends LessonTest {

    @Autowired
    private ClientSideFiltering clientSideFiltering;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(clientSideFiltering);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clientSideFiltering/attack1")
                .param("answer", "450000"))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void wrongSalary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clientSideFiltering/attack1")
                .param("answer", "10000"))
                .andExpect(jsonPath("$.feedback", CoreMatchers.is("This is not the salary from Neville Bartholomew...")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void getSalaries() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clientSideFiltering/salaries"))
                .andExpect(jsonPath("$[0]", Matchers.hasKey("UserID")))
                .andExpect(jsonPath("$.length()", CoreMatchers.is(12)));
    }
}