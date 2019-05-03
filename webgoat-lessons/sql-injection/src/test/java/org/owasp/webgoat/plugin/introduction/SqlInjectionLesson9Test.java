package org.owasp.webgoat.plugin.introduction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.owasp.webgoat.session.WebgoatContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Benedikt Stuhrmann
 * @since 11/07/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson9Test extends LessonTest {

    @Autowired
    private WebgoatContext context;

    private String completedError = "JSON path \"lessonCompleted\"";

    @Before
    public void setup() {
        SqlInjection sql = new SqlInjection();
        when(webSession.getCurrentLesson()).thenReturn(sql);
        when(webSession.getWebgoatContext()).thenReturn(context);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void oneAccount() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.9.one")))))
                    .andExpect(jsonPath("$.output", containsString("<table><tr><th>")));
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.9.success")))))
                    .andExpect(jsonPath("$.output", containsString("<table><tr><th>")));
        }
    }

    @Test
    public void multipleAccounts() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.9.one")))))
                    .andExpect(jsonPath("$.output", containsString("<tr><td>96134<\\/td><td>Bob<\\/td><td>Franco<\\/td><td>Marketing<\\/td><td>83700<\\/td><td>LO9S2V<\\/td><\\/tr>")));
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.9.success")))))
                    .andExpect(jsonPath("$.output", containsString("<tr><td>96134<\\/td><td>Bob<\\/td><td>Franco<\\/td><td>Marketing<\\/td><td>83700<\\/td><td>LO9S2V<\\/td><\\/tr>")));
        }
    }

    @Test
    public void wrongNameReturnsNoAccounts() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.8.no.results")))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.8.no.success")))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        }
    }

    @Test
    public void wrongTANReturnsNoAccounts() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", ""))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.8.no.results")))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", ""))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.9.success")))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        }
    }

    @Test
    public void malformedQueryReturnsError() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1'"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.error")))))
                    .andExpect(jsonPath("$.output", containsString("feedback-negative")));
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1'"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.9.success")))))
                    .andExpect(jsonPath("$.output", containsString("feedback-negative")));
        }
    }

    @Test
    public void SmithIsMostEarningCompletesAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                .param("name", "Smith")
                .param("auth_tan", "3SL99A'; UPDATE employees SET salary = '300000' WHERE last_name = 'Smith"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.9.success")))))
                .andExpect(jsonPath("$.output", containsString("300000")));
    }
}