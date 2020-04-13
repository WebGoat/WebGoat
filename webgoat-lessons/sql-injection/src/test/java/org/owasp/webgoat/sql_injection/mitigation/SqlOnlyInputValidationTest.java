package org.owasp.webgoat.sql_injection.mitigation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.sql_injection.SqlLessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class SqlOnlyInputValidationTest extends SqlLessonTest {

    @Test
    public void solve() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlOnlyInputValidation/attack")
                .param("userid_sql_only_input_validation", "Smith';SELECT/**/*/**/from/**/user_system_data;--"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", containsString("passW0rD")));
    }

    @Test
    public void containsSpace() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlOnlyInputValidation/attack")
                .param("userid_sql_only_input_validation", "Smith' ;SELECT from user_system_data;--"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", containsString("Using spaces is not allowed!")));
    }
}