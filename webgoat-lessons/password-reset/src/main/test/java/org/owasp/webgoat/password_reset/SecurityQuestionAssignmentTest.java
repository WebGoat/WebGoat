package org.owasp.webgoat.password_reset;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityQuestionAssignmentTest extends LessonTest {

    @Autowired
    private PasswordReset passwordReset;

    @Before
    public void setup() {
        Mockito.when(webSession.getCurrentLesson()).thenReturn(passwordReset);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Mockito.when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void oneQuestionShouldNotSolveTheAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("password-questions-one-successful"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)))
                .andExpect(jsonPath("$.output", CoreMatchers.notNullValue()));
    }

    @Test
    public void twoQuestionsShouldSolveTheAssignment() throws Exception {
        MockHttpSession mocksession = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?").session(mocksession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));

        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "In what year was your mother born?").session(mocksession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))))
                .andExpect(jsonPath("$.output", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void answeringSameQuestionTwiceShouldNotSolveAssignment() throws Exception {
        MockHttpSession mocksession = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?").session(mocksession))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?").session(mocksession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("password-questions-one-successful"))))
                .andExpect(jsonPath("$.output", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void solvingForOneUserDoesNotSolveForOtherUser() throws Exception {
        MockHttpSession mocksession = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?").session(mocksession));
        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "In what year was your mother born?").session(mocksession))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));

        MockHttpSession mocksession2 = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?").session(mocksession2)).
                andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}