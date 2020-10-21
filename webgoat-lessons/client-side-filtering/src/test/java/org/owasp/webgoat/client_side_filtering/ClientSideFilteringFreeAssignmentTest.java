package org.owasp.webgoat.client_side_filtering;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClientSideFilteringFreeAssignmentTest extends LessonTest {

    @Autowired
    private ClientSideFiltering clientSideFiltering;

    @Before
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