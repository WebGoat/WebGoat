package org.owasp.webgoat.lessons.cia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * @author Benedikt Stuhrmann
 * @since 13/03/19.
 */
public class CIAQuizTest extends LessonTest {

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(new CIA());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void allAnswersCorrectIsSuccess() throws Exception {
        String[] solution0 = {"Solution 3"};
        String[] solution1 = {"Solution 1"};
        String[] solution2 = {"Solution 4"};
        String[] solution3 = {"Solution 2"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(true)));
    }

    @Test
    public void oneAnswerWrongIsFailure() throws Exception {
        String[] solution0 = {"Solution 1"};
        String[] solution1 = {"Solution 1"};
        String[] solution2 = {"Solution 4"};
        String[] solution3 = {"Solution 2"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)));
    }

    @Test
    public void twoAnswersWrongIsFailure() throws Exception {
        String[] solution0 = {"Solution 1"};
        String[] solution1 = {"Solution 1"};
        String[] solution2 = {"Solution 4"};
        String[] solution3 = {"Solution 3"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)));
    }

    @Test
    public void threeAnswersWrongIsFailure() throws Exception {
        String[] solution0 = {"Solution 1"};
        String[] solution1 = {"Solution 1"};
        String[] solution2 = {"Solution 1"};
        String[] solution3 = {"Solution 3"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)));
    }

    @Test
    public void allAnswersWrongIsFailure() throws Exception {
        String[] solution0 = {"Solution 2"};
        String[] solution1 = {"Solution 1"};
        String[] solution2 = {"Solution 3"};
        String[] solution3 = {"Solution 1"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)));
    }

    @Test
    public void allAnswersCorrectGetResultsReturnsTrueTrueTrueTrue() throws Exception {
        String[] solution0 = {"Solution 3"};
        String[] solution1 = {"Solution 1"};
        String[] solution2 = {"Solution 4"};
        String[] solution3 = {"Solution 2"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cia/quiz"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("[ true, true, true, true ]");
    }

    @Test
    public void firstAnswerFalseGetResultsReturnsFalseTrueTrueTrue() throws Exception {
        String[] solution0 = {"Solution 2"};
        String[] solution1 = {"Solution 1"};
        String[] solution2 = {"Solution 4"};
        String[] solution3 = {"Solution 2"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cia/quiz"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("[ false, true, true, true ]");
    }

    @Test
    public void secondAnswerFalseGetResultsReturnsTrueFalseTrueTrue() throws Exception {
        String[] solution0 = {"Solution 3"};
        String[] solution1 = {"Solution 2"};
        String[] solution2 = {"Solution 4"};
        String[] solution3 = {"Solution 2"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cia/quiz"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("[ true, false, true, true ]");
    }

    @Test
    public void allAnswersFalseGetResultsReturnsFalseFalseFalseFalse() throws Exception {
        String[] solution0 = {"Solution 1"};
        String[] solution1 = {"Solution 2"};
        String[] solution2 = {"Solution 1"};
        String[] solution3 = {"Solution 1"};

        mockMvc.perform(MockMvcRequestBuilders.post("/cia/quiz")
                .param("question_0_solution", solution0)
                .param("question_1_solution", solution1)
                .param("question_2_solution", solution2)
                .param("question_3_solution", solution3));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cia/quiz"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("[ false, false, false, false ]");
    }

} // end class
