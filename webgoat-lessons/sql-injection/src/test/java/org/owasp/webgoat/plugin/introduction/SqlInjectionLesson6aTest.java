package org.owasp.webgoat.plugin.introduction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 6/15/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson6aTest extends LessonTest {

    @Before
    public void setup() throws Exception {
        when(webSession.getCurrentLesson()).thenReturn(new SqlInjection());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void wrongSolution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6a")
                .param("userid_6a", "John"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)));
    }

    @Test
    public void wrongNumberOfColumns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6a")
                .param("userid_6a", "Smith' union select userid,user_name, password,cookie from user_system_data --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.output", containsString("column number mismatch detected in rows of UNION, INTERSECT, EXCEPT, or VALUES operation")));
    }

    @Test
    public void wrongDataTypeOfColumns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6a")
                .param("userid_6a", "Smith' union select 1,password, 1,'2','3', '4',1 from user_system_data --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.output", containsString("incompatible data types in combination")));
    }

    @Test
    public void correctSolution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6a")
                .param("userid_6a", "Smith'; SELECT * from user_system_data; --"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", containsString("passW0rD")));
    }

    @Test
    public void noResultsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6a")
                .param("userid_6a", "Smith' and 1 = 2 --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.6a.no.results")))));
    }

    @Test
    public void noUnionUsed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack6a")
                .param("userid_6a", "S'; Select * from user_system_data; --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", containsString("UNION")));
    }
}