package org.owasp.webgoat.sql_injection.introduction;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.sql_injection.SqlLessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 5/21/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson5aTest extends SqlLessonTest {

    @Test
    public void knownAccountShouldDisplayData() throws Exception {
        var params = Map.of("account", "Smith", "operator", "", "injection", "");
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .params(new LinkedMultiValueMap(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", containsString("<p>USERID, FIRST_NAME")));
    }

    @Ignore
    @Test
    public void unknownAccount() throws Exception {
        var params = Map.of("account", "Smith", "operator", "", "injection", "");
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .params(new LinkedMultiValueMap(params)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("NoResultsMatched")))))
                .andExpect(jsonPath("$.output").doesNotExist());
    }

    @Test
    public void sqlInjection() throws Exception {
        var params = Map.of("account", "'", "operator", "OR", "injection", "'1' = '1");
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .params(new LinkedMultiValueMap(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", containsString("You have succeed")))
                .andExpect(jsonPath("$.output").exists());
    }

    @Test
    public void sqlInjectionWrongShouldDisplayError() throws Exception {
        var params = Map.of("account", "Smith'", "operator", "OR", "injection", "'1' = '1'");
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .params(new LinkedMultiValueMap(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", containsString(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", is("malformed string: '1''<br> Your query was: SELECT * FROM user_data WHERE" +
                        " first_name = 'John' and last_name = 'Smith' OR '1' = '1''")));
    }
}