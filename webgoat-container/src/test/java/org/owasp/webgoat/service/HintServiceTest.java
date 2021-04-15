package org.owasp.webgoat.service;

import com.beust.jcommander.internal.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.session.WebSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.owasp.webgoat.service.HintService.URL_HINTS_MVC;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class HintServiceTest {

    private MockMvc mockMvc;
    @Mock
    private WebSession websession;
    @Mock
    private Lesson lesson;
    @Mock
    private Assignment assignment;

    @BeforeEach
    void setup() {
        this.mockMvc = standaloneSetup(new HintService(websession)).build();
    }

    @Test
    void hintsPerAssignment() throws Exception {
        Assignment assignment = Mockito.mock(Assignment.class);
        when(assignment.getPath()).thenReturn("/HttpBasics/attack1");
        when(assignment.getHints()).thenReturn(Lists.newArrayList("hint 1", "hint 2"));
        when(lesson.getAssignments()).thenReturn(Lists.newArrayList(assignment));
        when(websession.getCurrentLesson()).thenReturn(lesson);
        mockMvc.perform(MockMvcRequestBuilders.get(URL_HINTS_MVC))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hint", CoreMatchers.is("hint 1")))
                .andExpect(jsonPath("$[0].assignmentPath", CoreMatchers.is("/HttpBasics/attack1")));
    }
}